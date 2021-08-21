package com.thoughtworks.InjectContainer;

import java.lang.annotation.*;

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Singleton {
}
