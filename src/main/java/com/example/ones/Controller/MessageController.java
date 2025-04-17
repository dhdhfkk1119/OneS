package com.example.ones.Controller;

import com.example.ones.DTO.MessageDTO;
import com.example.ones.Entity.Member;
import com.example.ones.Entity.Message;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Repository.MessageRepository;
import com.example.ones.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MemberRepository memberRepository;
    private final MessageService messageService;

    @GetMapping("/message/{memberIdx}")
    public String message(@PathVariable("memberIdx") Long memberIdx, Model model) {

        Member member = memberRepository.findByIdx(memberIdx)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을수없습니다"));

        model.addAttribute("member", member);
        return "message";
    }

    // 특정 유저와의 대화 내역 가져오기
    @GetMapping("/{user1}/{user2}")
    public List<MessageDTO> getConversation(@PathVariable Long user1, @PathVariable Long user2) {
        return messageService.getConversation(user1, user2);
    }

}
