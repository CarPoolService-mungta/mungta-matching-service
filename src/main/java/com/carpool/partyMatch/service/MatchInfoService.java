package com.carpool.partyMatch.service;

import java.util.List;

import com.carpool.partyMatch.domain.kafka.MatchAccepted;
import com.carpool.partyMatch.domain.kafka.MatchCancelled;
import com.carpool.partyMatch.domain.kafka.PartyRegistered;
import com.carpool.partyMatch.domain.kafka.PartyStatusChanged;
import org.springframework.data.repository.query.Param;

import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.controller.dto.MatchInfoDto;
import com.carpool.partyMatch.controller.dto.MatchProcessDto;
import com.carpool.partyMatch.controller.dto.PartyProcessDto;
import com.carpool.partyMatch.controller.dto.response.PartyProcessResponse;
import com.carpool.partyMatch.controller.dto.response.MatchProcessResponse;

public interface MatchInfoService {

  public List<MatchInfo> findMatchInfoList(Long partyInfoId, String matchStatus);

  public MatchInfo registerMatchInfo(MatchInfoDto matchInfoDto);

  public void cancelMatchInfo(MatchInfoDto matchInfoDto);

  public MatchInfo acceptMatchInfo(MatchProcessDto matchProcessDto);

  public MatchInfo denyMatchInfo(MatchProcessDto matchProcessDto);

  public PartyProcessResponse startParty(PartyProcessDto partyProcessDto);

  public PartyProcessResponse closeParty(PartyProcessDto partyProcessDto);
  public PartyProcessResponse cancelParty(PartyProcessDto partyProcessDto);
  public void partyRegistered(PartyRegistered partyRegistered);
  public void partyStatusRejectRollback(PartyStatusChanged partyStatusChanged);
  public MatchInfo acceptMatchInfoRollback(MatchAccepted matchAccepted);
  public void cancelMatchInfoRollback(MatchCancelled matchCancelled);
}
