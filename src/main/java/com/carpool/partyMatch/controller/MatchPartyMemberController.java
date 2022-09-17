package com.carpool.partyMatch.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.carpool.partyMatch.service.MatchPartyMemberService;
import com.carpool.partyMatch.controller.dto.response.MatchPartyMemberResponse;

@RestController
@RequestMapping("/api/match/partymembers")
@RequiredArgsConstructor
@Slf4j
public class MatchPartyMemberController {

  private final MatchPartyMemberService matchPartyMemberService;

  @Description("파티 매칭 정보 전체 조회")
  @GetMapping
  public ResponseEntity<List<MatchPartyMemberResponse>> getMatchPartyMembers(@RequestParam long partyInfoId, @RequestParam String matchStatus) {
    log.info("***************** MatchInfoController : 파티 매칭 정보 전체 조회 Postmapping 호출 *****************");

    List<MatchPartyMemberResponse> response = matchPartyMemberService.findMatchPartyMembers(partyInfoId, matchStatus);

    return ResponseEntity.ok(response);
  }
}
