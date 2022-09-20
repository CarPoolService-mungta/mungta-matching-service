package com.carpool.partyMatch.controller.dto.response;

import com.carpool.partyMatch.client.dto.ReviewResponse;
import com.carpool.partyMatch.domain.MatchStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MatchPartyMemberWithMatchStatusResponse {
    private Map<MatchStatus, List<MatchPartyMemberResponse>> matchStatusMembers;

    public void addMatchStatusMembers(MatchStatus matchStatus,MatchPartyMemberResponse matchPartyMemberResponse){
        if(matchStatusMembers == null){
            matchStatusMembers = new HashMap<>();
        }
        if(matchStatusMembers.get(matchStatus) == null){
            List<MatchPartyMemberResponse> matchPartyMemberResponses = new ArrayList<>();
            matchStatusMembers.put(matchStatus,matchPartyMemberResponses);
        }
        matchStatusMembers.get(matchStatus).add(matchPartyMemberResponse);
    }
}
