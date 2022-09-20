package com.carpool.partyMatch.controller.dto.response;

import com.carpool.partyMatch.client.dto.ReviewResponse;
import com.carpool.partyMatch.client.dto.UserResponse;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchPartyMemberResponse {

    private String userId;
    private String userName;
    private String gender;
    private String userTeamName;
    private byte[] userPhoto;
    private String fileExtension;
    private String comment;
    private String scoreAvg;

    public static MatchPartyMemberResponse of(UserResponse userResponse, ReviewResponse reviewResponse){
        return MatchPartyMemberResponse.builder()
                .userId(userResponse.getUserId())
                .userName(userResponse.getUserName())
                .gender(userResponse.getGender())
                .userTeamName(userResponse.getUserTeamName())
                .userPhoto(userResponse.getUserPhoto())
                .fileExtension(userResponse.getFileExtension())
                .comment(Objects.isNull(reviewResponse)? null : reviewResponse.getComment())
                .scoreAvg(Objects.isNull(reviewResponse)? null : reviewResponse.getScoreAvg())
                .build();
    }
}
