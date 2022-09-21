package com.carpool.partyMatch.domain.kafka;

import java.util.StringJoiner;

import com.carpool.partyMatch.AbstractEvent;
import com.carpool.partyMatch.domain.PartyStatus;
import com.carpool.partyMatch.domain.Driver;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyRegistered extends AbstractEvent {

  private Long partyId;
  private String driverId;
  private String driverName;
  private int maxNumberOfParty;

}
