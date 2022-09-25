package com.carpool.partyMatch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Column(name = "driver_id")
    String driverId;
    @Column(name = "driver_name")
    String driverName;




}
