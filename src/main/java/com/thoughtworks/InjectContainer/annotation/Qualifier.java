package com.thoughtworks.InjectContainer.annotation;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Qualifier {
}
