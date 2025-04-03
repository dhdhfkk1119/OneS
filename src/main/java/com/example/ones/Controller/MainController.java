package com.example.ones.Controller;

import com.example.ones.Entity.Board;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberRepository memberRepository;
    private final BoardService boardService;
    
    // 메인페이지에서 게실물 작성하기
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("board", new Board());
        return "index";
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
    
        System.out.println( "컨트롤러 유저번호 " + username);
        System.out.println( "컨트롤러 유저이름 " + username);
        System.out.println( "컨트롤러 이미지이름" + username);
        System.out.println( "컨트롤러 시간" + username);
        
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

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @ModelAttribute
    public void modelAttributes(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();// 현재 로그인한 사용자의 정보
        if(authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User){
            User user = (User) authentication.getPrincipal(); // 현재 로그인한 유저
            String userId = user.getUsername(); // 유저의 ID
            
            //DB에서 해당하는 정보 가져오기
            Optional<Member> optionalMember = memberRepository.findByUserId(userId);
            String UserName = optionalMember.map(Member::getUserName).orElse("");
            model.addAttribute("UserName", UserName); // Member에서 이름 가져오기
            
            //권한 목록 가져오기
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            String role = authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse("ROLE_USER");
            model.addAttribute("role", role);

        }else {
            model.addAttribute("UserName","");
            model.addAttribute("role","ROLE_GUEST");
        }
    }

}
