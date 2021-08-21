package com.thoughtworks.InjectContainer.models;

import com.thoughtworks.InjectContainer.annotation.*;

public class Car {
    private Seat driverSeat;
    private Seat passengerSeat;

    @Inject
    public Car(@Named("driver") Seat driverSeat, @Named("passenger") Seat passengerSeat) {
        this.driverSeat = driverSeat;
        this.passengerSeat = passengerSeat;
    }

    public String getDriverSeatType() { return driverSeat.getType(); }

    public String getPassengerSeatType() { return passengerSeat.getType(); }
}

