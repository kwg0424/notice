package com.example.notice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "ATTACH_FILE")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class AttachFile {

	@Comment("파일 ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ")
	private long seq;

	@Comment("게시글 ID")
	@ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "BOARD_SEQ")
	private Board board;

	@Comment("파일 실제 이름")
	@Column(name = "FILE_NAME", nullable = false)
	private String fileName;

	@Comment("파일 저장 이름")
	@Column(name = "SAVE_FILE_NAME", nullable = false)
	private String saveFileName;

}