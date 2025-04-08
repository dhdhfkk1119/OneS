package com.example.ones.Repository;

import com.example.ones.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByCommentBoardidx(Long boardidx);
    Optional<Comment> findByCommentIdx(Long commentId);
    List<Comment> findByCommentUseridx(Long useridx);
}
