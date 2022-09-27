package com.carpool.partyMatch.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import com.carpool.partyMatch.PolicyHandler;
import com.carpool.partyMatch.domain.kafka.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import com.carpool.partyMatch.controller.dto.MatchInfoDto;
import com.carpool.partyMatch.controller.dto.MatchProcessDto;
import com.carpool.partyMatch.controller.dto.PartyProcessDto;
import com.carpool.partyMatch.controller.dto.response.PartyProcessResponse;
import com.carpool.partyMatch.controller.dto.response.MatchProcessResponse;
import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.domain.Party;
import com.carpool.partyMatch.domain.Driver;
import com.carpool.partyMatch.domain.MatchStatus;
import com.carpool.partyMatch.domain.PartyStatus;
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

  private final KafkaProducer kafkaProducer;

    @Override
    public List<MatchInfo> findMatchInfoList(Long partyInfoId, String matchStatus){
        log.info("********* findMatchUsers Service *********");

        MatchStatus ms = MatchStatus.valueOf(matchStatus);

        List<MatchInfo> matchInfoList = matchInfoRepository.findByPartyInfoIdAndMatchStatus(partyInfoId, ms);

        return matchInfoList;
    }

    //userId가 왜있는지 모르겠음..
//    @Override
//    public MatchProcessResponse findMatchInfo(Long partyInfoId, String userId){
//        log.info("********* findMatchInfo Service *********");
//
//        MatchInfo matchInfo = matchInfoRepository.findByPartyInfoIdAndUserId(partyInfoId, userId);
//        Party party = partyRepository.findByPartyInfoId(partyInfoId);
//
//        MatchProcessResponse matchProcessResponse = new MatchProcessResponse(partyInfoId, userId, MatchStatus.AVAILABLE);
//        Driver driver = party.getDriver();
//
//        if(matchInfo != null){
//            matchProcessResponse.setMatchStatus(matchInfo.getMatchStatus());
//        } else{
//            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);
//        }
//
//        if(driver.getDriverId().equals(userId)){
//
//            if(party.getPartyStatus() == PartyStatus.STARTED){
//                matchProcessResponse.setMatchStatus(MatchStatus.START);
//            }
//            else{
//                matchProcessResponse.setMatchStatus(MatchStatus.FORMED);
//            }
//        }
//
//        return matchProcessResponse;
//    }
//    @Override
//    public MatchProcessResponse findMatchInfo(Long partyInfoId, String userId){
//        log.info("********* findMatchInfo Service *********");
//        Party party = partyRepository.findByPartyInfoId(partyInfoId);
//
//        MatchInfo matchInfo = matchInfoRepository.findByPartyInfoIdAndUserId(partyInfoId, userId).get();
//
//        MatchProcessResponse matchProcessResponse = new MatchProcessResponse(partyInfoId, userId, MatchStatus.AVAILABLE);
//        Driver driver = party.getDriver();
//
//        if(matchInfo != null){
//            matchProcessResponse.setMatchStatus(matchInfo.getMatchStatus());
//        } else{
//            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);
//        }
//
//        if(driver.getDriverId().equals(userId)){
//
//            if(party.getPartyStatus() == PartyStatus.STARTED){
//                matchProcessResponse.setMatchStatus(MatchStatus.START);
//            }
//            else{
//                matchProcessResponse.setMatchStatus(MatchStatus.FORMED);
//            }
//        }
//
//        return matchProcessResponse;
//    }

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
        MatchInfo matchInfo = matchInfoRepository.findAllByPartyInfoIdAndUserId(matchInfoDto.getPartyInfoId(), matchInfoDto.getUserId()).stream().findFirst().orElse(null);
        if(matchInfo == null){
            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);
        }

        boolean isAccepted = false;
        if(matchInfo.getMatchStatus() == MatchStatus.ACCEPT) isAccepted = true;

        MatchStatus pastMatchStatus = matchInfo.getMatchStatus();
        matchInfo.setMatchStatus(MatchStatus.CANCEL);

        if(isAccepted){
            kafkaProducer.send("partyMemberCanceled-out", MatchCancelled.of(matchInfo, pastMatchStatus));
            minusPartyNumber(party);
        }
        //매칭 취소 이벤트 발행
