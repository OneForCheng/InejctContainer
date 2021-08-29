package com.thoughtworks.InjectContainer.model;

import com.thoughtworks.InjectContainer.annotation.Inject;

public class Zoo {
    private Animal[] animals;

    @Inject
    public Zoo(Animal[] animals) {
        this.animals = animals;
    }

    public int getAnimalTotalCount() { return animals.length; }
}
