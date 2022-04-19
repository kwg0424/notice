package com.example.notice.board.controller;

import com.example.notice.board.dto.BoardDTO;
import com.example.notice.board.service.BoardService;
import com.example.notice.common.response.CommonResponse;
import com.example.notice.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	/**
	 * 공지사항 목록 조회
	 *
	 * @return CommonResponse<List<Board>>
	 */
	@GetMapping("")
	private CommonResponse<List<Board>> board() {
		return boardService.findAll();
	}

	/**
	 * 공지사항 조회
	 *
	 * @param seq
	 * @return CommonResponse<Board>
	 */
	@GetMapping("/{seq}")
	private CommonResponse<Board> board(@PathVariable long seq) {
		return boardService.findAllBySeq(seq);
	}

	/**
	 * 공지사항 등록
	 *
	 * @param multipartFiles
	 * @param boardDTO
	 * @return CommonResponse<Board>
	 */
	@PostMapping("")
	private CommonResponse<Board> postBoard(@RequestPart("board") BoardDTO boardDTO, @RequestPart("file") List<MultipartFile> multipartFiles) {
		return boardService.savePost(boardDTO, multipartFiles);
	}

	/**
	 * 공지사항 수정
	 *
	 * @param seq
	 * @param boardDTO
	 * @return CommonResponse<Board>
	 */
	@PutMapping("/{seq}")
	private CommonResponse<Board> putBoard(@PathVariable long seq, @RequestPart("board") BoardDTO boardDTO) {
		return boardService.updatePost(seq, boardDTO);
	}

	/**
	 * 공지사항 삭제
	 *
	 * @param seq
	 * @return Void
	 */
	@DeleteMapping("/{seq}")
	private CommonResponse<Void> deleteBoard(@PathVariable long seq) {
		return boardService.deletePost(seq);
	}

}
