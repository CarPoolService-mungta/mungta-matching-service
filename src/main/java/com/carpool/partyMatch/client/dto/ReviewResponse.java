package com.carpool.partyMatch.client.dto;

import lombok.Data;

@Data
public class ReviewResponse {

  String userId;
  String content;
  String reviewScore;

  public String getUserId() {
    return userId;
  }

}
