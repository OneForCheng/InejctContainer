package com.thoughtworks.InjectContainer.models;

import com.thoughtworks.InjectContainer.annotation.*;

public class Bongo {
    @Inject
    public Bongo(@Named("none") Noot noot) {}
}
