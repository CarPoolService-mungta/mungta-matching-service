package com.carpool.partyMatch.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
import static com.carpool.partyMatch.constants.MatchTestSample.*;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

@WebMvcTest(MatchInfoController.class)
public class MatchInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchInfoService matchInfoService;

    private MatchInfo matchInfo;

    @BeforeEach
    void setUp(){
        matchInfo = new MatchInfo();
        matchInfo.setPartyInfoId(PARTYINFO_ID);
        matchInfo.setUserId(USER_ID);
        matchInfo.setMatchStatus(MATCH_STATUS);
    }

    @DisplayName("매칭 신청 API")
    @Test
    void applyParty() throws Exception {

        MatchInfoDto matchInfoDto = new MatchInfoDto(PARTYINFO_ID, USER_ID);

        doReturn(matchInfo)
                .when(matchInfoService).registerMatchInfo(matchInfoDto);

        ResultActions result = mockMvc.perform(
                post("/api/match/apply")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(matchInfoDto))
        );

        result.andExpect(status().isOk());

    }

    @DisplayName("매칭 정보 조회 API")
    @Test
    void getMatchInfo() throws Exception {

        MatchProcessResponse matchProcessResponse = new MatchProcessResponse(matchInfo.getPartyInfoId(), matchInfo.getUserId(), matchInfo.getMatchStatus());

        doReturn(matchProcessResponse)
                .when(matchInfoService).findMatchInfo(PARTYINFO_ID,USER_ID);


        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("partyInfoId", "1");
        info.add("userId", USER_ID);

        ResultActions result = mockMvc.perform(
                get("/api/match/matchInfo")
                        .accept(MediaType.APPLICATION_JSON)
                        .params(info)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.partyInfoId").value(PARTYINFO_ID))
                .andExpect(jsonPath("$.userId").value(USER_ID));
    }


    @DisplayName("매칭 취소 API")
    @Test
    void cancelPartyApplication() throws Exception {

        MatchInfoDto matchInfoDto = new MatchInfoDto(PARTYINFO_ID, USER_ID);

        doNothing()
                .when(matchInfoService).cancelMatchInfo(matchInfoDto);

        ResultActions result = mockMvc.perform(
                post("/api/match/cancel")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(matchInfoDto))
        );

        result.andExpect(status().isNoContent());
    }
}
