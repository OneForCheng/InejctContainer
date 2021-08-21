package com.thoughtworks.InjectContainer.models;

import com.thoughtworks.InjectContainer.annotation.Named;

@Named("driver")
public class DriverSeat extends Seat {
    @Override
    public String getType() {
        return "driverSeat";
    }
}
