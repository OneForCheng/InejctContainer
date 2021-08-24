package com.thoughtworks.InjectContainer.model;

import com.thoughtworks.InjectContainer.annotation.Named;

@Named("driver")
public class DriverSeat extends Seat {
    @Override
    public String getType() {
        return "driverSeat";
    }
}
