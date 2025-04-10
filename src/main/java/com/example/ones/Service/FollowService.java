package com.example.ones.Service;

import com.example.ones.Entity.Follow;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.FollowRepository;
import com.example.ones.Repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    //현재 팔로우 했는지를 체크
    @Transactional
    public boolean isFollowCheck(Long useridx, Long loginidx) {
        Optional<Follow> existingFollow = followRepository.findByFollowLoginidxAndFollowUseridx(loginidx, useridx);
        return existingFollow.isPresent();
    }

    // 팔로우 누르면 해당 상태를 팔로우 함
    @Transactional
    public boolean addFollow(Long useridx, Long loginidx) {
        Optional<Follow> existingFollow = followRepository.findByFollowLoginidxAndFollowUseridx(loginidx, useridx);

        // 팔로잉 - 해당 유저의 팔로워 수가 증가
        Member member1 = memberRepository.findByIdx(loginidx)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        // 팔로워 - 해당 유저의 팔로잉 수가 증가 
        Member member2 = memberRepository.findByIdx(useridx)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        if (existingFollow.isPresent()) {
            member1.setFollowing(member1.getFollowing() - 1);
            memberRepository.save(member1);
            member2.setFollow(member2.getFollow() - 1);
            memberRepository.save(member2);

            followRepository.delete(existingFollow.get());
            return false;

        } else {
            member1.setFollowing(member1.getFollowing() + 1);
            memberRepository.save(member1);
            member2.setFollow(member2.getFollow() + 1);
            memberRepository.save(member2);

            Follow follow = new Follow(); // 객체 생성
            follow.setFollowUseridx(useridx);
            follow.setFollowLoginidx(loginidx);
            follow.setFollowAt(LocalDateTime.now());
            followRepository.save(follow);
            return true;

        }
    }

}
