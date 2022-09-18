package com.carpool.partyMatch.controller.dto;

import lombok.Data;

import com.carpool.partyMatch.domain.MatchStatus;

@Data
public class MatchInfoDto {

  private Long partyInfoId;
  private String userId;

  public MatchInfoDto(Long partyInfoId, String userId) {

    this.partyInfoId = partyInfoId;
    this.userId = userId;

  }

}
