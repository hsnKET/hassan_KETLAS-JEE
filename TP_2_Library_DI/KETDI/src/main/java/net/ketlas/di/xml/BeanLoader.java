package net.ketlas.di.xml;

import net.ketlas.di.xml.bean.Bean;
import net.ketlas.di.xml.bean.ConstructorArg;

import java.lang.reflect.Constructor;

public interface BeanLoader {

    Object createObjet(Bean beanDefinition);
    Constructor getConstructor(Bean beanDefinition);
    Object[] resolveParams(ConstructorArg[] paramsDefinition);
    Bean getBeanDefinitionById(String id);
    Object getBeanByName(String name);
    Object getBeanById(String id);
    Object getBeanByClassName(String className);
    Object getBeanByClassName(Class clazz);

}
