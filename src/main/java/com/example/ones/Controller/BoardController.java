package com.example.ones.Controller;

import com.example.ones.Entity.Board;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberRepository memberRepository;

//    @GetMapping("/board")
//    public String board(Model model ) {
//        model.addAttribute("board", new Board());
//        return "board";
//    }
//
//    @PostMapping("/board")
//    public String register(Model model ,
//                           Principal principal,
//                           @Valid @ModelAttribute("board") Board board) {
//        String username = principal.getName();
//        Member member = memberRepository.findByUserId(username).
//                orElseThrow(() -> new RuntimeException("유저정보를 찾을수없습니다"));
//
//        boardService.registerBoard(board,member.getIdx());
//        return "redirect:/";
//    }
}
