package com.carpool.partyMatch.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface KafkaProcessor {

    String PARTY_REGISTERED = "partyRegistered-in-0";
    String PARTY_STATUS_CHANGED_REJECT="partyStatusChangedReject-in-0";
    String PARTY_MEMBER_ACCEPT_REJECT="partyMemberAcceptReject-in-0";
    String PARTY_MEMBER_CANCEL_REJECT="partyMemberCanceledReject-in-0";
//    String OUTPUT = "event-out";

    @Input(PARTY_REGISTERED)
    SubscribableChannel partyRegisteredTopic();

    @Input(PARTY_STATUS_CHANGED_REJECT)
    SubscribableChannel partyStatusChangedRejectTopic();

    @Input(PARTY_MEMBER_ACCEPT_REJECT)
    SubscribableChannel partyMemberAcceptRejectTopic();

    @Input(PARTY_MEMBER_CANCEL_REJECT)
    SubscribableChannel partyMemberCancelRejectTopic();


}
