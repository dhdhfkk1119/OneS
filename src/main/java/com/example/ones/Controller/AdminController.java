package com.example.ones.Controller;


import com.example.ones.Entity.Board;
import com.example.ones.Entity.Comment;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.BoardRepository;
import com.example.ones.Repository.CommentRepository;
import com.example.ones.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Delete;
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
    private final CommentRepository commentRepository;

    @GetMapping("/mypage")
    public String mypage(Principal principal, Model model) {

        // 헌재 로그인한 유저의 인증정보를 가져온다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities(); // roles()말고 authorities()

        // 로그인 유저의 role 상태를 체크해서 ADMIN 이면 true 를 반납 anyMatch -> 있는경우 true
        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")); // "ROLE_ADMIN"으로 체크

        Map<Long, Member> boardMembersMap = new HashMap<>(); // 게시물 작성자 정보를 가져오기
        Map<Long, List<String>> boardImagesMap = new HashMap<>(); // 게시물 이미지 정보 "," 로 가져오기

        // 로그인 한 유저가 admin 인지 체크한다
        if (isAdmin) {
            String username = principal.getName();
            Member member = memberRepository.findByUserId(username)
                    .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다"));

            // 모든 유저들의 정보를 가져오기
            List<Member> memberList = memberRepository.findAllByOrderByUserAtDesc();
            
            // 게시물에 대한 댓글 가져오기
            Map<Long,List<Comment>> CommentMap = new HashMap<>();

            // 댓글 에 대한 유저 정보 가져오기
            Map<Long,Member> commentMemberMap = new HashMap<>();

            // 댓글에 대한 이미지 정보 가져오기
            Map<Long,List<String>> commentImagesMap = new HashMap<>();
            
            // 현재 로그인 중인 유저들의 숫자만 가져오기
            Long connectUserCount = memberList.stream()
                    .filter(memberC -> "connect".equals(memberC.getUserStatus()))
                    .count();
            
            // 현재 승인 되지 않은 유저들의 수
            Long InProgress = memberList.stream()
                    .filter(memberA -> "In-progress".equals(memberA.getUserAction()))
                    .count();
            // 현재 승인 된 유저들의 수
            Long InCompleted = memberList.stream()
                    .filter(memberA -> "In-completed".equals(memberA.getUserAction()))
                    .count();


            // 모든 유저들의 게시물 가져오기
            List<Board> boardList = boardRepository.findAllByOrderByBoardIdxDesc();
            for (Board board : boardList) {
                Member boardMemberList = memberRepository.findByIdx(board.getBoardUseridx()).orElseThrow(() -> new RuntimeException("해당유저를 찾을수없습니다"));
                boardMembersMap.put(board.getBoardIdx(), boardMemberList);

                List<String> boardImageList = new ArrayList<>();
                if (board.getBoardImages() != null && !board.getBoardImages().isEmpty()) {
                    boardImageList = Arrays.asList(board.getBoardImages().split(","));
                }
                boardImagesMap.put(board.getBoardIdx(), boardImageList);

                // 게시판에 달린 댓글
                List<Comment> commentList = commentRepository.findByCommentBoardidx(board.getBoardIdx());
                CommentMap.put(board.getBoardIdx(), commentList);
                for (Comment comment : commentList) {
                    // 댓글에 달린유저 정보
                    Member commentMember = memberRepository.findByIdx(comment.getCommentUseridx()).orElseThrow(() -> new RuntimeException("해당유저를 찾을수없습니다"));
                    commentMemberMap.put(comment.getCommentIdx(), commentMember);
                    
                    // 댓글에 달린 이미지
                    List<String> commentImageList = new ArrayList<>();
                    if (comment.getCommentImages() != null && !comment.getCommentImages().isEmpty()) {
                        commentImageList = Arrays.asList(comment.getCommentImages().split(","));
                    }
                    commentImagesMap.put(comment.getCommentIdx(), commentImageList);
                }
            }


            model.addAttribute("memberList", memberList); // 모든 유저의 정보
            model.addAttribute("boardList", boardList); // 게시물 정보
            model.addAttribute("writers", boardMembersMap); // 게시물 작성자 정보
            model.addAttribute("boardImages", boardImagesMap); // 게시물 이미지 정보
            model.addAttribute("connectMember", connectUserCount); // 현재 로그인 중인 유저 수
            model.addAttribute("InProgress", InProgress); // 현재 유저의 수를 가져온다
            model.addAttribute("InCompleted", InCompleted); // 현재 유저의 수를 가져온다
            model.addAttribute("commentList",CommentMap); // 댓글 정보
            model.addAttribute("commentwirtes",commentMemberMap); // 댓글을 작성한 유저의 정보
            model.addAttribute("commentImages",commentImagesMap); // 댓글에 작성된 이미지정보

        }

        return "mypage";
    }
    
    // 회원가입 유저 승인하기
    @PostMapping("/admin/approveUser")
    public ResponseEntity<Map<String, Object>> approveUser(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> map = new HashMap<>();

        Long useridx = Long.valueOf(requestData.get("userIdx").toString());

        Member member = memberRepository.findByIdx(useridx).
                orElseThrow(() -> new RuntimeException("해당 유저를 찾을수없습니다"));

        member.setUserAction("In-completed");
        memberRepository.save(member);

        // 현재 승인 되지 않은 유저
        Long InProgress = memberRepository.countByUserAction("In-progress");
        map.put("InProgressCount", InProgress);

        // 현재 승인 된 유저 수
        Long InCompleted = memberRepository.countByUserAction("In-completed");
        map.put("InCompletedCount", InCompleted);

        return ResponseEntity.ok(map);
    }

    // 회원 가입 유저 삭제하기
    @PostMapping("/admin/DeleteUser")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> map = new HashMap<>();

        Long useridx = Long.valueOf(requestData.get("userIdx").toString());

        Member member = memberRepository.findByIdx(useridx).
                orElseThrow(() -> new RuntimeException("해당 유저를 찾을수없습니다"));

        memberRepository.delete(member);

        // 현재 승인 되지 않은 유저 수
        Long InProgress = memberRepository.countByUserAction("In-progress");
        map.put("InProgressCount", InProgress);

        // 현재 승인 된 유저 수
        Long InCompleted = memberRepository.countByUserAction("In-completed");
        map.put("InCompletedCount", InCompleted);

        return ResponseEntity.ok(map);
    }
    
    // 회원 게시물 삭제하기
    @PostMapping("/board/Delete")
    public ResponseEntity<Map<String,Object>> deleteBoard(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> map = new HashMap<>();

        Long boardIdx = Long.valueOf(requestData.get("boardIdx").toString());
        Board board = boardRepository.findByBoardIdx(boardIdx)
                .orElseThrow(() -> new RuntimeException("해당 게시물이 없습니다"));
        List<Comment> comment = commentRepository.findByCommentBoardidx(boardIdx);

        commentRepository.deleteAll(comment);
        boardRepository.delete(board);
        Long BoardCount = boardRepository.count();
        map.put("BoardCount", BoardCount);

        return ResponseEntity.ok(map);
    }

}
