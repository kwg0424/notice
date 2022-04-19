package com.example.notice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "BOARD")
@EntityListeners(AuditingEntityListener.class)
public class Board {

	@Comment("게시글 ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ")
	private long seq;

	@Comment("제목")
	@Column(name = "TITLE", nullable = false)
	private String title;

	@Comment("내용")
	@Column(name = "CONTENT", length = 4000)
	private String content;

	@Comment("조회수")
	@Column(name = "HIT")
	private int hit;

	@Comment("공지 시작일시")
	@Column(name = "START_DATE")
	private LocalDateTime startDate;

	@Comment("공지 종료일시")
	@Column(name = "END_DATE")
	private LocalDateTime endDate;

	@Comment("작성자")
	@Column(name = "REGISTER", nullable = false)
	private String register;

	@Comment("등록일시")
	@CreatedDate
	@Column(name = "REG_DATE", nullable = false)
	private LocalDateTime regDate;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<AttachFile> attachFiles;

}