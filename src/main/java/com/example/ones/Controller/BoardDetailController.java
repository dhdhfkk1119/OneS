package com.example.ones.Controller;

import com.example.ones.Entity.Board;
import com.example.ones.Entity.Comment;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.BoardRepository;
import com.example.ones.Repository.CommentRepository;
import com.example.ones.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class BoardDetailController {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/board/{boardIdx}")
    public String boardDetail(@PathVariable("boardIdx") Long boardIdx,
                              Model model,
                              Principal principal) {
        Optional<Board> board = boardRepository.findByBoardIdx(boardIdx);

        Member memberOpt = null;

        if (principal != null) {  // 사용자가 로그인한 경우
            String username = principal.getName();
            memberOpt = memberRepository.findByUserId(username)
                    .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다"));
        }

        Map<Long, Member> memberList = new HashMap<>(); // 게시물에 대한 유저 정보가져오기
        Map<Long, List<String>> BoardImages = new HashMap<>(); // 게시물에 대한 이미지 구별하기
        Map<Long, List<String>> CommentImages = new HashMap<>(); // 댓글에 대한 이미지 구별하기
        Map<Long, Member> commentmemberList = new HashMap<>(); // 댓글에 대한 유저 정보가져오기

        List<Comment> comments = commentRepository.findByCommentBoardidx(boardIdx);

        Long useridx = board.get().getBoardUseridx();
        Optional<Member> member = memberRepository.findByIdx(useridx);
        memberList.put(board.get().getBoardIdx(), member.get());

        List<String> images = new ArrayList<>();
        Board boardImage = board.get();
        if (boardImage.getBoardImages() != null && !boardImage.getBoardImages().isEmpty()) {
            images = Arrays.asList(boardImage.getBoardImages().split(","));
        }
        BoardImages.put(board.get().getBoardIdx(), images); // 이게 맞음


        for (Comment comment : comments) {
            List<String> commentImages = new ArrayList<>();
            if (comment.getCommentImages() != null && !comment.getCommentImages().isEmpty()) {
                commentImages = Arrays.asList(comment.getCommentImages().split(","));
            }
            CommentImages.put(comment.getCommentIdx(), commentImages);

            Long commentUseridx = comment.getCommentUseridx();
            Optional<Member> member2 = memberRepository.findByIdx(commentUseridx);
            commentmemberList.put(comment.getCommentIdx(),member2.get());
        }

        Board ViewCount = board.get();
        ViewCount.setBoardView(ViewCount.getBoardView() + 1L);
        boardRepository.save(ViewCount);

        model.addAttribute("boards", board.get());
        model.addAttribute("commentList", comments);
        model.addAttribute("writers", memberList);
        model.addAttribute("boardImages", BoardImages);
        model.addAttribute("commentImages", CommentImages);
        model.addAttribute("commentwirtes", commentmemberList);
        model.addAttribute("comment",new Comment());
        model.addAttribute("member", memberOpt);
        return "boardDetail";
    }
}
