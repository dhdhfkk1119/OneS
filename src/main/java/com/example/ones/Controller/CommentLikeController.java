package com.example.ones.Controller;

import com.example.ones.Entity.Board;
import com.example.ones.Entity.Comment;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.CommentRepository;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/comment_like/status/{commentidx}")
    public ResponseEntity<Map<String, Object>> icCheckCommentLike(@PathVariable Long commentidx , Principal principal) {
        Map<String, Object> response = new HashMap<>();

        String username = principal.getName();
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("해당유저를 찾을수없습니다"));

        Long likeCount = commentRepository.findByCommentIdx(commentidx)
                .map(Comment::getCommentLike) // 좋아요 개수 가져오기
                .orElse(0L);
        response.put("likeCount", likeCount);

        boolean isCheck = commentLikeService.isCheckCommentLike(commentidx,member.getIdx());
        response.put("status", isCheck ? "liked" : "not_liked");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/comment_like/{commentidx}")
    public ResponseEntity<Map<String, Object>> toggleCommentLike(@PathVariable Long commentidx , Principal principal) {
        Map<String, Object> response = new HashMap<>();

        String username = principal.getName();
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("해당유저를 찾을수없습니다"));

        Long likeCount = commentRepository.findByCommentIdx(commentidx)
                .map(Comment::getCommentLike) // 좋아요 개수 가져오기
                .orElse(0L);
        response.put("likeCount", likeCount);

        boolean isCheck = commentLikeService.toggleCheckCommentLike(commentidx,member.getIdx());
        response.put("status", isCheck ? "added" : "removed");

        return ResponseEntity.ok(response);
    }
}
