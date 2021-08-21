package com.thoughtworks.InjectContainer.models;

import com.thoughtworks.InjectContainer.Inject;

public class Thud {
    public Thud() {}

    @Inject
    public Thud(Qux qux) {
    }
}
