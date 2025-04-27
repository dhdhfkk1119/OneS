package com.example.ones.Repository;

import com.example.ones.Entity.Board;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
     Optional<Board> findByBoardIdx(Long boardidx);
     List<Board> findByBoardUseridx(Long useridx);

     // 검색 및 정렬기능
     List<Board> findByBoardContentContainingIgnoreCase(String keyword, Sort sort);
     
     // 모든 게시물 가져오기
     List<Board> findAllByOrderByBoardIdxDesc();
}         

