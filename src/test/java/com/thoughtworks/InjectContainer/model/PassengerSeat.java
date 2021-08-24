package com.thoughtworks.InjectContainer.model;

import com.thoughtworks.InjectContainer.annotation.Named;

@Named("passenger")
public class PassengerSeat extends Seat {
    @Override
    public String getType() {
        return "passengerSeat";
    }
}
