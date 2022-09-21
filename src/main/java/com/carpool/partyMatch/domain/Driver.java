package com.carpool.partyMatch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.Embeddable;


@Embeddable
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    String driverId;
    String driverName;




}
