package org.logly.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, ErrorCode.E400, "요청값 검증에 실패 했습니다", LogLevel.DEBUG),
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, ErrorCode.E401, "인증된 사용자만 수행할 수 있는 요청입니다.", LogLevel.DEBUG),
    AUTHORIZATION_ERROR(HttpStatus.FORBIDDEN, ErrorCode.E403, "권한이 부족한 유저입니다.", LogLevel.DEBUG),
    TOO_MANY_REQUEST(HttpStatus.TOO_MANY_REQUESTS, ErrorCode.E429, "잠시 후 다시 시도 해주세요.", LogLevel.DEBUG),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "예상하지 못한 예외가 발생 했습니다.", LogLevel.ERROR);

    private final HttpStatus httpStatus;
    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;
}
