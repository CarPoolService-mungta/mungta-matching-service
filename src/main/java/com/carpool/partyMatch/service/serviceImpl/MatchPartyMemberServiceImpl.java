package com.carpool.partyMatch.service.serviceImpl;

import java.util.List;
import java.util.Map;
import java.lang.RuntimeException;
import java.util.stream.Collectors;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.domain.MatchStatus;
import com.carpool.partyMatch.service.MatchPartyMemberService;
import com.carpool.partyMatch.controller.dto.response.MatchPartyMemberResponse;
import com.carpool.partyMatch.controller.dto.MatchPartyMemberDto;
import com.carpool.partyMatch.client.ReviewServiceClient;
import com.carpool.partyMatch.client.UserServiceClient;
import com.carpool.partyMatch.client.dto.UserResponse;
import com.carpool.partyMatch.client.dto.ReviewResponse;
import com.carpool.partyMatch.repository.MatchInfoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchPartyMemberServiceImpl implements MatchPartyMemberService {

  private final MatchInfoRepository matchInfoRepository;

  private final ReviewServiceClient reviewServiceClient;
  private final UserServiceClient userServiceClient;

  @Override
  public List<MatchPartyMemberResponse> findMatchPartyMembers(Long partyInfoId, String matchStatus) {
    log.info("********* findMatchPartyMembers Service *********");

    MatchStatus ms = MatchStatus.valueOf(matchStatus);

    List<MatchInfo> matchInfoList = matchInfoRepository.findByPartyInfoIdAndMatchStatus(partyInfoId, ms);

    List<String> userIds = matchInfoList.stream()
        .map(m -> m.getUserId())
        .collect(Collectors.toList());

    List<UserResponse> userList = userServiceClient.getUserList(userIds);
    List<ReviewResponse> reviewList = reviewServiceClient.getReviewList(userIds);

    Map<String, ReviewResponse> reviewMap = reviewList.stream()
        .collect(Collectors.toMap(ReviewResponse::getUserId, Function.identity()));

    List<MatchPartyMemberResponse> matchPartyMemberList = userList.stream()
        .filter(it -> reviewMap.containsKey(it.getUserId()))
        .map(it -> new MatchPartyMemberResponse(it, reviewMap.get(it.getUserId())))
        .collect(Collectors.toList());


    // List<MatchInfo> matchInfoList = matchInfoRepository.findByPartyInfoIdAndMatchStatus(partyInfoId, ms);

    return matchPartyMemberList;
  }

}
