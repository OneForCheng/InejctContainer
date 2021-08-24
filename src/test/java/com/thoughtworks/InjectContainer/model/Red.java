package com.thoughtworks.InjectContainer.model;

import com.thoughtworks.InjectContainer.annotation.Inject;

public class Red {
    @Inject
    public Red(Blue blue) {
    }
}
