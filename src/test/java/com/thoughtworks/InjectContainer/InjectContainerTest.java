package com.thoughtworks.InjectContainer;

import com.thoughtworks.InjectContainer.model.BaseTest;
import com.thoughtworks.InjectContainer.constant.ExecuteStatus;
import com.thoughtworks.fusheng.integration.junit5.FuShengTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@FuShengTest
public class InjectContainerTest extends BaseTest {
    private Object instance;
    private Object[] interfaceInstances;
    private Exception exception;
    private final String NULL = "null";

    public String getInstance(String className) {
        instance = null;
        exception = null;

        try {
            Class<?> clazz = getClassByName(className);
            instance = container.getInstance(clazz);
        } catch (Exception e) {
            exception = e;
            return ExecuteStatus.FAILURE.getDescription();
        }

        return ExecuteStatus.SUCCESS.getDescription();
    }

    public  String getInterfaceInstances(String className) {
        interfaceInstances = null;
        exception = null;

        try {
            Class<?> clazz = getClassByName(className);
            interfaceInstances = container.getInterfaceInstances(clazz);
        } catch (Exception e) {
            exception = e;
            return ExecuteStatus.FAILURE.getDescription();
        }

        return ExecuteStatus.SUCCESS.getDescription();
    }

    public void registerQualifiedClass(String className) throws ClassNotFoundException {
        Class<?> clazz = getClassByName(className);
        container.registerQualifiedClass(clazz);
    }

    public void registerInterfaceImplementation(String className) throws ClassNotFoundException {
        Class<?> clazz = getClassByName(className);
        container.registerInterfaceImplementation(currentRegisterInterface, clazz);
    }

    public String getInstanceClassName() {
        return instance != null ? instance.getClass().getSimpleName() : NULL;
    }

    public String getInterfaceInstanceClassName(String serialNumber) {
        int index = Integer.parseInt(serialNumber) - 1;
        return interfaceInstances != null ? interfaceInstances[index].getClass().getSimpleName() : NULL;
    }

    public String getExceptionMessage() {
        return exception != null ? exception.getMessage() : NULL;
    }

    public Object invokeInstanceMethodWithReturnValue(String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = instance.getClass().getMethod(methodName);
        Object returnValue = method.invoke(instance);
        if (returnValue instanceof Number) {
            return returnValue;
        }
        return returnValue.toString();
    }
}