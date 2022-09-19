package com.carpool.partyMatch.controller.dto.response;

import lombok.Data;

import com.carpool.partyMatch.domain.MatchStatus;
import com.carpool.partyMatch.client.dto.UserResponse;
import com.carpool.partyMatch.client.dto.ReviewResponse;

@Data
public class MatchPartyMemberResponse {

  private String userId;
  private String userName;
  private String gender;
  private String userTeamName;
  private String content;
  private String reviewScore;

  public MatchPartyMemberResponse(UserResponse userResponse, ReviewResponse reviewResponse) {

    this.userId = userResponse.getUserId();
    this.userName = userResponse.getUserName();
    this.gender = userResponse.getGender();
    this.userTeamName = userResponse.getUserTeamName();
    this.content = reviewResponse.getContent();
    this.reviewScore = reviewResponse.getReviewScore();

  }
}
