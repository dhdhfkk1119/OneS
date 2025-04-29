package com.example.ones.Controller;

import com.example.ones.Entity.*;
import com.example.ones.Repository.*;
import com.example.ones.Service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MypageController {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final FollowRepository followRepository;
    private final MessageRepository messageRepository;

    @GetMapping("/mypage")
    public String myPage(Principal principal, Model model) {
        
        String username = principal.getName();
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("해당유저를 찾을수없습니다"));


        model.addAttribute("member", member);
        return "mypage";
    }

    @PostMapping("/delete/user")
    public String deleteUser(Principal principal,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             Authentication authentication) {
        String username = principal.getName();
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다"));

        List<Board> board = boardRepository.findByBoardUseridx(member.getIdx());
        List<Comment> comment = commentRepository.findByCommentUseridx(member.getIdx());
        List<Message> message = messageRepository.findBySenderIdxOrReceiverIdx(member.getIdx(),member.getIdx());
        List<Follow> follows = followRepository.findByFollowLoginidxOrFollowUseridx(member.getIdx(),member.getIdx());

        followRepository.deleteAll(follows);
        messageRepository.deleteAll(message);
        commentRepository.deleteAll(comment);
        boardRepository.deleteAll(board);
        memberRepository.delete(member);

        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "redirect:/";
    }
}
