package com.example.ones.Controller;

import com.example.ones.Entity.Member;
import com.example.ones.Entity.Message;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;

    @GetMapping("/message/{idx}")
    public String message(@PathVariable("idx") Long idx, Model model, Principal principal) {

        String username = principal.getName();
        Member loginUser = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Member targetUser = memberRepository.findByIdx(idx)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다"));

        List<Message> messageList = messageRepository.findMessagesBetweenUsers(loginUser.getIdx(), targetUser.getIdx());

        model.addAttribute("login", loginUser);
        model.addAttribute("member", targetUser);
        model.addAttribute("messages", messageList);

        return "message";
    }

}
