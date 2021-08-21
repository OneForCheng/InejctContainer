package com.thoughtworks.InjectContainer;

import com.thoughtworks.InjectContainer.models.BaseTest;
import com.thoughtworks.InjectContainer.enums.ExecuteStatus;
import com.thoughtworks.fusheng.integration.junit5.FuShengTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@FuShengTest
public class InjectContainerTest extends BaseTest {
    private Object instance;
    private Exception exception;
    private final String NULL = "null";

    public  String getInstance(String className) {
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

    public String getInstanceClassName() {
        return instance != null ? instance.getClass().getSimpleName() : NULL;
    }

    public String getExceptionMessage() {
        return exception != null ? exception.getMessage() : NULL;
    }

    public String invokeInstanceMethodWithReturnValue(String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = instance.getClass().getMethod(methodName);
        return method.invoke(instance).toString();
    }
}