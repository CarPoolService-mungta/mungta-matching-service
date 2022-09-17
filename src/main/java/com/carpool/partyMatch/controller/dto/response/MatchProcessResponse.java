package com.carpool.partyMatch.controller.dto.response;

import lombok.Data;
import java.util.List;

import com.carpool.partyMatch.domain.MatchStatus;

@Data
public class MatchProcessResponse {

  private Long partyInfoId;
  private String userId;
  private MatchStatus matchStatus;

  public MatchProcessResponse(Long partyInfoId, String userId, MatchStatus matchStatus) {

    this.partyInfoId = partyInfoId;
    this.userId = userId;
    this.matchStatus = matchStatus;

  }

}
