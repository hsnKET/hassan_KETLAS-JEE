package net.ketlas.di.xml;

import net.ketlas.di.exceptions.KETDIException;
import net.ketlas.di.utils.Util;
import net.ketlas.di.xml.bean.Bean;
import net.ketlas.di.xml.bean.Beans;
import net.ketlas.di.xml.bean.ConstructorArg;
import net.ketlas.di.xml.bean.Property;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanLoaderImpl implements BeanLoader {

    private Map<Bean, Object> beans;
    private Map<Class, Class> impClasses;
    private Resource resource;
    private Beans resourceBeansDefinition;

    public BeanLoaderImpl(Resource resource) {
        this.resource = resource;
        beans = new HashMap<>();
        impClasses = new HashMap<>();
        resourceBeansDefinition = resource.getResource();
        load();
    }

    private void load() {
        for (Bean bean : resourceBeansDefinition.getBean()) {
            if (beans.containsKey(bean)) continue;
            Object obj = createObjet(bean);
            addBean(bean, obj);
            initFields(bean, obj);
        }
    }

    /**
     * Initiate all non initiated fields
     *
     * @param bean Bean definition
     * @param obj  instance of bean definition
     * @return void
     */
    void initFields(Bean bean, Object obj) {
        if (obj == null) return;
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(obj) != null)
                    continue;
                for (Property property : bean.getProperties()) {
                    if (field.getType().getName().equals(property.getType())
                            && field.getName().equals(property.getName())) {
                        if (property.getRef() != null) {
                            field.set(obj, createObjet(getBeanDefinitionById(property.getRef().getBean())));
                        } else {
                            field.set(obj, Util.resolvePrimitiveArg(property.getType(), property.getValue()));
                        }
                    }
                }
            } catch (Exception e) {
                throw new KETDIException("Unable to set value in a field " +
                        field.getType() + " because of " + e.getMessage());
            }
            field.setAccessible(false);

        }

    }

    /**
     * Add bean to hashmap
     *
     * @param beanDefinition key of hashmap
     * @param obj            instance of beanDefinition
     */
    private void addBean(Bean beanDefinition, Object obj) {
        if (obj == null)
            throw new KETDIException("we can't instantiate class "+beanDefinition.getClazz());
        beans.put(beanDefinition, obj);
        for (Class inter : obj.getClass().getInterfaces())
            impClasses.put(inter, obj.getClass());
    }

    /**
     * @param id Id of bean definition
     * @return Bean
     */
    @Override
    public Bean getBeanDefinitionById(String id) {
        return resourceBeansDefinition.getBean()
                .stream()
                .filter(bean -> bean.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * create new objet of bean definition
     *
     * @param beanDefinition
     * @return new instance
     */
    @Override
    public Object createObjet(Bean beanDefinition) {
        if (beans.containsKey(beanDefinition))
            return beans.get(beanDefinition);
        Object obj;
        try {
            if (beanDefinition.getConstructorArgs().size() > 0) {
                Constructor constructor = getConstructor(beanDefinition);
                if (constructor != null) {
                    if (beanDefinition.getConstructorArgs().size() != 0)
                        obj = constructor.newInstance(resolveParams(beanDefinition.getConstructorArgs()
                                .toArray(new ConstructorArg[0])));
                    else
                        obj = constructor.newInstance();

                    return obj;
                }
            } else {
                obj = Class.forName(beanDefinition.getClazz()).newInstance();
                return obj;
            }
        } catch (InstantiationException e) {
            throw new KETDIException("Unable to instantiate class " + beanDefinition.getClazz());
        } catch (IllegalAccessException e) {
            throw new KETDIException("Unable to access class " + beanDefinition.getClazz());
        } catch (InvocationTargetException e) {
            throw new KETDIException("The constructor of class " + beanDefinition.getClazz()
                    + " lave this exception " + e.getMessage());

        } catch (ClassNotFoundException e) {
            throw new KETDIException("The  class " + beanDefinition.getClazz()
                    + " not found");
        }

        return null;
    }

    /**
     * Get constructor with match parameter with bean definition
     *
     * @param beanDefinition
     * @return Constructor object
     */
    @Override
    public Constructor getConstructor(Bean beanDefinition) {

        try {
            Class clazz = Class.forName(beanDefinition.getClazz());
            Constructor[] constructors = clazz.getConstructors();
            List<ConstructorArg> constructorArgs = beanDefinition.getConstructorArgs();
            int i = 0;
            for (Constructor constructor : constructors) {
                Parameter[] parameters = constructor.getParameters();

                if (parameters.length != constructorArgs.size())
                    continue;

                if (parameters.length == 0)
                    return constructor;

                for (Parameter parameter : parameters) {
                    if (constructorArgs.get(i).getRef() != null) {
                        Bean argObj = getBeanDefinitionById(constructorArgs.get(i).getRef().getBean());
                        if (argObj != null &&
                                !argObj.getClazz().equals(constructorArgs.get(i).getType())) {
                            i = 0;
                            break;
                        }
                    } else if (!parameter.getType().getName()
                            .equals(constructorArgs.get(i).getType())) {
                        i = 0;
                        break;
                    }
                    i++;
                }
                if (i != 0)
                    return constructor;
            }

        } catch (ClassNotFoundException e) {
            throw new KETDIException("Unable to find constructor for class "
                    + beanDefinition.getClazz() + " with " +
                    "given parameters ");
        }

        return null;
    }

    /**
     * Resolve constructor definition argument to array of objet
     *
     * @param paramsDefinition Array of constructor definition argument
     * @return Object[]
     */
    @Override
    public Object[] resolveParams(ConstructorArg[] paramsDefinition) {
        Object[] args = new Object[paramsDefinition.length];
        int i = 0;
        for (; i < paramsDefinition.length; i++) {
            ConstructorArg constructorArg = paramsDefinition[i];
            if (constructorArg.getRef() != null) {
                Bean beanRef = getBeanDefinitionById(constructorArg.getRef().getBean());
                Object obj;
                if (beans.containsKey(beanRef))
                    obj = beans.get(beanRef);
                else {
                    obj = createObjet(getBeanDefinitionById(constructorArg.getRef().getBean()));
                    addBean(beanRef, obj);
                }
                args[i] = obj;
            } else
                args[i] = Util.resolvePrimitiveArg(constructorArg.getType(), constructorArg.getValue());

        }
        return args;
    }

    /**
     * Get bean using name of bean definition
     *
     * @param name Name of bean definition
     * @return Object
     */
    @Override
    public Object getBeanByName(String name) {
        Object obj = beans.entrySet()
                .stream()
                .filter(beanObjectEntry -> beanObjectEntry.getKey().getName().equals(name))
                .map(beanObjectEntry -> beanObjectEntry.getValue())
                .findFirst()
                .orElse(null);
        if (obj == null)
            throw new KETDIException("No Bean Found For Name " + name);
        return obj;
    }

    /**
     * Get bean using id of bean definition
     *
     * @param id Id of bean definition
     * @return Object
     */
    @Override
    public Object getBeanById(String id) {
        return beans.entrySet()
                .stream()
                .filter(beanObjectEntry -> beanObjectEntry.getKey().getId().equals(id))
                .map(beanObjectEntry -> beanObjectEntry.getValue())
                .findFirst()
                .orElse(null);
    }

    /**
     * Get bean using class name String of bean definition
     *
     * @param className Id of bean definition
     * @return Object
     */
    @Override
    public Object getBeanByClassName(String className) {
        Object obj = beans.entrySet()
                .stream()
                .filter(beanObjectEntry -> beanObjectEntry.getKey().getClazz().equals(className))
                .map(beanObjectEntry -> beanObjectEntry.getValue())
                .findFirst()
                .orElse(null);
        if (obj == null)
            throw new KETDIException("No Bean Found For Class " + className);
        return obj;
    }

    /**
     * Get bean using clazz class Object  of bean
     *
     * @param clazz class of bean
     * @return Object
     */
    @Override
    public Object getBeanByClassName(Class clazz) {
        if (clazz.isInterface()) {
            if (impClasses.containsKey(clazz))
                return getBeanByClassName(impClasses.get(clazz).getName());
            else
                throw new KETDIException("No implementation for interface " + clazz.getName());
        }
        return getBeanByClassName(clazz.getName());
    }


}
