package com.thoughtworks.InjectContainer;

import com.thoughtworks.InjectContainer.exception.InjectException;
import com.thoughtworks.InjectContainer.resolver.QualifierResolver;
import com.thoughtworks.InjectContainer.resolver.ConstructorResolver;
import com.thoughtworks.InjectContainer.resolver.SingletonResolver;
import com.thoughtworks.InjectContainer.validator.CircularDependencyValidator;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;

public class InjectContainer {
    private final CircularDependencyValidator circularDependencyValidator = new CircularDependencyValidator();

    private final SingletonResolver singletonResolver = new SingletonResolver();

    private final QualifierResolver qualifierResolver = new QualifierResolver();

    private final Map<Class<?>, Set<Class<?>>> interfaceImplementations = Collections.synchronizedMap(new HashMap<>());

    public void registerQualifiedClass(Class<?> clazz) {
        qualifierResolver.registerQualifiedClass(clazz);
    }

    public void registerInterfaceImplementation(Class<?> registerInterface, Class<?> clazz) {
        Set<Class<?>> classes = interfaceImplementations.get(registerInterface);
        if (classes == null) {
            classes = Collections.synchronizedSet(new LinkedHashSet<>());
            interfaceImplementations.put(registerInterface, classes);
        }
        classes.add(clazz);
    }

    public <T> T[] getInterfaceInstances(Class<T> clazz) {
        Set<Class<?>> classes = interfaceImplementations.get(clazz);
        if (classes == null) {
            return null;
        }
        return classes.stream().map(item -> getInstance(item)).toArray(size -> (T[]) Array.newInstance(clazz, size));
    }

    public  <T> T  getInstance(Class<T> clazz) {
        circularDependencyValidator.entry(clazz);

        Object instance = singletonResolver.getSingletonOrNull(clazz);

        if (instance == null) {
            instance = createFromClass(clazz);
            singletonResolver.tryRegisterSingleton(clazz, instance);
        }

        circularDependencyValidator.exit(clazz);

        return (T)instance;
    }

    private <T> T createFromClass(Class<T> clazz) {
        Constructor<T> constructor = ConstructorResolver.getUniqueInjectableConstructor(clazz);
        return createFromConstructor(constructor);
    }

    private <T> T createFromConstructor(Constructor<T> constructor) {
        Object[] params = Arrays.stream(constructor.getParameters()).map(this::createFromParameter).toArray();
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw  new InjectException(String.format("create instance error from %s constructor", constructor.getDeclaringClass().getSimpleName()), e);
        }
    }

    private Object createFromParameter(Parameter parameter) {
        if (parameter.getType().isArray()) {
            return createFromArrayParameter(parameter);
        }
        return createFromNormalParameter(parameter);
    }

    private Object[] createFromArrayParameter(Parameter parameter) {
        Class<?> clazz = parameter.getType().getComponentType();
        return getInterfaceInstances(clazz);
    }

    private Object createFromNormalParameter(Parameter parameter) {
        Class<?> clazz = qualifierResolver.getUniqueRegisteredQualifiedClassOrNull(parameter);
        if (clazz == null) clazz = parameter.getType();
        return getInstance(clazz);
    }
}
