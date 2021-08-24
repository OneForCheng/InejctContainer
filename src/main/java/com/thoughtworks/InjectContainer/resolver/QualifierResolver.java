package com.thoughtworks.InjectContainer.resolver;

import com.thoughtworks.InjectContainer.annotation.Qualifier;
import com.thoughtworks.InjectContainer.exception.InjectException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class QualifierResolver {
    private final Map<Annotation, Class<?>> qualifiedClasses = Collections.synchronizedMap(new HashMap<>());

    public void registerQualifiedClass(Class<?> clazz) {
        List<Annotation> annotations = getQualifierAnnotations(clazz.getAnnotations());
        annotations.forEach(annotation -> qualifiedClasses.put(annotation, clazz));
    }

    private static List<Annotation> getQualifierAnnotations(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(Qualifier.class))
                .collect(Collectors.toList());
    }

    public Class<?> getUniqueRegisteredQualifiedClassOrNull(Parameter parameter) {
        Class<?> clazz = null;

        List<Annotation> annotations = getQualifierAnnotations(parameter.getAnnotations());
        if (!annotations.isEmpty()) {
            Set<? extends Class<?>> registeredQualifiedClasses = annotations.stream().filter(qualifiedClasses::containsKey).map(qualifiedClasses::get).collect(Collectors.toSet());
            int size = registeredQualifiedClasses.size();
            if (size == 1) {
                clazz = registeredQualifiedClasses.iterator().next();
            } else if (size > 1) {
                throw new InjectException(String.format("multiple injectable class %s for injection parameter %s because of using multiple annotations %s", registeredQualifiedClasses.stream().map(Class::getSimpleName).collect(Collectors.joining(",")), parameter.getType().getSimpleName(), annotations.stream().map(annotation -> annotation.annotationType().getSimpleName()).collect(Collectors.joining(","))));
            }
        }
        return clazz;
    }
}
