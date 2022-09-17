package com.carpool.partyMatch;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.carpool.partyMatch.domain.Party;
import com.carpool.partyMatch.domain.PartyStatus;
import com.carpool.partyMatch.domain.Driver;
import com.carpool.partyMatch.domain.kafka.PartyRegistered;
import com.carpool.partyMatch.repository.PartyRepository;
import com.carpool.partyMatch.kafka.KafkaProcessor;

@Service
public class PolicyHandler {
    @Autowired
    PartyRepository partyRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPartyRegistered_registerParty(@Payload PartyRegistered partyRegistered){
        if(!partyRegistered.validate())
            return;

        Party party = new Party();

        party.setPartyInfoId(partyRegistered.getId());
        party.setDriver(new Driver(partyRegistered.getDriverId(), partyRegistered.getDriverName()));
        party.setCurNumberOfParty(partyRegistered.getCurNumberOfParty());
        party.setMaxNumberOfParty(partyRegistered.getMaxNumberOfParty());
        party.setPartyStatus(PartyStatus.AVAILABLE);

        partyRepository.save(party);
    }

}
