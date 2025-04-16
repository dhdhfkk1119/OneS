package com.example.ones.Controller;

import com.example.ones.Entity.Member;
import com.example.ones.Entity.Search_history;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Repository.SearchHistoryRepositroy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SearchHistoryController {

    private final MemberRepository memberRepository;
    private final SearchHistoryRepositroy searchHistoryRepositroy;

    // 유저 검색 기록
    @PostMapping("/search-history")
    public ResponseEntity<Map<String,Object>> saveSearchHistory(@RequestBody Map<String, String> body, Principal principal) {
        String keyword = body.get("keyword");

        if (principal != null && keyword != null && !keyword.isBlank()) {
            String userId = principal.getName();
            Member member = memberRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없음"));

            Search_history history = new Search_history();
            history.setSearchKeyword(keyword);
            history.setSearchUseridx(member.getIdx());
            history.setSearchAt(LocalDateTime.now());

            searchHistoryRepositroy.save(history);

            return ResponseEntity.ok(Map.of("message", "저장 완료"));
        }

        return ResponseEntity.badRequest().body(Map.of("message", "저장 실패"));
    }
}
