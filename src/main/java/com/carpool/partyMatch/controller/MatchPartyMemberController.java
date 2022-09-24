package com.carpool.partyMatch.controller;

import java.util.List;

import com.carpool.partyMatch.client.dto.UserResponse;
import com.carpool.partyMatch.controller.dto.response.MatchPartyMemberWithMatchStatusResponse;
import com.carpool.partyMatch.controller.dto.response.MatchStatusAndMemberListResponse;
import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.domain.MatchStatus;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.carpool.partyMatch.service.MatchPartyMemberService;

@RestController
@RequestMapping("/api/match/partymembers")
@RequiredArgsConstructor
@Slf4j
public class MatchPartyMemberController {

  private final MatchPartyMemberService matchPartyMemberService;

  @Description("파티 매칭 정보 전체 조회")
  @GetMapping
  public ResponseEntity<MatchPartyMemberWithMatchStatusResponse> getMatchPartyMembers(@RequestParam long partyInfoId) {
    log.info("***************** MatchInfoController : 파티 매칭 정보 전체 조회 Postmapping 호출 *****************");
    return ResponseEntity.ok(matchPartyMemberService.findMatchPartyMembers(partyInfoId));
  }

  @Description("파티 매칭 멤버 및 유저 매칭 상태 조회")
  @GetMapping("/summary")
  public ResponseEntity<MatchStatusAndMemberListResponse> findPartyMembersListSummaryAndMatchStatus(@RequestParam long partyInfoId,
                                                                                            @RequestHeader("userId") String userId) {

    return ResponseEntity.ok(matchPartyMemberService.findPartyMembersListSummaryAndMatchStatus(partyInfoId, userId));
  }
  @Description("파티 매칭 멤버 조회")
  @GetMapping("/summary-for-review")
  public ResponseEntity<List<UserResponse>> findPartyMembersListSummary(@RequestParam long partyInfoId) {

    return ResponseEntity.ok(matchPartyMemberService.findPartyMembersListSummary(partyInfoId));
  }

  @Description("파티 매칭 멤버 조회")
  @GetMapping("/waiting-and-accpet")
  public ResponseEntity<List<MatchInfo>>  findWaitAndAcceptMembersByPartyInfoId(@RequestParam long partyInfoId) {

    return ResponseEntity.ok(matchPartyMemberService.findWaitAndAcceptMembersByPartyInfoId(partyInfoId));
  }
}
