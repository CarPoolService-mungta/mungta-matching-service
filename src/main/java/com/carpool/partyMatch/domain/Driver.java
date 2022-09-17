package com.carpool.partyMatch.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;


@Embeddable
public class Driver {

    String driverId;
    String driverName;

    protected Driver(){}
    public Driver(String driverId, String driverName){
        this.driverId = driverId;
        this.driverName = driverName;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverId() {
        return driverId;
    }


}
