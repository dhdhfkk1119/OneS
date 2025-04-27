package com.example.ones.Controller;


import com.example.ones.Entity.Board;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.BoardRepository;
import com.example.ones.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @GetMapping("/mypage")
    public String mypage(Principal principal, Model model){
        
        // 헌재 로그인한 유저의 인증정보를 가져온다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities(); // roles()말고 authorities()
        
        // 로그인 유저의 role 상태를 체크해서 ADMIN 이면 true 를 반납 anyMatch -> 있는경우 true
        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")); // "ROLE_ADMIN"으로 체크

        Map<Long,Member> boardMembersMap = new HashMap<>(); // 게시물 작성자 정보를 가져오기
        Map<Long,List<String>> boardImagesMap = new HashMap<>(); // 게시물 이미지 정보 "," 로 가져오기

        // 로그인 한 유저가 admin 인지 체크한다
        if (isAdmin) {
            String username = principal.getName();
            Member member = memberRepository.findByUserId(username)
                    .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다"));
            
            // 모든 유저들의 정보를 가져오기
            List<Member> memberList = memberRepository.findAllByOrderByUserAtDesc();
            
            // 모든 유저들의 게시물 가져오기
            List<Board> boardList = boardRepository.findAllByOrderByBoardIdxDesc();
            for(Board board : boardList){
                Member boardMemberList = memberRepository.findByIdx(board.getBoardUseridx()).orElseThrow(() -> new RuntimeException("해당유저를 찾을수없습니다"));
                boardMembersMap.put(board.getBoardIdx(), boardMemberList);

                List<String> boardImageList = new ArrayList<>();
                if(board.getBoardImages() != null && board.getBoardImages().isEmpty()) {
                    boardImageList = Arrays.asList(board.getBoardImages().split(","));
                }
                boardImagesMap.put(board.getBoardIdx(), boardImageList);
            }


            model.addAttribute("memberList", memberList); // 모든 유저의 정보
            model.addAttribute("boardList", boardList); // 게시물 정보
            model.addAttribute("boardMembersMap", boardMembersMap); // 게시물 작성자 정보
            model.addAttribute("boardImagesMap", boardImagesMap); // 게시물 이미지 정보
        }

        return "mypage";
    }

    @PostMapping("/admin/approveUser")
    public ResponseEntity<Map<String,Object>> approveUser(@RequestBody Map<String, Object> requestData){
        Map<String,Object> map = new HashMap<>();

        Long useridx = Long.valueOf(requestData.get("userIdx").toString());

        Member member = memberRepository.findByIdx(useridx).
                orElseThrow(() -> new RuntimeException("해당 유저를 찾을수없습니다"));

        member.setUserAction("In-completed");
        memberRepository.save(member);

        return ResponseEntity.ok(map);
    }

}
