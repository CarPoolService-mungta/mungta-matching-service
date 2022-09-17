package com.carpool.partyMatch.service.serviceImpl;

import java.util.List;
import java.lang.RuntimeException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import com.carpool.partyMatch.controller.dto.MatchInfoDto;
import com.carpool.partyMatch.controller.dto.MatchProcessDto;
import com.carpool.partyMatch.controller.dto.PartyProcessDto;
import com.carpool.partyMatch.controller.dto.response.PartyProcessResponse;
import com.carpool.partyMatch.controller.dto.response.MatchProcessResponse;
import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.domain.Party;
import com.carpool.partyMatch.domain.Carpooler;
import com.carpool.partyMatch.domain.Driver;
import com.carpool.partyMatch.domain.MatchStatus;
import com.carpool.partyMatch.domain.PartyStatus;
import com.carpool.partyMatch.domain.kafka.MatchAccepted;
import com.carpool.partyMatch.domain.kafka.MatchCancelled;
import com.carpool.partyMatch.domain.kafka.PartyStarted;
import com.carpool.partyMatch.domain.kafka.PartyClosed;
import com.carpool.partyMatch.repository.MatchInfoRepository;
import com.carpool.partyMatch.repository.PartyRepository;
import com.carpool.partyMatch.service.MatchInfoService;
import com.carpool.partyMatch.exception.ApiException;
import com.carpool.partyMatch.exception.ApiStatus;


