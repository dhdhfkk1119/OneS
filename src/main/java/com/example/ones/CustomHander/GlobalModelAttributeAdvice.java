package com.example.ones.CustomHander;

import com.example.ones.Entity.Member;
import com.example.ones.Repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@ControllerAdvice
@RequiredArgsConstructor
@Transactional
public class GlobalModelAttributeAdvice {
    private final MemberRepository memberRepository;
    
    // 현재 로그인한 유저의 정보를 가져옴
    @ModelAttribute
    public void addCommonAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            String userId = user.getUsername();

            // 로그인 성공시 해당 유저의 로그인 유무(status) -> connect 로 변경
            Optional<Member> memberOptional = memberRepository.findByUserId(userId);

            if(memberOptional.isPresent()) {
                Member member = memberOptional.get();
                member.setUserStatus("connect");
                memberRepository.save(member);
                model.addAttribute("UserName", member.getUserName());



            } else {
                model.addAttribute("UserName", "");
            }

            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            String role = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("ROLE_USER");
            model.addAttribute("role", role);

        } else {
            model.addAttribute("UserName", "");
            model.addAttribute("role", "ROLE_GUEST");
        }
    }

    // 세션이 종료 될 경우 사용자 로그인 status -> discoonect 로 변경
    @EventListener
    public void handleSessionDestroyed(SessionDestroyedEvent event) {
        List<SecurityContext> contexts = event.getSecurityContexts();
        for (SecurityContext context : contexts) {
            Object principal = context.getAuthentication().getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                String userId = userDetails.getUsername();
                memberRepository.findByUserId(userId).ifPresent(member -> {
                    member.setUserStatus("disconnect");
                    memberRepository.save(member);
                });
            }
        }
    }

}
