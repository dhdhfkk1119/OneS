package com.example.ones.Controller;

import com.example.ones.Entity.Follow;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.FollowRepository;
import com.example.ones.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserFollowerListController {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @GetMapping("/follower/{Idx}")
    public String follower(@PathVariable("Idx") Long Idx, Principal principal, Model model) {

        Member memberOpt = null;

        if (principal != null) {
            String username = principal.getName();
            memberOpt = memberRepository.findByUserId(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        // 내가 팔로우 하고 있는 유저
        List<Follow> following = followRepository.findByFollowLoginidx(Idx);
        List<Member> followingUsers = new ArrayList<>();

        for (Follow follow : following) {
            Member followingUser = memberRepository.findByIdx(follow.getFollowUseridx())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            followingUsers.add(followingUser);
        }


        // 나를 팔로우 하고 있는 유저 리스트
        List<Follow> follow = followRepository.findByFollowUseridx(Idx);
        List<Member> followerUsers = new ArrayList<>();

        for (Follow follower : follow) {
            Member followingUser = memberRepository.findByIdx(follower.getFollowLoginidx())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            followerUsers.add(followingUser);
        }
        
        model.addAttribute("following", followingUsers); // 나를팔로우 하고 있는 유저의 정보
        model.addAttribute("follower", followerUsers); // 내가 팔로우가 하고 있는 유저의 정보
        model.addAttribute("member", memberOpt);


        return "follower";
    }
}
