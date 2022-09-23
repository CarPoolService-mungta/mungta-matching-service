package com.carpool.partyMatch.domain.kafka;

import com.carpool.partyMatch.AbstractEvent;
import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.domain.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MatchAccepted extends AbstractEvent{

    private Long partyInfoId;
    private String userId;
    private MatchStatus matchStatus;
    private MatchStatus pastMatchStatus;


    public static MatchAccepted of(MatchInfo matchInfo, MatchStatus pastMatchStatus){
        return MatchAccepted.builder()
                .partyInfoId(matchInfo.getPartyInfoId())
                .userId(matchInfo.getUserId())
                .matchStatus(matchInfo.getMatchStatus())
                .pastMatchStatus(pastMatchStatus)
                .build();
    }
}
