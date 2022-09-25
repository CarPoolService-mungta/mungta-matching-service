package com.carpool.partyMatch.service;

import java.util.List;

import com.carpool.partyMatch.client.dto.UserResponse;
import com.carpool.partyMatch.controller.dto.response.MatchPartyMemberWithMatchStatusResponse;
import com.carpool.partyMatch.controller.dto.response.MatchStatusAndMemberListResponse;
import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.domain.MatchStatus;

public interface MatchPartyMemberService {

  public MatchPartyMemberWithMatchStatusResponse findMatchPartyMembers(Long partyInfoId);

  MatchStatusAndMemberListResponse findPartyMembersListSummaryAndMatchStatus(Long partyInfoId, String userId);

  List<UserResponse> findPartyMembersListSummary(Long partyInfoId);

  List<MatchInfo> findWaitAndAcceptMembersByPartyInfoId(Long partyInfoId);
  List<Long> findWaitingPartyList(String userId);
}

