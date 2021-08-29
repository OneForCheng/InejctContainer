package com.thoughtworks.InjectContainer.model;

import com.thoughtworks.InjectContainer.InjectContainer;

public abstract class BaseTest {
    protected InjectContainer container = new InjectContainer();

    protected Class<?> currentRegisterInterface;

    private String getFullPackageName(String className) {
        return BaseTest.class.getPackageName() + "." + className;
    }

    protected Class<?> getClassByName(String className) throws ClassNotFoundException {
        String fullPackageName = getFullPackageName(className);
        return Class.forName(fullPackageName);
    }

    public void setCurrentRegisterInterface(String className) throws ClassNotFoundException {
        String fullPackageName = getFullPackageName(className);
        currentRegisterInterface = Class.forName(fullPackageName);
    }
}