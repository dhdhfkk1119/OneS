package com.example.ones.Controller;

import com.example.ones.Entity.Board;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.BoardRepository;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Service.BoardSerachService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardSearchController {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardSerachService boardSerachService;

    @GetMapping("/search")
    public String search(Model model, Principal principal, @RequestParam(required = false) String keyword) {
        Member member = null;

        if (principal != null) {
            String username = principal.getName();
            member = memberRepository.findByUserId(username)
                    .orElse(null);
        }

        model.addAttribute("member", member);
        model.addAttribute("keyword", keyword);

        // 검색어가 있을 때만 boardList 전달
        if (keyword != null && !keyword.isEmpty()) {
            List<Board> board = boardSerachService.searchBoard(keyword);
            model.addAttribute("boardList", board);
        }

        return "search";
    }


}
