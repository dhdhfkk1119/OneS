package com.example.ones.Controller;

import com.example.ones.Entity.Comment;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final MemberRepository memberRepository;


    @PostMapping("comment")
    public String registerComment(@ModelAttribute("comment") Comment comment,
                                   @Validated @RequestParam("commentfiles") MultipartFile[] files,
                                   @RequestParam Long boardidx,
                                   @RequestParam("from") String from,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        String username = principal.getName();
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수없습니다"));

        try{
            commentService.registerComment(comment,files,boardidx,member.getIdx());
            redirectAttributes.addFlashAttribute("message","댓글을 작성했습니다");
            if("index".equals(from)){
                return "redirect:/";
            }else{
                return "redirect:/board/" + boardidx;
            }
        }catch (IllegalArgumentException e){
            model.addAttribute("error",e.getMessage());
            return "redirect:/";
        }catch (Exception e){
            model.addAttribute("error",e.getMessage());
            return "redirect:/";
        }
    }
}
