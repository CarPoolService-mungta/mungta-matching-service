package com.carpool.partyMatch.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.carpool.partyMatch.controller.dto.response.MatchPartyMemberResponse;
import com.carpool.partyMatch.controller.dto.response.MatchPartyMemberWithMatchStatusResponse;
import com.carpool.partyMatch.controller.dto.response.MatchStatusAndMemberListResponse;
import com.carpool.partyMatch.domain.PartyStatus;
import com.carpool.partyMatch.repository.MatchInfoRepositorySupport;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.domain.MatchStatus;
import com.carpool.partyMatch.service.MatchPartyMemberService;
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
  private final MatchInfoRepositorySupport matchInfoRepositorySupport;

  @Override
  @Deprecated
  public MatchPartyMemberWithMatchStatusResponse findMatchPartyMembers(Long partyInfoId) {
    log.info("********* findMatchPartyMembers Service *********");

    List<MatchStatus> matchStatusList = new ArrayList<>(){
      {
        add(MatchStatus.WAITING);
        add(MatchStatus.FORMED);
        add(MatchStatus.ACCEPT);
      }
    } ;
    List<MatchInfo> matchInfoList = matchInfoRepository.findByPartyInfoIdAndMatchStatusIsIn(partyInfoId, matchStatusList);

    List<String> userIds = matchInfoList.stream()
        .map(m -> m.getUserId())
        .collect(Collectors.toList());

    List<UserResponse> userList = userServiceClient.getUserList(userIds);
    List<ReviewResponse> reviewList = reviewServiceClient.getReviewList(userIds);

    MatchPartyMemberWithMatchStatusResponse matchPartyMemberWithMatchStatusResponse = new MatchPartyMemberWithMatchStatusResponse();

    matchInfoList.stream().forEach(o->{
      matchPartyMemberWithMatchStatusResponse.addMatchStatusMembers(o.getMatchStatus(),
              MatchPartyMemberResponse.of(userList.stream().filter(u->u.getUserId().equals(o.getUserId())).findFirst().orElse(null),
                      reviewList.stream().filter(r->r.getUserId().equals(o.getUserId())).findFirst().orElse(null)));
    });

    return matchPartyMemberWithMatchStatusResponse;
  }

  @Override
  public MatchStatusAndMemberListResponse findPartyMembersListSummaryAndMatchStatus(Long partyInfoId, String userId){
    List<MatchStatus> partyMemberStatusCondition = new ArrayList<>(){
      {
        add(MatchStatus.ACCEPT);
        add(MatchStatus.START);
        add(MatchStatus.FORMED);
        add(MatchStatus.CLOSE);
      }
    } ;
    List<MatchInfo> matchInfoList = matchInfoRepository.findByPartyInfoIdAndMatchStatusIsIn(partyInfoId, partyMemberStatusCondition);
    List<MatchInfo> waitingList = matchInfoRepository.findByPartyInfoIdAndMatchStatus(partyInfoId, MatchStatus.WAITING);
    MatchInfo userMatchInfo= matchInfoRepository.findByPartyInfoIdAndUserId(partyInfoId, userId).orElse(null);

    return MatchStatusAndMemberListResponse.of(userServiceClient.getUserList(matchInfoList.stream()
            .map(o->o.getUserId())
            .collect(Collectors.toList())),
            Objects.isNull(userMatchInfo) ? null : userMatchInfo.getMatchStatus(),
            waitingList.size());
  }


  //Review에서 사용
  @Override
  public List<UserResponse> findPartyMembersListSummary(Long partyInfoId){
    List<MatchStatus> partyMemberStatusCondition = new ArrayList<>(){
      {
        add(MatchStatus.ACCEPT);
        add(MatchStatus.FORMED);
        add(MatchStatus.CLOSE);
      }
    } ;
    List<MatchInfo> matchInfoList = matchInfoRepository.findByPartyInfoIdAndMatchStatusIsIn(partyInfoId, partyMemberStatusCondition);

    return userServiceClient.getUserList(matchInfoList.stream()
            .map(o->o.getUserId())
            .collect(Collectors.toList()));
  }

  @Override
  public List<MatchInfo> findWaitAndAcceptMembersByPartyInfoId(Long partyInfoId){
    List<MatchStatus> partyMemberStatusCondition = new ArrayList<>(){
      {
        add(MatchStatus.WAITING);
        add(MatchStatus.FORMED);
        add(MatchStatus.ACCEPT);
      }
    };
    return matchInfoRepository.findByPartyInfoIdAndMatchStatusIsIn(partyInfoId, partyMemberStatusCondition);
  }

  @Override
  public List<Long> findWaitingPartyList(String userId){

    List<PartyStatus> partyStatuses = new ArrayList<>(){
      {
        add(PartyStatus.OPEN);
        add(PartyStatus.FULL);
      }
    };

    return matchInfoRepositorySupport.findPartyIdByUserIdAndMatchStatusAndPartyStatuses(userId, MatchStatus.WAITING, partyStatuses);
  }
}
