package com.example.notice.board.dto;

import com.example.notice.entity.AttachFile;
import com.example.notice.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardDTO {
	private String title;
	private String content;
	private int hit;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String register;
	private List<AttachFile> attachFiles;

	@Builder
	public BoardDTO(String title, String content, int hit, LocalDateTime startDate, LocalDateTime endDate, String register, List<AttachFile> attachFiles) {
		this.title = title;
		this.content = content;
		this.hit = hit;
		this.startDate = startDate;
		this.endDate = endDate;
		this.register = register;
		this.attachFiles = attachFiles;
	}

	public Board toEntity() {
		return Board.builder()
				.title(title)
				.content(content)
				.hit(hit)
				.startDate(startDate)
				.endDate(endDate)
				.register(register)
				.build();
	}

}
