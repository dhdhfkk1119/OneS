package com.example.ones.CustomHander;

import com.example.ones.Entity.Member;
import com.example.ones.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collection;
import java.util.Optional;

@Component
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeAdvice {
    private final MemberRepository memberRepository;
    
    // 현재 로그인한 유저의 정보를 가져옴
    @ModelAttribute
    public void addCommonAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            String userId = user.getUsername();

            Optional<Member> optionalMember = memberRepository.findByUserId(userId);
            String userName = optionalMember.map(Member::getUserName).orElse("");
            model.addAttribute("UserName", userName);

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
}