//        if(isAccepted){
//            party.removePartyNumber();
//            MatchCancelled matchCancelled = new MatchCancelled();
//            BeanUtils.copyProperties(matchInfo, matchCancelled);
//            matchCancelled.publish();
//        }
    }

    @Override
    @Transactional
    public void cancelMatchInfoRollback(MatchCancelled matchCancelled){
        log.info("********* cancelMatchInfo *********");
        log.debug(String.valueOf(matchCancelled));

        Party party = partyRepository.findByPartyInfoId(matchCancelled.getPartyInfoId());

        //파티 상태 확인
        MatchInfo matchInfo = matchInfoRepository.findAllByPartyInfoIdAndUserId(matchCancelled.getPartyInfoId(), matchCancelled.getUserId()).stream().findFirst().get();
        matchInfo.setMatchStatus(matchCancelled.getPastMatchStatus());

        addPartyNumber(party);
    }

    @Override
    @Transactional
    public MatchInfo acceptMatchInfo(MatchProcessDto matchProcessDto){
        log.info("********* acceptMatchInfo *********");
        log.debug(String.valueOf(matchProcessDto));

        Party party = partyRepository.findByPartyInfoId(matchProcessDto.getPartyInfoId());
        validateDriver(party, matchProcessDto.getDriverId());

        MatchInfo matchInfo =  matchInfoRepository.findAllByPartyInfoIdAndUserId(matchProcessDto.getPartyInfoId(), matchProcessDto.getUserId()).stream().findFirst().get();
        validateProcess(matchInfo);

        MatchStatus pastMatchStatus = matchInfo.getMatchStatus();
        matchInfo.setMatchStatus(MatchStatus.ACCEPT);

        kafkaProducer.send("partyMemberAccept-out", MatchAccepted.of(matchInfo, pastMatchStatus));

        addPartyNumber(party);

        //매칭 수락 이벤트 발행
//        MatchAccepted matchAccepted = new MatchAccepted();
//        BeanUtils.copyProperties(matchInfo, matchAccepted);
//        matchAccepted.publish();

        return matchInfo;
    }


    @Override
    @Transactional
    public MatchInfo acceptMatchInfoRollback(MatchAccepted matchAccepted){
        log.info("********* acceptMatchInfoRollback *********");
        log.debug(String.valueOf(matchAccepted));

        Party party = partyRepository.findByPartyInfoId(matchAccepted.getPartyInfoId());

        MatchInfo matchInfo =  matchInfoRepository.findAllByPartyInfoIdAndUserId(matchAccepted.getPartyInfoId(), matchAccepted.getUserId()).stream().findFirst().get();
        matchInfo.setMatchStatus(matchAccepted.getPastMatchStatus());

        minusPartyNumber(party);

        return matchInfo;
    }

    public void minusPartyNumber(Party party){
        int restNumber = party.getCurNumberOfParty() - 1;
        if(restNumber < party.getMaxNumberOfParty()){
            PartyStatus pastPartyStatus = party.getPartyStatus();
            party.setPartyStatus(PartyStatus.OPEN);
            publishPartyStatusChanged(party, pastPartyStatus);
        }
        else if (restNumber > party.getMaxNumberOfParty()) {
            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);
        }
        party.setCurNumberOfParty(restNumber);

    }

    public void addPartyNumber(Party party) {
        int restNumber = party.getCurNumberOfParty() + 1;
        if(restNumber == party.getMaxNumberOfParty()){
            PartyStatus pastPartyStatus = party.getPartyStatus();
            party.setPartyStatus(PartyStatus.FULL);
            publishPartyStatusChanged(party, pastPartyStatus);
        }
        else if (restNumber > party.getMaxNumberOfParty()) {
            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);
        }
        party.setCurNumberOfParty(restNumber);
    }

    //파티관리 서비스에서 신청 가능 인원 확인 후 신청 불가할 경우에도 아래 실행
    @Override
    @Transactional
    public MatchInfo denyMatchInfo(MatchProcessDto matchProcessDto){
        log.info("********* denyMatchInfo *********");
        log.debug(String.valueOf(matchProcessDto));

        Party party = partyRepository.findByPartyInfoId(matchProcessDto.getPartyInfoId());
        validateDriver(party, matchProcessDto.getDriverId());

        MatchInfo matchInfo = matchInfoRepository.findAllByPartyInfoIdAndUserId(matchProcessDto.getPartyInfoId(), matchProcessDto.getUserId()).stream().findFirst().get();
        validateProcess(matchInfo);

        matchInfo.setMatchStatus(MatchStatus.DENY);

        return matchInfo;
    }

    private void validateApplyParty(Long partyInfoId, String userId) {

        //파티 상태 확인 (시작 또는 종료이면 신청 불가)
        Party party = partyRepository.findByPartyInfoId(partyInfoId);

        if(party.getPartyStatus() != PartyStatus.OPEN && party.getPartyStatus() != PartyStatus.FULL){

            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);

        }
        List<MatchStatus> matchStatuses = new ArrayList<>(){
            {
                add(MatchStatus.WAITING);
                add(MatchStatus.FORMED);
                add(MatchStatus.ACCEPT);
            }
        };
        MatchInfo matchInfo =  matchInfoRepository.findByPartyInfoIdAndUserIdAndMatchStatusIsIn(partyInfoId, userId, matchStatuses).orElse(null);
        if (matchInfo != null) {
            throw new ApiException(ApiStatus.INVALID_MODIFY_MATCH);
        }