import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MatchInfoServiceImpl implements MatchInfoService {

  private final MatchInfoRepository matchInfoRepository;
  private final PartyRepository partyRepository;

    @Override
    public List<MatchInfo> findMatchInfoList(Long partyInfoId, String matchStatus){
        log.info("********* findMatchUsers Service *********");

        MatchStatus ms = MatchStatus.valueOf(matchStatus);

        List<MatchInfo> matchInfoList = matchInfoRepository.findByPartyInfoIdAndMatchStatus(partyInfoId, ms);

        return matchInfoList;
    }

    @Override
    public MatchProcessResponse findMatchInfo(Long partyInfoId, String userId){
        log.info("********* findMatchInfo Service *********");

        MatchInfo matchInfo = matchInfoRepository.findByPartyInfoIdAndUserId(partyInfoId, userId);
        Party party = partyRepository.findByPartyInfoId(partyInfoId);

        MatchProcessResponse matchProcessResponse = new MatchProcessResponse(partyInfoId, userId, MatchStatus.AVAILABLE);
        Driver driver = party.getDriver();

        if(matchInfo != null){
            matchProcessResponse.setMatchStatus(matchInfo.getMatchStatus());
        }

        if(driver.getDriverId().equals(userId)){

            if(party.getPartyStatus() == PartyStatus.STARTED){
                matchProcessResponse.setMatchStatus(MatchStatus.START);
            }
            else{
                matchProcessResponse.setMatchStatus(MatchStatus.FORMED);
            }
        }

        return matchProcessResponse;
    }

    @Override
    @Transactional
    public MatchInfo registerMatchInfo(MatchInfoDto matchInfoDto){
        log.info("********* registerMatchInfo *********");
        log.debug(String.valueOf(matchInfoDto));

        validateApplyParty(matchInfoDto.getPartyInfoId(), matchInfoDto.getUserId());

        MatchInfo matchInfo = new MatchInfo();
        matchInfo.setPartyInfoId(matchInfoDto.getPartyInfoId());
        matchInfo.setUserId(matchInfoDto.getUserId());
        matchInfo.setMatchStatus(MatchStatus.WAITING);
        matchInfoRepository.save(matchInfo);

        return matchInfo;
    }

    @Override
    @Transactional
    public void cancelMatchInfo(MatchInfoDto matchInfoDto){
        log.info("********* cancelMatchInfo *********");
        log.debug(String.valueOf(matchInfoDto));

        Party party = partyRepository.findByPartyInfoId(matchInfoDto.getPartyInfoId());
        validateCancelParty(party);
        //파티 상태 확인
        MatchInfo matchInfo = matchInfoRepository.findByPartyInfoIdAndUserId(matchInfoDto.getPartyInfoId(), matchInfoDto.getUserId());

        boolean isAccepted = false;
        if(matchInfo.getMatchStatus() == MatchStatus.ACCEPT) isAccepted = true;
        matchInfo.setMatchStatus(MatchStatus.CANCEL);

        //매칭 취소 이벤트 발행
        if(isAccepted){
            party.removePartyNumber();
            MatchCancelled matchCancelled = new MatchCancelled();
            BeanUtils.copyProperties(matchInfo, matchCancelled);
            matchCancelled.publish();
        }

        matchInfoRepository.deleteByPartyInfoIdAndUserId(matchInfoDto.getPartyInfoId(), matchInfoDto.getUserId());
    }

    @Override
    @Transactional
    public MatchInfo acceptMatchInfo(MatchProcessDto matchProcessDto){
        log.info("********* acceptMatchInfo *********");
        log.debug(String.valueOf(matchProcessDto));

        Party party = partyRepository.findByPartyInfoId(matchProcessDto.getPartyInfoId());
        validateDriver(party, matchProcessDto.getDriverId());

        MatchInfo matchInfo =  matchInfoRepository.findByPartyInfoIdAndUserId(matchProcessDto.getPartyInfoId(), matchProcessDto.getUserId());
        validateProcess(matchInfo);

        matchInfo.setMatchStatus(MatchStatus.ACCEPT);
        party.addPartyNumber();

        //매칭 수락 이벤트 발행
        MatchAccepted matchAccepted = new MatchAccepted();
        BeanUtils.copyProperties(matchInfo, matchAccepted);
        matchAccepted.publish();

        return matchInfo;
    }

    //파티관리 서비스에서 신청 가능 인원 확인 후 신청 불가할 경우에도 아래 실행
    @Override
    @Transactional
    public MatchInfo denyMatchInfo(MatchProcessDto matchProcessDto){
        log.info("********* denyMatchInfo *********");
        log.debug(String.valueOf(matchProcessDto));

        Party party = partyRepository.findByPartyInfoId(matchProcessDto.getPartyInfoId());
        validateDriver(party, matchProcessDto.getDriverId());

        MatchInfo matchInfo = matchInfoRepository.findByPartyInfoIdAndUserId(matchProcessDto.getPartyInfoId(), matchProcessDto.getUserId());
        validateProcess(matchInfo);

        matchInfo.setMatchStatus(MatchStatus.DENY);

        return matchInfo;
    }

    private void validateApplyParty(Long partyInfoId, String userId) {

        //파티 상태 확인 (시작 또는 종료이면 신청 불가)
        Party party = partyRepository.findByPartyInfoId(partyInfoId);

        if(party.getPartyStatus() != PartyStatus.AVAILABLE && party.getPartyStatus() != PartyStatus.FULL){

            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);

        }

        MatchInfo matchInfo =  matchInfoRepository.findByPartyInfoIdAndUserId(partyInfoId, userId);
        if (matchInfo != null) {
            throw new ApiException(ApiStatus.INVALID_MODIFY_MATCH);
        }

        List<MatchInfo> matchWaitList = matchInfoRepository.findByUserIdAndMatchStatus(userId, MatchStatus.WAITING);
        List<MatchInfo> matchAcceptList = matchInfoRepository.findByUserIdAndMatchStatus(userId, MatchStatus.ACCEPT);
        if (!matchWaitList.isEmpty() || !matchAcceptList.isEmpty()) {
            throw new ApiException(ApiStatus.INVALID_MODIFY_MATCH);
        }

    }

    private void validateCancelParty(Party party) {

        if(party.getPartyStatus() != PartyStatus.AVAILABLE && party.getPartyStatus() != PartyStatus.FULL){

            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);

        }

    }

    private void validateDriver(Party party, String driverId) {

        if(!party.isDriver(driverId)){

            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);

        }
    }

    private void validateProcess(MatchInfo matchInfo) {

        if(matchInfo.getMatchStatus() != MatchStatus.WAITING){

            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);

        }
    }



    @Override
    @Transactional
    public PartyProcessResponse startParty(PartyProcessDto partyProcessDto){
        log.info("********* startParty *********");
        log.debug(String.valueOf(partyProcessDto));

        Party party = partyRepository.findByPartyInfoId(partyProcessDto.getPartyInfoId());

        validateDriver(party, partyProcessDto.getDriverId());
        party.setPartyStatus(PartyStatus.STARTED);

        //파티 시작 이벤트 발행
        PartyStarted partyStarted = new PartyStarted();
        BeanUtils.copyProperties(party, partyStarted);
        partyStarted.publish();

        PartyProcessResponse response = new PartyProcessResponse(party.getPartyInfoId(), party.getPartyStatus());

        return response;
    }


    @Override
    @Transactional
    public PartyProcessResponse closeParty(PartyProcessDto partyProcessDto){
        log.info("********* closeParty *********");
        log.debug(String.valueOf(partyProcessDto));

        Party party = partyRepository.findByPartyInfoId(partyProcessDto.getPartyInfoId());

        validateDriver(party, partyProcessDto.getDriverId());
        party.setPartyStatus(PartyStatus.CLOSED);

        //파티 종료 이벤트 발행
        PartyClosed partyClosed = new PartyClosed();
        BeanUtils.copyProperties(party, partyClosed);
        partyClosed.publish();

        PartyProcessResponse response = new PartyProcessResponse(party.getPartyInfoId(), party.getPartyStatus());

        return response;
    }

}
