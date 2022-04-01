package net.ketlas.di;

public interface BeanFactory {

    Object getBean(String name);
    Object getBean(String name,Object...args);
    <T> T getBean(Class<T> clazz);
    <T> T getBean(Class<T> clazz,Object...args);
    
}
