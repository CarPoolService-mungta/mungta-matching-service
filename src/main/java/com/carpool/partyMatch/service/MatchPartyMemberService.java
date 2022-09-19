package com.carpool.partyMatch.service;

import java.util.List;

import com.carpool.partyMatch.client.dto.UserResponse;
import com.carpool.partyMatch.controller.dto.response.MatchStatusAndMemberListResponse;
import org.springframework.data.repository.query.Param;

import com.carpool.partyMatch.controller.dto.response.MatchPartyMemberResponse;

public interface MatchPartyMemberService {

  public List<MatchPartyMemberResponse> findMatchPartyMembers(Long partyInfoId, String matchStatus);

  MatchStatusAndMemberListResponse findPartyMembersListSummary(Long partyInfoId, String userId);
}

