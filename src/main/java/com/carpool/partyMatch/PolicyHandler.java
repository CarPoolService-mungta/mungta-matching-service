package com.carpool.partyMatch;

import com.carpool.partyMatch.service.MatchInfoService;
import com.carpool.partyMatch.service.MatchPartyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Payload;

import com.carpool.partyMatch.domain.Party;
import com.carpool.partyMatch.domain.PartyStatus;
import com.carpool.partyMatch.domain.Driver;
import com.carpool.partyMatch.domain.kafka.PartyRegistered;
import com.carpool.partyMatch.repository.PartyRepository;
import com.carpool.partyMatch.kafka.KafkaProcessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@RequiredArgsConstructor
@Component
public class PolicyHandler {

    private final MatchInfoService matchInfoService;

    @StreamListener(KafkaProcessor.PARTY_REGISTERED)
    public void wheneverPartyRegistered_registerParty(@Payload PartyRegistered partyRegistered){
        if(!partyRegistered.validate())
            return;
        matchInfoService.partyRegistered(partyRegistered);
    }

}
