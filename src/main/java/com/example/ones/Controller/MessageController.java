package com.example.ones.Controller;

import com.example.ones.Entity.Member;
import com.example.ones.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MemberRepository memberRepository;

    @GetMapping("/message/{memberIdx}")
    public String message(@PathVariable("memberIdx") Long memberIdx, Model model) {

        Member member = memberRepository.findByIdx(memberIdx)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을수없습니다"));

        model.addAttribute("member", member);
        return "message";
    }
}
