package com.thoughtworks.InjectContainer.models;

import com.thoughtworks.InjectContainer.annotation.Inject;

public class Red {
    @Inject
    public Red(Blue blue) {
    }
}
