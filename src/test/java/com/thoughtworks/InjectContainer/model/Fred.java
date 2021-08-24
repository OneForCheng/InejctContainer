package com.thoughtworks.InjectContainer.model;

import com.thoughtworks.InjectContainer.annotation.Inject;

public class Fred {
    @Inject
    public Fred(Qux qux) {
    }
}
