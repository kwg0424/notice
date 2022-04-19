package com.example.notice;

import com.example.notice.board.dto.BoardDTO;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoticeApplicationTests {

	private final String domain = "http://localhost";

	@Resource
	TestRestTemplate testRestTemplate;

	@DisplayName("통합 테스트")
	@Test
	void integrateTest(){
		this.insertTest(); // 등록
		this.updateTest(); // 수정
		this.listTest(); // 목록
		this.infoTest(); // 상세
		this.deleteTest(); // 삭제
	}

	@DisplayName("공지사항 목록 조회")
	@Test
	@Order(3)
	void listTest() {
		ResponseEntity<Map> response = testRestTemplate.exchange(domain + "/board", HttpMethod.GET, null, Map.class);

		// 데이터가 성공적으로 가져오는지 확인
		MatcherAssert.assertThat(response.getBody().get("status"), is(HttpStatus.OK.value()));
	}

	@DisplayName("공지사항 상세 조회")
	@Test
	@Order(4)
	void infoTest() {
		ResponseEntity<Map> response = testRestTemplate.exchange(domain + "/board", HttpMethod.GET, null, Map.class);
		List<LinkedHashMap<String, Object>> boards = (ArrayList<LinkedHashMap<String, Object>>) response.getBody().get("data");

		// 게시글의 목록이 0보다 큰지 확인
		MatcherAssert.assertThat(boards.size(), greaterThan(0));

		// 게시글의 ID로 상세 조회
		LinkedHashMap<String, Object> board = boards.get(0);
		response = testRestTemplate.exchange(domain + "/board/" + board.get("seq"), HttpMethod.GET, null, Map.class);
		MatcherAssert.assertThat(response.getBody().get("status"), is(HttpStatus.OK.value()));
	}

	@DisplayName("공지사항 입력")
	@Test
	@Order(1)
	void insertTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("board", tempBoardData().toEntity());
		for (int i = 0; i < 3; i++) {
			body.add("file", new FileSystemResource(tempFile()));
		}

		ResponseEntity<Map> response = testRestTemplate.exchange(domain + "/board", HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);

		// 데이터가 성공적으로 가져오는지 확인
		MatcherAssert.assertThat(response.getBody().get("status"), is(HttpStatus.OK.value()));
	}

	@DisplayName("공지사항 수정")
	@Test
	@Order(2)
	void updateTest() {
		LinkedHashMap<String, Object> board = this.getFirstItemByList();

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("board", tempBoardData().toEntity());

		HttpHeaders headers = new HttpHeaders();

		ResponseEntity<Map> response = testRestTemplate.exchange(domain + "/board/" + board.get("seq"), HttpMethod.PUT, new HttpEntity<>(body, headers), Map.class);

		// 데이터가 성공적으로 가져오는지 확인
		MatcherAssert.assertThat(response.getBody().get("status"), is(HttpStatus.OK.value()));
	}

	@DisplayName("공지사항 삭제")
	@Test
	@Order(5)
	void deleteTest() {
		LinkedHashMap<String, Object> board = this.getFirstItemByList();

		ResponseEntity<Map> response = testRestTemplate.exchange(domain + "/board/" + board.get("seq"), HttpMethod.DELETE, null, Map.class);

		// 데이터가 성공적으로 가져오는지 확인
		MatcherAssert.assertThat(response.getBody().get("status"), is(HttpStatus.OK.value()));
	}

	/**
	 * 첫번째 항목 가져오기 위한 조회
	 * @return
	 */
	private LinkedHashMap<String, Object> getFirstItemByList() {
		ResponseEntity<Map> response = testRestTemplate.exchange(domain + "/board", HttpMethod.GET, null, Map.class);
		List<LinkedHashMap<String, Object>> boards = (ArrayList<LinkedHashMap<String, Object>>) response.getBody().get("data");

		// 게시글의 목록이 0보다 큰지 확인
		MatcherAssert.assertThat(boards.size(), greaterThan(0));

		// 게시글의 ID로 상세 조회
		return boards.get(0);
	}

	/**
	 * 공지사항 테스트용 데이터 생성
	 * @return
	 */
	private BoardDTO tempBoardData() {
		return BoardDTO.builder()
				.title("테스트 제목 " + UUID.randomUUID()) // 제목
				.content("테스트 내용 " + UUID.randomUUID()) // 내용
				.startDate(LocalDateTime.now().plusMinutes(1)) // 공지 시작일시 1분뒤
				.endDate(LocalDateTime.now().plusMinutes(2)) // 공지 종료일시 2분뒤
				.register("김우겸")
				.build();
	}

	/**
	 * 공지사항 테스트용 파일 생성
	 * @return
	 */
	private File tempFile() {
		File file = null;
		try {
			String fileName = "업로드용 임시 파일 " + UUID.randomUUID();

			Path tempFile = Files.createTempFile(fileName, ".txt");
			Files.write(tempFile, ("내용 " + UUID.randomUUID()).getBytes());
			file = tempFile.toFile();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return file;
	}

}
