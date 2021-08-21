package com.thoughtworks.InjectContainer.models;

import com.thoughtworks.InjectContainer.Inject;

public class Blue {
    @Inject
    public Blue(Red red) {
    }
}
