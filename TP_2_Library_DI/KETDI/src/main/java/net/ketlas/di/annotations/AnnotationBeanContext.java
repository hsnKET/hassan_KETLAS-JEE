package net.ketlas.di.annotations;

import net.ketlas.di.BeanFactory;
import net.ketlas.di.annotations.stereotype.Autowired;
import net.ketlas.di.annotations.stereotype.Component;
import net.ketlas.di.annotations.stereotype.CustomValue;
import net.ketlas.di.annotations.stereotype.Qualifier;
import net.ketlas.di.exceptions.KETDIException;
import net.ketlas.di.utils.Util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationBeanContext implements BeanFactory {

    private static AnnotationBeanContext instance;
    private Map<Class<?>, Object> beans;
    private Map<Class, Class> impClasses;
    private List<String> packages;

    /**
     * @param pkg The String array of package names to search on
     */
    private AnnotationBeanContext(String... pkg) {
        beans = new HashMap<>();
        impClasses = new HashMap<>();
        packages = Arrays.stream(pkg).collect(Collectors.toList());
        init();
    }

    /**
     * @param startUpClass The start up class
     * @return singleton instance of AnnotationBeanContext
     */
    public static AnnotationBeanContext getInstance(Class startUpClass) {
        return getInstance(startUpClass.getPackage().getName());
    }

    /**
     * @param pkg array of package to search on
     * @return singleton instance of AnnotationBeanContext
     */
    public static AnnotationBeanContext getInstance(String... pkg) {
        if (instance == null)
            instance = new AnnotationBeanContext(pkg);
        return instance;
    }

    /**
     * Load classes and interfaces from packages
     * and instantiate them (classes)
     * and autowired fields and setters methods
     */
    private void init() {
        List<Class> classes = getAllClasses();
        classes.stream().filter(clazz -> clazz.isAnnotationPresent(Component.class))
                .forEach(classWithAnnotation -> {
                    Arrays.stream(classWithAnnotation.getInterfaces()).
                            forEach(inter -> {
                                impClasses.put(inter, classWithAnnotation);
                            });
                });

        classes.stream().filter(aClass -> aClass.isAnnotationPresent(Component.class))
                .forEach(clazz -> {
                    Object obj = createObject(clazz);
                    this.beans.put(clazz, obj);
                    this.autowiredFields(clazz, obj);
                    this.autowiredSetters(clazz, obj);

                });

    }

    /**
     *Set value to fields width autowired annotation
     * and handel also qualified annotation
     * @param clazz The class to search on
     * @param objInstance instance of @param clazz
     */
    private void autowiredFields(Class clazz, Object objInstance) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                Object fieldInstance = null;
                String qualifier = field.isAnnotationPresent(Qualifier.class) ? field.getAnnotation(Qualifier.class).name().trim() : "";
                if (!qualifier.isEmpty()) {
                    fieldInstance = beans.entrySet()
                            .stream()
                            .filter(entry -> entry.getKey().getSimpleName().equals(qualifier))
                            .map(entry -> entry.getValue())
                            .findFirst().orElse(null);
                } else {
                    fieldInstance = getBean(field.getType());
                }
                try {
                    field.set(objInstance, fieldInstance);
                    autowiredFields(field.getClass(), fieldInstance);
                } catch (IllegalAccessException e) {
                    throw new KETDIException("Unable to access field " + field.getName() + " in class " + clazz.getName());
                }
            }
        }
    }

    /**
     * Invoke setter autowired methods
     * @param clazz The class object of method
     * @param objInstance The instance of class
     */
    private void autowiredSetters(Class clazz, Object objInstance) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Autowired.class) && Util.isSetter(method)) {
                Parameter[] parameters = method.getParameters();
                try {
                    method.invoke(objInstance, getParamsObjs(parameters));
                } catch (IllegalAccessException e) {
                    throw new KETDIException("Unable to access method " + method.getName() + " in class " + clazz.getName());
                } catch (InvocationTargetException e) {
                    throw new KETDIException("The method of class " + method.getName()
                            + " lave this exception " + e.getMessage());

                }
            }
        }
    }

    /**
     * Create a new object of given class using  Autowired constructor
     * if exist if not using default constructor and generate an exception
     * if they are more than one autowired constructor,if they are
     *  a multiple constructor with autowired annotation it's will lave an exception
     * @param clazz class that we want to instantiate
     * @return new Instance of given class
     */
    private Object createObject(Class clazz) {
        try {
            Constructor[] constructors = clazz.getConstructors();
            List<Constructor> constructors2 = Arrays.stream(constructors).
                    filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                    .collect(Collectors.toList());
            if (constructors2.size() > 1)
                throw new KETDIException("The are " + constructors2.size() + " constructor with Autowired annotation");
            else if (constructors2.size() == 1) {
                Parameter[] parameters = constructors2.get(0).getParameters();
                Object obj = constructors2.get(0).newInstance(getParamsObjs(parameters));
                return obj;
            } else {
                if (clazz.isInterface()) {
                    //search for implementation of interface
                    if (impClasses.containsKey(clazz))
                        if (beans.containsKey(impClasses.get(clazz)))
                            return beans.get(impClasses.get(clazz));
                        else
                            return createObject(impClasses.get(clazz));
                    else
                        throw new KETDIException("No Impl For Interface Founded For " + clazz.getName());
                } else
                    return clazz.newInstance();
            }
        } catch (InstantiationException e) {
            throw new KETDIException("Unable to instantiate class " + clazz.getName());
        } catch (IllegalAccessException e) {
            throw new KETDIException("Unable to access class " + clazz.getName());
        } catch (InvocationTargetException e) {
            throw new KETDIException("The constructor of class " + clazz.getName()
                    + " lave this exception " + e.getMessage());
        }
    }


    /**
     * Get array of object  for given array of parameters
     * @param parameters The parameters of methods
     * @return Object[] The array contain instances of all given parameter
     */
    private Object[] getParamsObjs(Parameter[] parameters) {
        List<Object> list = new ArrayList<>();
        for (Parameter parameter : parameters) {
            if (String.class.equals(parameter.getType())) {
                if (parameter.isAnnotationPresent(CustomValue.class)) {
                    CustomValue customValue = parameter.getAnnotation(CustomValue.class);
                    list.add(Util.resolvePrimitiveArg(customValue.type(), customValue.value()));
                } else
                    list.add(new String(""));
            } else {
                list.add(createObject(parameter.getType()));
            }
        }
        return list.toArray();
    }

    /**
     * Get all classes in packages @packages
     * @return List of class exist in all @packages
     */
    private List<Class> getAllClasses() {
        List<Class> files = new ArrayList<>();
        for (String pkg : this.packages) {
            try {
                String dirPkg = pkg.replace(".", File.separator);
                Enumeration<URL> urls = ClassLoader.getSystemClassLoader().getResources(dirPkg);
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    Files.walkFileTree(Paths.get(url.toURI()), new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                            String pathName = path.toString();
                            //Avoid inner classes
                            if (pathName.endsWith(".class") && !pathName.contains("$")) {
                                int beginIndex = pathName.lastIndexOf(dirPkg);
                                int endIndex = pathName.lastIndexOf(".class");
                                String className = pathName.substring(beginIndex, endIndex)
                                        .replace(File.separator, ".");

                                try {
                                    Class clazz = Class.forName(className);
                                    files.add(clazz);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            return super.visitFile(path, attrs);
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    @Override
    public <T> T getBean(Class<T> clazz) {

        if (clazz.isInterface()) {

            if (impClasses.containsKey(clazz))
                return (T) beans.get(impClasses.get(clazz));
            else
                throw new KETDIException("No Impl Found For Interface  " + clazz.getName());
        }
        if (beans.containsKey(clazz))
            return (T) beans.get(clazz);
        else
            return (T) createObject(clazz);

    }

    @Override
    public <T> T getBean(Class<T> clazz, Object... args) {
        throw new UnsupportedOperationException("This function not implemented yet");
    }

    @Override
    public Object getBean(String name) {
        throw new UnsupportedOperationException("This function not implemented yet");
    }

    @Override
    public Object getBean(String name, Object... args) {
        throw new UnsupportedOperationException("This function not implemented yet");
    }
}
