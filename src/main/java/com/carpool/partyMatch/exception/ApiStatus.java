package com.carpool.partyMatch.exception;

import java.util.Arrays;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ApiStatus {

    INVALID_MODIFY_MATCH(HttpStatus.BAD_REQUEST, -701, "이미 신청중인 매칭이 있습니다."),
    INVALID_DENY_MATCH(HttpStatus.BAD_REQUEST, -702, "신청 불가한 매칭입니다."),
    NOT_EXIST_MATCH(HttpStatus.BAD_REQUEST,-703, "요청하신 정보가 존재하지 않습니다."),
    NOT_DRIVER(HttpStatus.BAD_REQUEST,-704, "운전자가 아닙니다."),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -1, "예상치 못한 에러가 발생하였습니다."),
    ALREADY_WAITING_PARTY(HttpStatus.BAD_REQUEST, -701, "이미 해당 파티를 신청하였습니다."),
    ;
    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    ApiStatus(HttpStatus httpStatus, Integer code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public static ApiStatus of(String message) {
        return Arrays.stream(ApiStatus.values())
                .filter(apiStatus -> apiStatus.getMessage().equals(message))
                .findFirst()
                .orElse(null);
    }
}
