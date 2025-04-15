package com.example.ones.Controller;

import com.example.ones.Entity.Board;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberRepository memberRepository;

    @GetMapping("/boardWrite")
    public String boardWrite(Model model , Principal principal){
        String username = principal.getName();
        Member member = memberRepository.findByUserId(username)
                        .orElseThrow(() -> new RuntimeException("해당 유저를 찾을수없습니다"));

        model.addAttribute("member",member);
        model.addAttribute("board",new Board());
        return "boardWrite";
    }

    // 메인페이지 게실물 작성하기
    @PostMapping("/board")
    public String register(Model model ,
                           Principal principal,
                           @Valid @ModelAttribute("board") Board board,
                           @Validated @RequestParam("files") MultipartFile[] files,
                           RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        Member member = memberRepository.findByUserId(username).
                orElseThrow(() -> new RuntimeException("유저정보를 찾을수없습니다"));


        try{
            boardService.registerBoard(board,member.getIdx(),files);
            redirectAttributes.addFlashAttribute("message", "게실물을 작성했습니다");
            return "redirect:/";
        }catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다");
            return "redirect:/";
        }

    }
}
