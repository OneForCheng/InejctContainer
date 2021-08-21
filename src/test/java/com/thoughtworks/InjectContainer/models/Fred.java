package com.thoughtworks.InjectContainer.models;

import com.thoughtworks.InjectContainer.annotation.Inject;

public class Fred {
    @Inject
    public Fred(Qux qux) {
    }
}
