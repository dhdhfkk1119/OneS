package com.example.ones.Service;

import com.example.ones.Entity.Comment;
import com.example.ones.Entity.Comment_like;
import com.example.ones.Repository.CommentLikeRepository;
import com.example.ones.Repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    // 댓글 찜하기 상태 체크
    @Transactional
    public boolean isCheckCommentLike(Long commentidx , Long useridx) {
        Optional<Comment_like> commentLike = commentLikeRepository.findByCommentlikeCommentidxAndCommentlikeUseridx(commentidx,useridx);
        return commentLike.isPresent();
    }

    // 댓글 찜하기 클릭시 증가
    @Transactional
    public boolean toggleCheckCommentLike(Long commentidx , Long useridx) {
        Optional<Comment_like> commentLike = commentLikeRepository.findByCommentlikeCommentidxAndCommentlikeUseridx(commentidx,useridx);

        Comment comment = commentRepository.findByCommentIdx(commentidx)
                .orElseThrow(() -> new RuntimeException("해당 댓글을 찾을수없습니다"));


        if(commentLike.isPresent()) {
            comment.setCommentLike(comment.getCommentLike() - 1);
            commentRepository.save(comment);

            commentLikeRepository.delete(commentLike.get());
            return false;
        }else{

            comment.setCommentView(comment.getCommentView() + 1);
            comment.setCommentLike(comment.getCommentLike() + 1);
            commentRepository.save(comment);

            Comment_like comment_like = new Comment_like();
            comment_like.setCommentlikeCommentidx(commentidx);
            comment_like.setCommentlikeUseridx(useridx);
            comment_like.setCommentlikeAt(LocalDateTime.now());
            commentLikeRepository.save(comment_like);

            return true;
        }
    }
}
