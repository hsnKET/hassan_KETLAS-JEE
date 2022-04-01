package net.ketlas.di.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Util {

    /**
     * check if given method is setter
     * @param m The method
     * @return boolean
     */
    public static boolean isSetter(Method m){
        return Modifier.isPublic(m.getModifiers()) &&
                m.getName().toLowerCase().startsWith("set") &&
                (Void.class == m.getReturnType() ||
                        void.class == m.getReturnType() )&&
                m.getParameterCount() == 1;
    }

    /**
     * Get primitive value of given class name and string value
     * @param className type of value
     * @param value value in string format
     * @return Object The instance of given class
     */
    public static Object resolvePrimitiveArg(String className, String value) {
        if ("string".equalsIgnoreCase(className) ||
                String.class.getName().equals(className)) {
            return value;
        } else if ("int".equalsIgnoreCase(className) ||
                int.class.getName().equals(className) ||
                Integer.class.getName().equals(className)) {
            return Integer.valueOf(value);
        } else if ("double".equalsIgnoreCase(className) ||
                double.class.getName().equals(className) ||
                Double.class.getName().equals(className)) {
            return Double.valueOf(value);
        } else if ("float".equalsIgnoreCase(className) ||
                float.class.getName().equals(className) ||
                Float.class.getName().equals(className)) {
            return Float.valueOf(value);
        } else if ("short".equalsIgnoreCase(className) ||
                short.class.getName().equals(className) ||
                Short.class.getName().equals(className)) {
            return Short.valueOf(value);
        } else if ("boolean".equalsIgnoreCase(className) ||
                boolean.class.getName().equals(className) ||
                Boolean.class.getName().equals(className)) {
            return Boolean.valueOf(value);
        } else
            return value;

    }

}
