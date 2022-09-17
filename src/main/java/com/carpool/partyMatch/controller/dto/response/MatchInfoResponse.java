package com.carpool.partyMatch.controller.dto.response;

import lombok.Data;
import java.util.List;

import com.carpool.partyMatch.domain.MatchStatus;

@Data
public class MatchInfoResponse {
  private Long partyInfoId;
  private String userId;

  public MatchInfoResponse(Long partyInfoId, String userId) {

    this.partyInfoId = partyInfoId;
    this.userId = userId;

  }
}
