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
import java.util.*;

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

        Map<Long, Member> boardWriteMap = new HashMap<>(); // 검색 기능으로 찾은 게시물을 작성한 유저의 정보
        Map<Long, List<String>> boardImagesMap = new HashMap<>(); // 검색 기능으로찾은 게시물 이미지 정보 가져오기

        Member member = null;

        if (principal != null) {
            String username = principal.getName();
            member = memberRepository.findByUserId(username).orElse(null);
            List<Search_history> searchHistory = searchHistoryRepositroy.findBySearchUseridx(member.getIdx());
            model.addAttribute("searchHistoryList", searchHistory);
        }

        model.addAttribute("member", member);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort); // 선택된 정렬값도 넘겨줘야 active 처리 가능

        // 👉 사용자 검색용 조건 분기
        if (keyword != null && !keyword.isEmpty()) {
            if ("user".equals(sort)) {
                // 검색어에 해당하는 유저 정보 가져오기
                List<Member> memberList = memberRepository.findBySearchKeyword(keyword);
                model.addAttribute("memberList", memberList);
            } else {
                List<Board> boardList = boardSerachService.searchBoard(keyword, sort);
                for (Board board : boardList) {
                    Member memberOpt = memberRepository.findByIdx(board.getBoardUseridx())
                            .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다"));
                    boardWriteMap.put(board.getBoardIdx(), memberOpt);

                    List<String> ImageList = new ArrayList();
                    if(board.getBoardImages() != null && !board.getBoardImages().isEmpty()) {
                        ImageList = Arrays.asList(board.getBoardImages().split(","));
                    }
                    boardImagesMap.put(board.getBoardIdx(), ImageList);
                }

                model.addAttribute("boardList", boardList);
                model.addAttribute("writers", boardWriteMap);
                model.addAttribute("boardImages", boardImagesMap);
            }
        }


        model.addAttribute("searchHistory", new Search_history());
        return "search";
    }


}
