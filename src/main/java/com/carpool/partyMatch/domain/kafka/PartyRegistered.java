package com.carpool.partyMatch.domain.kafka;

import java.util.StringJoiner;

import com.carpool.partyMatch.AbstractEvent;
import com.carpool.partyMatch.domain.PartyStatus;
import com.carpool.partyMatch.domain.Driver;

public class PartyRegistered extends AbstractEvent {

  Long id;
  String driverId;
  String driverName;
  int curNumberOfParty;
  int maxNumberOfParty;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDriverId() {
    return driverId;
  }

  public void setDriverId(String driverId) {
    this.driverId = driverId;
  }

  public int getCurNumberOfParty() {
    return curNumberOfParty;
  }

  public void setCurNumberOfParty(int curNumberOfParty) {
    this.curNumberOfParty = curNumberOfParty;
  }

  public int getMaxNumberOfParty() {
    return maxNumberOfParty;
  }

  public void setMaxNumberOfParty(int maxNumberOfParty) {
    this.maxNumberOfParty = maxNumberOfParty;
  }

  public String getDriverName() {
    return driverName;
  }

  public void setDriverName(String driverName) {
    this.driverName = driverName;
  }

}
