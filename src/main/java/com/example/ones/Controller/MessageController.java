package com.example.ones.Controller;

import com.example.ones.Entity.Member;
import com.example.ones.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MemberRepository memberRepository;

    @GetMapping("/message/{idx}")
    public String message(@PathVariable("idx") Long idx, Model model, Principal principal) {

        String username = principal.getName();
        Member memberOpt = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Member member = memberRepository.findByIdx(idx)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다"));

        model.addAttribute("login", memberOpt);
        model.addAttribute("member", member);
        return "message";
    }

}
