package com.thoughtworks.InjectContainer;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {
}
