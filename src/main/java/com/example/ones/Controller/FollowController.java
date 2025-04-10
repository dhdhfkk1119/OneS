package com.example.ones.Controller;

import com.example.ones.Entity.Board;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.BoardRepository;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Service.FollowService;
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
public class FollowController {

    private final FollowService followService;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    
    // 팔로우 팔로잉 체크
    @GetMapping("/profile/follow/{memberIdx}")
    public ResponseEntity<Map<String, Object>> checkFollow(@PathVariable Long memberIdx, Principal principal) {
        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "로그인이 필요합니다"));
        }

        String username = principal.getName();
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다"));

        boolean isFollow = followService.isFollowCheck(memberIdx, member.getIdx());
        boolean isMutual = followService.isFollowCheck(member.getIdx(), memberIdx); // 상대가 나를 팔로우

        Long followCount = memberRepository.findByIdx(memberIdx)
                .map(Member::getFollow)
                .orElse(0L);
        response.put("followCount",followCount);

        if (isFollow) {
            response.put("status", "following");
        } else if (!isFollow && isMutual) {
            response.put("status", "mutual");
        } else {
            response.put("status", "not_following");
        }

        return ResponseEntity.ok(response);
    }

    // 팔로우 팔로잉 클릭
    @PostMapping("/profile/following/{memberIdx}")
    public ResponseEntity<Map<String, Object>> toggleFollowing(@PathVariable Long memberIdx, Principal principal) {
        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "로그인이 필요합니다"));
        }

        String username = principal.getName();
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다"));

        boolean isFollowing = followService.addFollow(memberIdx, member.getIdx());

        Long followCount = memberRepository.findByIdx(memberIdx)
                .map(Member::getFollow)
                .orElse(0L);
        response.put("followCount",followCount);

        if(isFollowing){
            response.put("status","added");
        } else {
            response.put("status","removed");
        }
        return ResponseEntity.ok(response);
    }
}
