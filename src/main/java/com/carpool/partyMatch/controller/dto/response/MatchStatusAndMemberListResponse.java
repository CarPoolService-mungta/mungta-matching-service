package com.carpool.partyMatch.controller.dto.response;

import com.carpool.partyMatch.client.dto.UserResponse;
import com.carpool.partyMatch.domain.MatchStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchStatusAndMemberListResponse {
    private List<UserResponse> userResponses;
    private MatchStatus matchStatus;

    public static MatchStatusAndMemberListResponse of(List<UserResponse> userResponses, MatchStatus matchStatus){
        return MatchStatusAndMemberListResponse.builder()
                .userResponses(userResponses)
                .matchStatus(matchStatus)
                .build();
    }
}
