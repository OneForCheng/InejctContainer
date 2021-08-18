package com.thoughtworks.InjectContainer;

import com.thoughtworks.fusheng.integration.junit5.FuShengTest;

class A {
    public A() {}
}

@FuShengTest
public class InjectContainerTest {

    public InjectContainer container = new InjectContainer();

    public  String getInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(InjectContainerTest.class.getPackageName() + "." + className);

        var instance = container.getInstance(clazz);

        return "成功";
    }
}