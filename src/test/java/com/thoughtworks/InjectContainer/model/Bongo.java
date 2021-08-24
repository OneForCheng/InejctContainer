package com.thoughtworks.InjectContainer.model;

import com.thoughtworks.InjectContainer.annotation.*;

public class Bongo {
    @Inject
    public Bongo(@Named("none") Noot noot) {}
}
