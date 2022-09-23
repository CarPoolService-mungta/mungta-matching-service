package com.carpool.partyMatch.domain.kafka;

import com.carpool.partyMatch.AbstractEvent;
import com.carpool.partyMatch.domain.PartyStatus;
import com.carpool.partyMatch.domain.Driver;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class PartyClosed extends AbstractEvent{

  Long id;
  Long partyInfoId;

  Driver driver;

  PartyStatus partyStatus;
}
