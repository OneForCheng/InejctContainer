package com.thoughtworks.InjectContainer;

import com.thoughtworks.InjectContainer.annotation.Singleton;
import com.thoughtworks.InjectContainer.exception.InjectException;
import com.thoughtworks.InjectContainer.resolver.QualifierResolver;
import com.thoughtworks.InjectContainer.resolver.ConstructorResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;

public class InjectContainer {
    private final Set<Class<?>> creatingClasses = Collections.synchronizedSet(new HashSet<>());

    private final Map<Class<?>, Object> singletonClasses = Collections.synchronizedMap(new HashMap<>());

    private final QualifierResolver qualifierResolver = new QualifierResolver();

    public void registerQualifiedClass(Class<?> clazz) {
        qualifierResolver.registerQualifiedClass(clazz);
    }

    public  <T> T  getInstance(Class<T> clazz) {
        T target;

        boolean isSingletonClass = clazz.isAnnotationPresent(Singleton.class);
        Object instance = singletonClasses.get(clazz);

        if (isSingletonClass && instance != null) {
            target =  (T)instance;
        } else {
            creatingClasses.add(clazz);

            target = createFromClass(clazz);

            creatingClasses.remove(clazz);

            if (isSingletonClass) {
                singletonClasses.put(clazz, target);
            }
        }

        return target;
    }

    private <T> T createFromClass(Class<T> clazz) {
        Constructor<T> constructor = ConstructorResolver.getUniqueInjectableConstructor(clazz);
        return createFromConstructor(constructor);
    }

    private <T> T createFromConstructor(Constructor<T> constructor) {
        Object[] params = Arrays.stream(constructor.getParameters()).map(param -> {
            if (creatingClasses.contains(param.getType())) {
                throw new InjectException(String.format("circular dependency on constructor, the class is %s", constructor.getDeclaringClass().getSimpleName()));
            }
            return createFromParameter(param);
        }).toArray();
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw  new InjectException(String.format("create instance error from %s constructor", constructor.getDeclaringClass().getSimpleName()), e);
        }
    }

    private Object createFromParameter(Parameter parameter) {
        Class<?> clazz = qualifierResolver.getUniqueRegisteredQualifiedClassOrNull(parameter);

        if (clazz == null) clazz = parameter.getType();

        return getInstance(clazz);
    }
}
