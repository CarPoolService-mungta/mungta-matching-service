package com.carpool.partyMatch.controller.dto.response;

import lombok.Data;

import com.carpool.partyMatch.domain.MatchStatus;
import com.carpool.partyMatch.client.dto.UserResponse;
import com.carpool.partyMatch.client.dto.ReviewResponse;

@Data
public class MatchPartyMemberResponse {

  String userId;
  String userName;
  String gender;
  String curPhoto;
  String partyRole;
  String userTeamName;
  String content;
  String reviewScore;

  public MatchPartyMemberResponse(UserResponse userResponse, ReviewResponse reviewResponse) {

    this.userId = userResponse.getUserId();
    this.userName = userResponse.getName();
    this.gender = userResponse.getGender();
    this.curPhoto = userResponse.getCurPhoto();
    this.partyRole = userResponse.getPartyRole();
    this.userTeamName = userResponse.getUserTeamName();
    this.content = reviewResponse.getContent();
    this.reviewScore = reviewResponse.getReviewScore();

  }
}
