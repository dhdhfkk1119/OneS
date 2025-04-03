package com.example.ones.Service;

import com.example.ones.Entity.Member;
import com.example.ones.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> _member = this.memberRepository.findByUserId(username);
        if(_member.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
        }
        Member member = _member.get();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if("admin".equals(username)){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else{
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new User(member.getUserId(),member.getUserPassword(), grantedAuthorities);
    }
}
