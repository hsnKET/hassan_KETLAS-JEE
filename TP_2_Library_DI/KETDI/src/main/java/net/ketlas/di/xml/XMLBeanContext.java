package net.ketlas.di.xml;

import net.ketlas.di.BeanFactory;

public class XMLBeanContext implements BeanFactory {

    private static XMLBeanContext instance;
    private String config;
    private Resource resource;
    private BeanLoader beanResolver;

    private XMLBeanContext(String config) {
        this.config = config;
        resource = new XMLResource("xsd.xsd", this.config);
        beanResolver = new BeanLoaderImpl(resource);
    }

    public static XMLBeanContext getInstance(String config) {
        if (instance == null) {
            instance = new XMLBeanContext(config);
        }
        return instance;
    }

    @Override
    public Object getBean(String name) {
        return beanResolver.getBeanByName(name);
    }

    @Override
    public Object getBean(String name, Object...args) {
        throw new UnsupportedOperationException("This function not implemented yet");
    }
    @Override
    public <T> T getBean(Class<T> clazz) {
        return (T)beanResolver.getBeanByClassName(clazz);
    }

    @Override
    public <T> T getBean(Class<T> clazz, Object... args) {
        throw new UnsupportedOperationException("This function not implemented yet");
    }
}
