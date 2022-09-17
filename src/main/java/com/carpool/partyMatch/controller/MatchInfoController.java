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


import com.carpool.partyMatch.domain.MatchStatus;
import com.carpool.partyMatch.domain.Party;
import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.controller.dto.MatchInfoDto;
import com.carpool.partyMatch.controller.dto.MatchProcessDto;
import com.carpool.partyMatch.controller.dto.PartyProcessDto;
import com.carpool.partyMatch.controller.dto.response.MatchInfoResponse;
import com.carpool.partyMatch.controller.dto.response.MatchProcessResponse;
import com.carpool.partyMatch.controller.dto.response.PartyProcessResponse;
import com.carpool.partyMatch.service.MatchInfoService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
@Slf4j
public class MatchInfoController {

  final private MatchInfoService matchInfoService;

  @Description("파티 매칭 정보 전체 조회")
  @GetMapping("/matchInfoes")
	public ResponseEntity<List<MatchInfoResponse>> getMatchInfoes(@RequestParam Long partyInfoId, @RequestParam String matchStatus) {
    log.info("***************** MatchInfoController : 파티 매칭 정보 전체 조회 Postmapping 호출 *****************");

    List<MatchInfo> matchInfoList = matchInfoService.findMatchInfoList(partyInfoId, matchStatus);

    List<MatchInfoResponse> response = matchInfoList.stream()
                .map(m -> new MatchInfoResponse(m.getPartyInfoId(), m.getUserId()))
                .collect(Collectors.toList());

    return ResponseEntity.ok(response);
	}


  @Description("개인 파티 매칭 정보 조회")
  @GetMapping("/matchInfo")
	public ResponseEntity<MatchProcessResponse> getMatchInfo(@RequestParam Long partyInfoId, @RequestParam String userId) {
    log.info("***************** MatchInfoController : 개인 파티 매칭 정보 조회 Postmapping 호출 *****************");

    MatchProcessResponse response = matchInfoService.findMatchInfo(partyInfoId, userId);

    return ResponseEntity.ok(response);
	}


  @Description("파티 신청")
  @PostMapping("/apply")
	public ResponseEntity<MatchInfoResponse> applyParty(@RequestBody MatchInfoDto matchInfoDto) {
    log.info("***************** MatchInfoController : 파티 신청 Postmapping 호출 *****************");

    MatchInfo matchInfo = matchInfoService.registerMatchInfo(matchInfoDto);
    MatchInfoResponse response = new MatchInfoResponse(matchInfo.getPartyInfoId(), matchInfo.getUserId());

    return ResponseEntity.ok(response);
	}

  @Description("파티 신청 취소")
  @PostMapping("/cancel")
	public ResponseEntity cancelPartyApplication(@RequestBody MatchInfoDto matchInfoDto) {
    log.info("***************** MatchInfoController : 파티 신청 취소 Postmapping 호출 *****************");

    matchInfoService.cancelMatchInfo(matchInfoDto);
    return ResponseEntity.noContent().build();
	}

  @Description("파티 신청 수락")
  @PostMapping("/accept")
	public ResponseEntity<MatchProcessResponse> acceptPartyApplication(@RequestBody MatchProcessDto matchProcessDto) {
    log.info("***************** MatchInfoController : 파티 신청 수락 Postmapping 호출 *****************");

    MatchInfo matchInfo = matchInfoService.acceptMatchInfo(matchProcessDto);
    MatchProcessResponse response = new MatchProcessResponse(matchInfo.getPartyInfoId(), matchInfo.getUserId(), matchInfo.getMatchStatus());

    return ResponseEntity.ok(response);
	}

  @Description("파티 신청 거절")
  @PostMapping("/deny")
	public ResponseEntity<MatchProcessResponse> denyPartyApplication(@RequestBody MatchProcessDto matchProcessDto) {
    log.info("***************** MatchInfoController : 파티 신청 거절 Postmapping 호출 *****************");

    MatchInfo matchInfo = matchInfoService.denyMatchInfo(matchProcessDto);
    MatchProcessResponse response = new MatchProcessResponse(matchInfo.getPartyInfoId(), matchInfo.getUserId(), matchInfo.getMatchStatus());

    return ResponseEntity.ok(response);
	}

  @Description("파티 시작")
  @PostMapping("/partyStart")
	public ResponseEntity<PartyProcessResponse> startParty(@RequestBody PartyProcessDto partyProcessDto) {
    log.info("***************** MatchInfoController : 파티 시작 Postmapping 호출 *****************");

    PartyProcessResponse response = matchInfoService.startParty(partyProcessDto);

    return ResponseEntity.ok(response);
	}

  @Description("파티 종료")
  @PostMapping("/partyClose")
	public ResponseEntity<PartyProcessResponse> closeParty(@RequestBody PartyProcessDto partyProcessDto) {
    log.info("***************** MatchInfoController : 파티 종료 Postmapping 호출 *****************");

    PartyProcessResponse response = matchInfoService.closeParty(partyProcessDto);

    return ResponseEntity.ok(response);
	}

}
