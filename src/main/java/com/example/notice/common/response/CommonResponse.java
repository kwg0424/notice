package com.example.notice.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class CommonResponse<T> {

	private int status;
	private String message;
	private LocalDateTime timestamp;
	private T data;

	public CommonResponse<T> result(CommonCode common) {
		setStatus(common.getCode());
		setMessage(common.getMsg());
		setTimestamp(LocalDateTime.now());
		return this;
	}

}
