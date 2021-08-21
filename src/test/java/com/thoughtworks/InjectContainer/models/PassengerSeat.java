package com.thoughtworks.InjectContainer.models;

import com.thoughtworks.InjectContainer.annotation.Named;

@Named("passenger")
public class PassengerSeat extends Seat {
    @Override
    public String getType() {
        return "passengerSeat";
    }
}
