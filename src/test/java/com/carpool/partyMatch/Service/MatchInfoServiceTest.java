package com.carpool.partyMatch.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


import com.carpool.partyMatch.domain.MatchStatus;
import com.carpool.partyMatch.domain.Party;
import com.carpool.partyMatch.domain.PartyStatus;
import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.domain.Driver;
import com.carpool.partyMatch.controller.dto.MatchInfoDto;
import com.carpool.partyMatch.controller.dto.MatchProcessDto;
import com.carpool.partyMatch.controller.dto.PartyProcessDto;
import com.carpool.partyMatch.controller.dto.response.MatchInfoResponse;
import com.carpool.partyMatch.controller.dto.response.MatchProcessResponse;
import com.carpool.partyMatch.controller.dto.response.PartyProcessResponse;
import com.carpool.partyMatch.service.serviceImpl.MatchInfoServiceImpl;
import com.carpool.partyMatch.repository.MatchInfoRepository;
import com.carpool.partyMatch.repository.PartyRepository;
import com.carpool.partyMatch.exception.ApiException;
import com.carpool.partyMatch.exception.ApiStatus;
import static com.carpool.partyMatch.constants.MatchTestSample.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(value = MockitoExtension.class)
class MatchInfoServiceTest {

    @InjectMocks
    @Spy
    private MatchInfoServiceImpl matchInfoService;

    @Mock
    private MatchInfoRepository matchInfoRepository;

    @Mock
    private PartyRepository partyRepository;

    private MatchInfo matchInfo;
    private Party party;

    @BeforeEach
    void setUp(){
        matchInfo = new MatchInfo();
        matchInfo.setId(MATCHINFO_ID);
        matchInfo.setPartyInfoId(PARTYINFO_ID);
        matchInfo.setUserId(USER_ID);
        matchInfo.setMatchStatus(MATCH_STATUS);

        party = new Party();
        Driver driver = new Driver(DRIVER_ID, DRIVER_NAME);
        party.setCurNumberOfParty(1);
        party.setPartyInfoId(PARTYINFO_ID);
        party.setDriver(driver);
        party.setPartyStatus(PartyStatus.AVAILABLE);
    }


    @DisplayName("매칭 신청 API")
    @Test
    void applyParty() {

        given(matchInfoRepository.save(any())).willReturn(matchInfo);
        given(partyRepository.findByPartyInfoId(PARTYINFO_ID)).willReturn(party);

        MatchInfoDto matchInfoDto = new MatchInfoDto(PARTYINFO_ID, USER_ID);
        MatchInfo matchInfoResult = matchInfoService.registerMatchInfo(matchInfoDto);

        assertThat(matchInfoResult.getUserId()).isEqualTo(USER_ID);

    }

    @DisplayName("매칭 정보 조회 API")
    @Test
    void getMatchInfo() {

        given(matchInfoRepository.findByPartyInfoIdAndUserId(PARTYINFO_ID, USER_ID).get()).willReturn(matchInfo);
        given(partyRepository.findByPartyInfoId(PARTYINFO_ID)).willReturn(party);

        MatchProcessResponse matchProcessResponse = matchInfoService.findMatchInfo(PARTYINFO_ID, USER_ID);

        assertAll(
                () -> assertThat(matchProcessResponse.getUserId()).isEqualTo(USER_ID),
                () -> assertThat(matchProcessResponse.getPartyInfoId()).isEqualTo(PARTYINFO_ID),
                () -> assertThat(matchProcessResponse.getMatchStatus()).isEqualTo(MATCH_STATUS)
        );
    }


    @DisplayName("파라미터로 matchInfo 객체를 찾을 수 없는 경우 Exception 발생.")
    @Test
    void getMatchInfo_not_found_by_partyid_and_userid() {
        given(matchInfoRepository.findByPartyInfoIdAndUserId(2L, USER_ID))
                .willThrow(new ApiException(ApiStatus.NOT_EXIST_MATCH));

        assertThatThrownBy(() -> matchInfoService.findMatchInfo(2L, USER_ID))
                .isInstanceOf(ApiException.class)
                .hasMessage("요청하신 정보가 존재하지 않습니다.");
    }


    @DisplayName("운전자 ID와 수락 파라미터로 받은 운전자 ID가 일치하지 않는 경우 Exception 발생.")
    @Test
    void acceptMatchInfo_not_match_by_driverId() {

        given(matchInfoRepository.findByPartyInfoIdAndUserId(PARTYINFO_ID, USER_ID).get()).willReturn(matchInfo);
        given(partyRepository.findByPartyInfoId(PARTYINFO_ID)).willReturn(party);

        MatchProcessDto matchProcessDto = new MatchProcessDto(PARTYINFO_ID, DRIVER_ID, DRIVER_NAME, USER_ID);

        assertThatThrownBy(() -> matchInfoService.acceptMatchInfo(matchProcessDto))
                .isInstanceOf(ApiException.class)
                .hasMessage("요청하신 정보가 존재하지 않습니다.");
    }


}
