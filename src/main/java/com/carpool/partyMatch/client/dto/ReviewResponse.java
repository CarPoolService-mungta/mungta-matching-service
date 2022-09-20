package com.carpool.partyMatch.client.dto;

import lombok.Data;

@Data
public class ReviewResponse {

  private String userId;
  private String comment;
  private String scoreAvg;

  public String getUserId() {
    return userId;
  }

}
