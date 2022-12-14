package com.carpool.partyMatch.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


import com.carpool.partyMatch.domain.Party;
import com.carpool.partyMatch.domain.PartyStatus;
import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.domain.Driver;
import com.carpool.partyMatch.controller.dto.MatchInfoDto;
import com.carpool.partyMatch.controller.dto.MatchProcessDto;
import com.carpool.partyMatch.controller.dto.response.MatchProcessResponse;
import com.carpool.partyMatch.service.serviceImpl.MatchInfoServiceImpl;
import com.carpool.partyMatch.repository.MatchInfoRepository;
import com.carpool.partyMatch.repository.PartyRepository;
import com.carpool.partyMatch.exception.ApiException;
import com.carpool.partyMatch.exception.ApiStatus;

import java.util.List;

import static com.carpool.partyMatch.constants.MatchTestSample.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


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
        party.setPartyStatus(PartyStatus.OPEN);
    }


    @DisplayName("?????? ?????? API")
    @Test
    void applyParty() {

        given(matchInfoRepository.save(any())).willReturn(matchInfo);
        given(partyRepository.findByPartyInfoId(PARTYINFO_ID)).willReturn(party);

        MatchInfoDto matchInfoDto = new MatchInfoDto(PARTYINFO_ID, USER_ID);
        MatchInfo matchInfoResult = matchInfoService.registerMatchInfo(matchInfoDto);

        assertThat(matchInfoResult.getUserId()).isEqualTo(USER_ID);

    }

//    @DisplayName("?????? ?????? ?????? API")
//    @Test
//    void getMatchInfo() {
//
//        given(matchInfoRepository.findByPartyInfoIdAndUserId(PARTYINFO_ID, USER_ID).get()).willReturn(matchInfo);
//        given(partyRepository.findByPartyInfoId(PARTYINFO_ID)).willReturn(party);
//
//        MatchProcessResponse matchProcessResponse = matchInfoService.findMatchInfo(PARTYINFO_ID, USER_ID);
//
//        assertAll(
//                () -> assertThat(matchProcessResponse.getUserId()).isEqualTo(USER_ID),
//                () -> assertThat(matchProcessResponse.getPartyInfoId()).isEqualTo(PARTYINFO_ID),
//                () -> assertThat(matchProcessResponse.getMatchStatus()).isEqualTo(MATCH_STATUS)
//        );
//    }
//
//
//    @DisplayName("??????????????? matchInfo ????????? ?????? ??? ?????? ?????? Exception ??????.")
//    @Test
//    void getMatchInfo_not_found_by_partyid_and_userid() {
//        given(matchInfoRepository.findByPartyInfoIdAndUserId(2L, USER_ID))
//                .willThrow(new ApiException(ApiStatus.NOT_EXIST_MATCH));
//
//        assertThatThrownBy(() -> matchInfoService.findMatchInfo(2L, USER_ID))
//                .isInstanceOf(ApiException.class)
//                .hasMessage("???????????? ????????? ???????????? ????????????.");
//    }


    @DisplayName("????????? ID??? ?????? ??????????????? ?????? ????????? ID??? ???????????? ?????? ?????? Exception ??????.")
    @Test
    void acceptMatchInfo_not_match_by_driverId() {

        given(matchInfoRepository.findAllByPartyInfoIdAndUserIdOrderByIdDesc(PARTYINFO_ID, USER_ID)).willReturn(List.of(matchInfo));
        given(partyRepository.findByPartyInfoId(PARTYINFO_ID)).willReturn(party);

        MatchProcessDto matchProcessDto = new MatchProcessDto(PARTYINFO_ID, DRIVER_ID, DRIVER_NAME, USER_ID);

        assertThatThrownBy(() -> matchInfoService.acceptMatchInfo(matchProcessDto))
                .isInstanceOf(ApiException.class)
                .hasMessage("???????????? ????????? ???????????? ????????????.");
    }


}
