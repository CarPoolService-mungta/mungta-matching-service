package com.carpool.partyMatch.controller.dto;

import lombok.Data;

import com.carpool.partyMatch.domain.MatchStatus;
import com.carpool.partyMatch.client.dto.UserResponse;
import com.carpool.partyMatch.client.dto.ReviewResponse;

@Data
public class MatchPartyMemberDto {

  UserResponse userResponse;
  ReviewResponse reviewResponse;

  public MatchPartyMemberDto(UserResponse userResponse, ReviewResponse reviewResponse) {

    this.userResponse = userResponse;
    this.reviewResponse = reviewResponse;

  }
}
