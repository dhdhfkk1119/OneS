package com.example.ones.Repository;

import com.example.ones.Entity.Board;
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

     @Query("SELECT p FROM Board p WHERE " +
             "LOWER(p.boardContent) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
             "ORDER BY p.boardIdx DESC")
     List<Board> findAllSearch(@Param("keyword") String keyword);
}
