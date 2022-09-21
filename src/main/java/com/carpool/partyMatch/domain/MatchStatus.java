package com.carpool.partyMatch.domain;

public enum MatchStatus{
    WAITING, //파티 신청 후 대기
    ACCEPT, // 파티 신청 수락됨
    DENY, // 파티 신청 거절됨
    CANCEL, // 파티 신청 취소함
    START, // 파티 시작 됨
    CLOSE, // 파티 종료됨
    FORMED, // 파티 형성됨
    REJECT,//파티 생성이 파티매칭에 되지 않았을 경우
}
