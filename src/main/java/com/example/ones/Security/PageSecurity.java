package com.example.ones.Security;


import com.example.ones.CustomHander.CustomLogoutSuccessHandler;
import com.example.ones.CustomHander.GlobalModelAttributeAdvice;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class PageSecurity {

    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrfConfig) ->
                        csrfConfig.disable()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/board_list", "/sign", "/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .invalidateHttpSession(true)
                )
        ;
        return http.build();
    }

    // 로그인 인증 유무 체크
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
