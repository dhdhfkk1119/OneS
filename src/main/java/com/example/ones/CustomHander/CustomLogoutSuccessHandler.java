package com.example.ones.CustomHander;

import com.example.ones.Repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {

        if (authentication != null) {
            String userId = authentication.getName();

            memberRepository.findByUserId(userId).ifPresent(member -> {
                member.setUserStatus("disconnect");
                memberRepository.save(member);
            });
        }

        response.sendRedirect("/login?logout");
    }
}