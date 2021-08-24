package com.thoughtworks.InjectContainer.model;

import com.thoughtworks.InjectContainer.annotation.Inject;

public class Jim {
    private Shme s1;
    private Shme s2;

    @Inject
    public Jim(Shme s1, Shme s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    public boolean isSame() {
        return s1 == s2;
    }
}
