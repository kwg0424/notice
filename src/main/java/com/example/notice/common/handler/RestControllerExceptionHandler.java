package com.example.notice.common.handler;

import com.example.notice.common.response.CommonResponse;
import com.example.notice.common.response.CommonResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

	/**
	 * null 데이터 오류 발생
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler({NullPointerException.class})
	public ResponseEntity<CommonResponse<Void>> handleNullPointerException(final NullPointerException e) {
		CommonResponse<Void> response = CommonResponse.<Void>builder().build().result(CommonResponseCode.INTERNAL_SERVER_ERROR);
		log.info(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	/**
	 * 지원하지 않은 HTTP method 호출 할 경우 발생
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<CommonResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		CommonResponse<Void> response = CommonResponse.<Void>builder().build().result(CommonResponseCode.METHOD_NOT_ALLOWED);
		response.setMessage(response.getMessage() + e.getMessage());
		log.info(e.getMessage());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
	}

	/**
	 * 기본 오류 발생
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler({Exception.class})
	public ResponseEntity<CommonResponse<Void>> handleException(final Exception e) {
		CommonResponse<Void> response = CommonResponse.<Void>builder().build().result(CommonResponseCode.INTERNAL_SERVER_ERROR);
		response.setMessage(response.getMessage() + " " + e.getMessage());
		log.info(e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

}
