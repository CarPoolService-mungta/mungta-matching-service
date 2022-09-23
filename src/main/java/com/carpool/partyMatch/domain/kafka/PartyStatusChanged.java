package com.carpool.partyMatch.domain.kafka;

import com.carpool.partyMatch.AbstractEvent;
import com.carpool.partyMatch.domain.PartyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartyStatusChanged extends AbstractEvent{

  private Long partyInfoId;
  private PartyStatus partyStatus;
  private PartyStatus pastPartyStatus;

}
