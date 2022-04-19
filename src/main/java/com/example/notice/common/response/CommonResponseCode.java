package com.example.notice.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonResponseCode implements CommonCode {

	SUCCESS(200, "요청에 성공했습니다."),
	INVALID_INPUT_VALUE(400, "입력된 값이 잘못 되었습니다."),
	ACCESS_DENIED(403, "액세스 권한이 없습니다."),
	METHOD_NOT_ALLOWED(405, "지원하지 않는 Method 입니다."),
	INTERNAL_SERVER_ERROR(500, "예상치 못한 오류가 발생했습니다."),
	;

	private final int code;
	private final String msg;

}
