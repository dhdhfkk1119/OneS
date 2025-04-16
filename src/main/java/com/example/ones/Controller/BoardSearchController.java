package com.example.ones.Controller;

import com.example.ones.Entity.Board;
import com.example.ones.Entity.Member;
import com.example.ones.Entity.Search_history;
import com.example.ones.Repository.BoardRepository;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Repository.SearchHistoryRepositroy;
import com.example.ones.Service.BoardSerachService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardSearchController {

    private final MemberRepository memberRepository;
    private final BoardSerachService boardSerachService;
    private final SearchHistoryRepositroy searchHistoryRepositroy;

    @GetMapping("/search")
    public String search(Model model, Principal principal,
                         @RequestParam(required = false) String keyword,
                         @RequestParam(required = false, defaultValue = "viewst") String sort) {

        Member member = null;
        if (principal != null) {
            String username = principal.getName();
            member = memberRepository.findByUserId(username).orElse(null);
        }

        List<Search_history> searchHistory = searchHistoryRepositroy.findBySearchUseridx(member.getIdx());

        model.addAttribute("member", member);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort); // 선택된 정렬값도 넘겨줘야 active 처리 가능
        model.addAttribute("searchHistory",searchHistory);

        if (keyword != null && !keyword.isEmpty()) {
            List<Board> boardList = boardSerachService.searchBoard(keyword, sort);
            model.addAttribute("boardList", boardList);
        }

        model.addAttribute("searchHistory", new Search_history());
        return "search";
    }
    



}
