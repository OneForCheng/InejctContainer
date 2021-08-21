package com.thoughtworks.InjectContainer;

import com.thoughtworks.InjectContainer.models.BaseTest;
import com.thoughtworks.InjectContainer.enums.ExecuteStatus;
import com.thoughtworks.fusheng.integration.junit5.FuShengTest;

@FuShengTest
public class InjectContainerTest extends BaseTest {
    Object instance;

    public  String getInstance(String className) throws ClassNotFoundException {
        Class<?> clazz = getClassByName(className);

        try {
            instance = container.getInstance(clazz);
        } catch (Exception e) {
            return ExecuteStatus.FAILURE.getDescription();
        }

        return ExecuteStatus.SUCCESS.getDescription();
    }

    public String getInstanceClassName() {
        return instance != null ? instance.getClass().getSimpleName() : null;
    }
}