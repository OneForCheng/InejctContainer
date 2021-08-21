package com.thoughtworks.InjectContainer.models;

import com.thoughtworks.InjectContainer.annotation.Inject;

public class Blue {
    @Inject
    public Blue(Red red) {
    }
}