//        List<MatchInfo> matchWaitList = matchInfoRepository.findByUserIdAndMatchStatus(userId, MatchStatus.WAITING);
//        List<MatchInfo> matchAcceptList = matchInfoRepository.findByUserIdAndMatchStatus(userId, MatchStatus.ACCEPT);
//        if (!matchWaitList.isEmpty()) {
//            throw new ApiException(ApiStatus.INVALID_MODIFY_MATCH);
//        }

    }

    private void validateCancelParty(Party party) {

        if(party.getPartyStatus() != PartyStatus.OPEN && party.getPartyStatus() != PartyStatus.FULL){

            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);

        }

    }

    private void validateDriver(Party party, String userId) {

        if(!party.isDriver(userId)){

            throw new ApiException(ApiStatus.NOT_DRIVER);

        }
    }

    private void validateProcess(MatchInfo matchInfo) {

        if(matchInfo.getMatchStatus() != MatchStatus.WAITING){

            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);

        }
    }

    public void publishPartyStatusChanged(Party party, PartyStatus pastPartyStatus){
        kafkaProducer.send("partyStatusChanged-out", new PartyStatusChanged(party.getPartyInfoId(), party.getPartyStatus(), pastPartyStatus));
    }

    @Override
    @Transactional
    public void partyStatusRejectRollback(PartyStatusChanged partyStatusChanged){
        log.info("********* partyStatusRejectRollback *********");

        Party party = partyRepository.findByPartyInfoId(partyStatusChanged.getPartyInfoId());
        party.setPartyStatus(partyStatusChanged.getPastPartyStatus());
    }

    @Override
    @Transactional
    public PartyProcessResponse startParty(PartyProcessDto partyProcessDto){
        log.info("********* startParty *********");
        log.debug(String.valueOf(partyProcessDto));

        Party party = partyRepository.findByPartyInfoId(partyProcessDto.getPartyInfoId());

        validateDriver(party, partyProcessDto.getUserId());

        PartyStatus pastPartyStatus = party.getPartyStatus();
        party.setPartyStatus(PartyStatus.STARTED);

        publishPartyStatusChanged(party, pastPartyStatus);

//        //파티 시작 이벤트 발행
//        PartyStarted partyStarted = new PartyStarted();
//        BeanUtils.copyProperties(party, partyStarted);
//        partyStarted.publish();

        PartyProcessResponse response = new PartyProcessResponse(party.getPartyInfoId(), party.getPartyStatus());

        return response;
    }


    @Override
    @Transactional
    public PartyProcessResponse closeParty(PartyProcessDto partyProcessDto){
        log.info("********* closeParty *********");
        log.debug(String.valueOf(partyProcessDto));

        Party party = partyRepository.findByPartyInfoId(partyProcessDto.getPartyInfoId());

        validateDriver(party, partyProcessDto.getUserId());

        PartyStatus pastPartyStatus = party.getPartyStatus();
        party.setPartyStatus(PartyStatus.CLOSED);

        publishPartyStatusChanged(party, pastPartyStatus);

//        //파티 종료 이벤트 발행
//        PartyClosed partyClosed = new PartyClosed();
//        BeanUtils.copyProperties(party, partyClosed);
//        partyClosed.publish();

        PartyProcessResponse response = new PartyProcessResponse(party.getPartyInfoId(), party.getPartyStatus());

        return response;
    }

    @Override
    @Transactional
    public PartyProcessResponse cancelParty(PartyProcessDto partyProcessDto){
        log.info("********* cancelParty *********");
        log.debug(String.valueOf(partyProcessDto));

        Party party = partyRepository.findByPartyInfoId(partyProcessDto.getPartyInfoId());

        validateDriver(party, partyProcessDto.getUserId());

        PartyStatus pastPartyStatus = party.getPartyStatus();
        party.setPartyStatus(PartyStatus.CANCELED);

        publishPartyStatusChanged(party, pastPartyStatus);

        //파티 취소 이벤트 발행
//        PartyCanceled partyClosed = new PartyCanceled();
//        BeanUtils.copyProperties(party, partyClosed);
//        partyClosed.publish();

        PartyProcessResponse response = new PartyProcessResponse(party.getPartyInfoId(), party.getPartyStatus());

        return response;
    }


    @Override
    @Transactional
    public void partyRegistered(PartyRegistered partyRegistered){
        log.info("********* registeredParty *********");

        Party party = Party.of(
                partyRegistered.getPartyId(),
                1,
                partyRegistered.getMaxNumberOfParty(),
                new Driver(partyRegistered.getDriverId(),partyRegistered.getDriverName()),
                PartyStatus.OPEN);

        MatchInfo matchInfo = MatchInfo.of(
                partyRegistered.getPartyId(),
                partyRegistered.getDriverId(),
                MatchStatus.FORMED);

        partyRepository.save(party);
        matchInfoRepository.save(matchInfo);
    }


}
