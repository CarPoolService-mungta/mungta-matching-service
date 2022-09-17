package com.carpool.partyMatch.controller.dto;

import lombok.Data;

@Data
public class MatchProcessDto {

  private Long partyInfoId;
  private String driverId;
  private String driverName;
  private String userId;

}
