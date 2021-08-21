package com.thoughtworks.InjectContainer.models;

import com.thoughtworks.InjectContainer.Inject;

public class Fred {
    @Inject
    public Fred(Qux qux) {
    }
}
