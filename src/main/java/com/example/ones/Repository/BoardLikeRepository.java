package com.example.ones.Repository;

import com.example.ones.Entity.Board_like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<Board_like,Long> {
    Optional<Board_like> findByLikeBoardidxAndLikeUseridx(Long likeBoardidx,Long likeUseridx);
}
