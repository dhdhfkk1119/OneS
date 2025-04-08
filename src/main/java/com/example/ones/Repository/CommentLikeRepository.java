package com.example.ones.Repository;

import com.example.ones.Entity.Comment_like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<Comment_like,Long> {
    Optional<Comment_like> findByCommentlikeCommentidxAndCommentlikeUseridx (Long commentLikeidx, Long commentUseridx);
}
