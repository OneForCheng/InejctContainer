package com.thoughtworks.InjectContainer;

public class InjectContainer {
    public  <T> T  getInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }
}
