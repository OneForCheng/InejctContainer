package com.thoughtworks.InjectContainer.models;

import com.thoughtworks.InjectContainer.InjectContainer;

public abstract class BaseTest {
    protected InjectContainer container = new InjectContainer();

    private String getFullPackageName(String className) {
        return BaseTest.class.getPackageName() + "." + className;
    }

    protected Class<?> getClassByName(String className) throws ClassNotFoundException {
        String fullPackageName = getFullPackageName(className);
        return Class.forName(fullPackageName);
    }
}