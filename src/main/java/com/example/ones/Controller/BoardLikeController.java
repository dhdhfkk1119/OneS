package com.example.ones.Controller;

import com.example.ones.Entity.Board;
import com.example.ones.Entity.Board_like;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.BoardRepository;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Service.BoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class BoardLikeController {

    private final BoardLikeService boardLikeService;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    // GET 요청: 찜 상태 확인
    @GetMapping("/board/like-status/{boardIdx}")
    public ResponseEntity<Map<String, Object>> checkProductLike(@PathVariable Long boardIdx, Principal principal) {
        Map<String, Object> response = new HashMap<>();

        // 로그인 확인
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "로그인이 필요합니다."));
        }

        // 로그인한 유저 정보 가져오기
        String username = principal.getName();
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        Long useridx = member.getIdx();

        // 찜 개수 조회
        Long likeCount = boardRepository.findByBoardIdx(boardIdx)
                .map(Board::getBoardLike) // 좋아요 개수 가져오기
                .orElse(0L);
        response.put("likeCount", likeCount);

        // 찜 상태 확인
        boolean isLiked = boardLikeService.isBoardLiked(boardIdx, useridx);
        response.put("status", isLiked ? "liked" : "not_liked");

        return ResponseEntity.ok(response);
    }


    // POST 요청: 찜 추가 및 취소
    @PostMapping("/board/like/{boardIdx}")
    public ResponseEntity<Map<String, Object>> toggleProductLike(@PathVariable Long boardIdx, Principal principal) {
        Map<String, Object> response = new HashMap<>();

        // 로그인 확인
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "로그인이 필요합니다."));
        }

        // 로그인한 유저 정보 가져오기
        String username = principal.getName();
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        Long useridx = member.getIdx();

        // 찜 개수 조회
        Long likeCount = boardRepository.findByBoardIdx(boardIdx)
                .map(Board::getBoardLike) // 좋아요 개수 가져오기
                .orElse(0L);
        response.put("likeCount", likeCount);

        // 찜 상태 토글
        boolean isLiked = boardLikeService.addLike(boardIdx, useridx);
        response.put("status", isLiked ? "added" : "removed");

        return ResponseEntity.ok(response);
    }
}
