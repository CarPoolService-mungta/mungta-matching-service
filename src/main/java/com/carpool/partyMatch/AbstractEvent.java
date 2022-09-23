package com.carpool.partyMatch;

import com.carpool.partyMatch.kafka.KafkaProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.MimeTypeUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class AbstractEvent {

    protected String eventType;
    protected String timestamp;

    public AbstractEvent(){
        this.setEventType(this.getClass().getSimpleName());
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
//
//    public String toJson(){
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = null;
//
//        try {
//            json = objectMapper.writeValueAsString(this);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("JSON format exception", e);
//        }
//
//        return json;
//    }
//
//    public void publish(String json){
//        if( json != null ){
//
//            /**
//             * spring streams 방식
//             */
//            KafkaProcessor processor = MatchApplication.applicationContext.getBean(KafkaProcessor.class);
//            MessageChannel outputChannel = processor.outboundTopic();
//
//            outputChannel.send(MessageBuilder
//                    .withPayload(json)
//                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
//                    .build());
//
//        }
//    }
//
//    public void publish(){
//        this.publish(this.toJson());
//    }
//
//    public void publishAfterCommit(){
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
//
//            @Override
//            public void afterCompletion(int status) {
//                AbstractEvent.this.publish();
//            }
//        });
//    }

    public boolean validate(){
        return getEventType().equals(getClass().getSimpleName());
    }
}
