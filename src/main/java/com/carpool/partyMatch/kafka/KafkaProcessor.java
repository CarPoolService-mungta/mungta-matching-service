package com.carpool.partyMatch.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface KafkaProcessor {

    String PARTY_REGISTERED = "partyRegistered-in-0";
//    String OUTPUT = "event-out";

    @Input(PARTY_REGISTERED)
    SubscribableChannel inboundTopic();

//    @Output(OUTPUT)
//    MessageChannel outboundTopic();

}
