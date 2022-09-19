package com.carpool.partyMatch.domain;

public enum MatchStatus{
    WAITING, //파티 신청 후 대기
    ACCEPT, // 파티 신청 수락됨
    DENY, // 파티 신청 거절됨
    CANCEL, // 파티 신청 취소함
    START, // 파티 시작 됨
    CLOSE, // 파티 종료됨
    FORMED, // 파티 형성됨
    AVAILABLE; // 파티 진행 가능함...(신경 안써도 된다.)
}
