package com.example.notice.board.repository;

import com.example.notice.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
	Board findAllBySeq(long seq);
}