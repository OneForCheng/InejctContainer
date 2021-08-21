package com.thoughtworks.InjectContainer;

import com.thoughtworks.InjectContainer.annotation.Inject;
import com.thoughtworks.InjectContainer.annotation.Qualifier;
import com.thoughtworks.InjectContainer.annotation.Singleton;
import com.thoughtworks.InjectContainer.exception.InjectException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class InjectContainer {
    private final Set<Class<?>> creatingClasses = Collections.synchronizedSet(new HashSet<>());

    private final Map<Class<?>, Object> singletonClasses = Collections.synchronizedMap(new HashMap<>());

    private final Map<Annotation, Class<?>> qualifiedClasses = Collections.synchronizedMap(new HashMap<>());

    public  <T> T  getInstance(Class<T> clazz) {
        List<Constructor<?>> injectableConstructors = getInjectableConstructors(clazz);

        validateInjectableConstructors(clazz, injectableConstructors);

        Constructor<T> constructor = (Constructor<T>)injectableConstructors.get(0);

        boolean isSingletonClass = clazz.isAnnotationPresent(Singleton.class);

        if (isSingletonClass) {
            Object singleton = singletonClasses.get(clazz);
            if (singleton != null) return (T)singleton;
        }

        creatingClasses.add(clazz);

        T target = createFromConstructor(constructor);

        creatingClasses.remove(clazz);

        if (isSingletonClass) {
            singletonClasses.put(clazz, target);
        }

        return target;
    }

    private <T> T createFromConstructor(Constructor<T> constructor) {
        Object[] params = Arrays.stream(constructor.getParameters()).map(param -> {
            validateParameter(constructor, param);
            return createFromParameter(param);
        }).toArray();
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw  new InjectException(String.format("create instance error from %s constructor", constructor.getDeclaringClass().getSimpleName()), e);
        }
    }

    private Object createFromParameter(Parameter parameter) {
        List<Annotation> annotations = getQualifierAnnotations(parameter.getAnnotations());
        if (!annotations.isEmpty()) {
            Set<? extends Class<?>> registeredQualifiedClasses = annotations.stream()
                    .filter(qualifiedClasses::containsKey)
                    .map(qualifiedClasses::get)
                    .collect(Collectors.toSet());
            if (registeredQualifiedClasses.size() == 1) {
                Class<?> clazz = registeredQualifiedClasses.iterator().next();
                return getInstance(clazz);
            } else if (registeredQualifiedClasses.size() > 1) {
                throw new InjectException(String.format("multiple injectable class %s for injection parameter %s because of using multiple annotations %s",
                        registeredQualifiedClasses.stream().map(Class::getSimpleName).collect(Collectors.joining(",")),
                        parameter.getType().getSimpleName(),
                        annotations.stream().map(annotation -> annotation.annotationType().getSimpleName()).collect(Collectors.joining(",")))
                );
            }
        }

        Class<?> clazz = parameter.getType();
        return getInstance(clazz);
    }

    private List<Constructor<?>> getInjectableConstructors(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        return Arrays.stream(constructors)
                .filter(constructor -> Modifier.isPublic(constructor.getModifiers()))
                .filter(constructor -> constructor.getParameterCount() == 0 || constructor.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
    }

    private <T> void validateInjectableConstructors(Class<T> clazz, List<Constructor<?>> injectableConstructors) {
        int size = injectableConstructors.size();
        if (size == 0) {
            throw new InjectException(String.format("no accessible constructor for injection class %s", clazz.getSimpleName()));
        }
        if (size > 1) {
            throw new InjectException(String.format("multiple injectable constructor for injection class %s", clazz.getSimpleName()));
        }
    }

    private <T> void validateParameter(Constructor<T> constructor, Parameter param) {
        if (creatingClasses.contains(param.getType())) {
            throw new InjectException(String.format("circular dependency on constructor, the class is %s", constructor.getDeclaringClass().getSimpleName()));
        }
    }

    public void registerQualifiedClass(Class<?> clazz) {
        List<Annotation> annotations = getQualifierAnnotations(clazz.getAnnotations());
        annotations.forEach(annotation -> qualifiedClasses.put(annotation, clazz));
    }

    private List<Annotation> getQualifierAnnotations(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(Qualifier.class))
                .collect(Collectors.toList());
    }
}
