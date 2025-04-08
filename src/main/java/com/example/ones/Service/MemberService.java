package com.example.ones.Service;

import com.example.ones.Entity.Member;
import com.example.ones.DTO.MemberDTO;
import com.example.ones.Repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;



    // 아이디 중복 체크
    public Boolean isIdCheck(String userid){
        return memberRepository.existsByUserId(userid);
    }

    @Transactional
    public Member register(MemberDTO memberDTO){
        if(memberRepository.existsByUserId(memberDTO.getUserId())){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다");
        }
        if(memberRepository.existsByUserName(memberDTO.getUserName())){
            throw new IllegalArgumentException("이미 존재하는 이름입니다");
        }
        if(memberRepository.existsByUserEmail(memberDTO.getUserEmail())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }
        if(memberRepository.existsByUserPhoneNumber(memberDTO.getUserPhoneNumber())){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다");
        }


        //DTO -> Entity 변환
        Member member = new Member();
        member.setUserId(memberDTO.getUserId());
        member.setUserName(memberDTO.getUserName());
        member.setUserPassword(passwordEncoder.encode(memberDTO.getUserPassword()));
        member.setUserAge(memberDTO.getUserAge());
        member.setUserPhoneNumber(memberDTO.getUserPhoneNumber());
        member.setUserAddress(memberDTO.getUserAddress());
        member.setUserEmail(memberDTO.getUserEmail());
        member.setUserImage(memberDTO.getUserImage());
        member.setUserPoint(200L); // 기본 포인트 값 설정
        member.setUserAction("In-progress");
        member.setUserStatus("disconnect");

        return memberRepository.save(member);
    }
}
