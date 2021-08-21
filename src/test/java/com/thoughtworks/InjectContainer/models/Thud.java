package com.thoughtworks.InjectContainer.models;

import com.thoughtworks.InjectContainer.annotation.Inject;

public class Thud {
    public Thud() {}

    @Inject
    public Thud(Qux qux) {
    }
}
