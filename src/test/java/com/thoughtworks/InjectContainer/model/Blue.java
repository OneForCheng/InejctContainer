package com.thoughtworks.InjectContainer.model;

import com.thoughtworks.InjectContainer.annotation.Inject;

public class Blue {
    @Inject
    public Blue(Red red) {
    }
}
