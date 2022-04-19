package com.example.notice.board.service;

import com.example.notice.attachfile.repository.AttachFileRepository;
import com.example.notice.board.dto.BoardDTO;
import com.example.notice.board.repository.BoardRepository;
import com.example.notice.common.response.CommonResponse;
import com.example.notice.common.response.CommonResponseCode;
import com.example.notice.common.util.CommonUtil;
import com.example.notice.entity.AttachFile;
import com.example.notice.entity.Board;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	private final AttachFileRepository attachFileRepository;

	@Value("${env.saveDir}")
	private String saveDir;

	@Cacheable("noticeList")
	public CommonResponse<List<Board>> findAll() {
		CommonResponse<List<Board>> response = CommonResponse.<List<Board>>builder()
				.data(boardRepository.findAll())
				.build();

		return response.result(CommonResponseCode.SUCCESS);
	}

	@Cacheable("noticeInfo")
	@Transactional
	public CommonResponse<Board> findAllBySeq(long seq) {
		Board board = boardRepository.findAllBySeq(seq);

		Board boardBuilder = Board.builder()
				.seq(board.getSeq())
				.title(board.getTitle())
				.content(board.getContent())
				.hit(board.getHit() + 1)
				.startDate(board.getStartDate())
				.endDate(board.getEndDate())
				.register(board.getRegister())
				.regDate(board.getRegDate())
				.attachFiles(board.getAttachFiles())
				.build();

		boardRepository.save(boardBuilder);

		CommonResponse<Board> response = CommonResponse.<Board>builder()
				.data(boardBuilder)
				.build();

		return response.result(CommonResponseCode.SUCCESS);
	}

	@CacheEvict(value = {"noticeList", "noticeInfo"}, allEntries = true)
	@Transactional
	public CommonResponse<Board> savePost(BoardDTO boardDTO, List<MultipartFile> multipartFiles) {
		Board boardBuilder = BoardDTO.builder()
				.title(boardDTO.getTitle())
				.content(boardDTO.getContent())
				.hit(0)
				.startDate(boardDTO.getStartDate())
				.endDate(boardDTO.getEndDate())
				.register(boardDTO.getRegister())
				.build()
				.toEntity();

		// 게시글 저장
		boardRepository.save(boardBuilder);

		// 첨부파일 있는 경우
		if (multipartFiles.size() > 0) {
			// 저장 경로 폴더 확인 및 생성
			File dir = CommonUtil.mkdir(saveDir);

			List<AttachFile> attachFiles = new ArrayList<>();

			for (int i = 0; i < multipartFiles.size(); i++) {
				MultipartFile file = multipartFiles.get(i);

				// 저장용 고유 이름 생성
				StringBuilder saveFileName = new StringBuilder();
				saveFileName.append(boardBuilder.getSeq()).append("_").append(i).append("_").append(UUID.randomUUID());

				AttachFile attachFile = AttachFile.builder()
						.board(boardBuilder)
						.fileName(file.getOriginalFilename())
						.saveFileName(saveFileName.toString())
						.build();

				attachFiles.add(attachFile);

				try {
					file.transferTo(new File(dir + File.separator + saveFileName));
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}

			// 게시글의 파일 정보 저장
			attachFileRepository.saveAll(attachFiles);
		}

		CommonResponse<Board> response = CommonResponse.<Board>builder()
				.data(boardBuilder)
				.build();

		return response.result(CommonResponseCode.SUCCESS);
	}

	@CacheEvict(value = {"noticeList", "noticeInfo"}, allEntries = true)
	@Transactional
	public CommonResponse<Board> updatePost(long seq, BoardDTO boardDTO) {
		Board board = boardRepository.findAllBySeq(seq);

		Board boardBuilder = Board.builder()
				.seq(board.getSeq())
				.title(boardDTO.getTitle())
				.content(boardDTO.getContent())
				.hit(boardDTO.getHit())
				.startDate(boardDTO.getStartDate())
				.endDate(boardDTO.getEndDate())
				.register(board.getRegister())
				.regDate(board.getRegDate())
				.attachFiles(board.getAttachFiles())
				.build();

		boardRepository.save(boardBuilder);

		CommonResponse<Board> response = CommonResponse.<Board>builder()
				.data(board)
				.build();

		return response.result(CommonResponseCode.SUCCESS);
	}

	@CacheEvict(value = {"noticeList", "noticeInfo"}, allEntries = true)
	@Transactional
	public CommonResponse<Void> deletePost(long seq) {
		Board board = boardRepository.findAllBySeq(seq);

		attachFileRepository.deleteAll(board.getAttachFiles());
		boardRepository.delete(board);

		board.getAttachFiles().forEach(attachFile -> {
			File file  = new File(saveDir + File.separator + attachFile.getSaveFileName());
			try {
				FileUtils.forceDelete(file);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		});

		CommonResponse<Void> response = CommonResponse.<Void>builder()
				.data(null)
				.build();

		return response.result(CommonResponseCode.SUCCESS);
	}
}