package com.example.ones.Repository;

import com.example.ones.Entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
     Optional<Board> findByBoardIdx(Long boardidx);
     List<Board> findByBoardUseridx(Long useridx);
}
