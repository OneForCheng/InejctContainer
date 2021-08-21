package com.thoughtworks.InjectContainer.annotation;

import java.lang.annotation.*;

@Target({ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inject {
}
