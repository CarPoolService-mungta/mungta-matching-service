package com.carpool.partyMatch.controller.dto;

import lombok.Data;

@Data
public class MatchProcessDto {

  private Long partyInfoId;
  private String driverId;
  private String driverName;
  private String userId;

  public MatchProcessDto(Long partyInfoId, String driverId, String driverName, String userId) {

    this.partyInfoId = partyInfoId;
    this.driverId = driverId;
    this.driverName = driverName;
    this.userId = userId;

  }

}
