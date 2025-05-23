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
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    // 메인페이지에서 게실물 작성하기
    @GetMapping("/")
    public String index(Model model,Principal principal) {

        Member member = null;

        if (principal != null) {  // 사용자가 로그인한 경우
            String username = principal.getName();
            member = memberRepository.findByUserId(username)
                    .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다"));
        }

        List<Board> boards = boardRepository.findAllByOrderByBoardIdxDesc(); // 전체 게시물 가져오기
        Map<Long,Member> boardMembers = new HashMap<>(); // 게시물을 작성한 유저의 정보를 가져오기
        Map<Long, List<String>> boardImages = new HashMap<>(); // 각 게시물에 해당하는 이미지 리스트 가져오기

        Map<Long,List<Comment>> boardComments = new HashMap<>(); // 각 게시물에 달린 댓글 정보 가져오기
        Map<Long,Member> CommentsMembers = new HashMap<>(); // 댓글을 작성한 유저의 정보를 가져오기
        Map<Long,List<String>> CommentImages = new HashMap<>(); // 댓글에 달린 유저 정보인 이미지를 , 로 가져오기
        Map<Long, Long> commentCounts = new HashMap<>(); // 게시물에 달린 댓글 갯수


        for (Board board : boards){
            Long CommentCount = commentRepository.countByCommentBoardidx(board.getBoardIdx());
            commentCounts.put(board.getBoardIdx(), CommentCount);

            Long useridx = board.getBoardUseridx();
            Member writer = memberRepository.findByIdx(useridx).orElseThrow(() -> new RuntimeException("게시물 작성자를 찾을 수 없습니다"));
            boardMembers.put(board.getBoardIdx(), writer);

            List<String> images = new ArrayList<>();
            if (board.getBoardImages() != null && !board.getBoardImages().isEmpty()) {
                images = Arrays.asList(board.getBoardImages().split(","));
            }
            boardImages.put(board.getBoardIdx(), images);

            //게시물에 작성한 해당하는 댓글 정보 가져오기
            List<Comment> comment = commentRepository.findByCommentBoardidx(board.getBoardIdx());
            boardComments.put(board.getBoardIdx(), comment);



            //댓글을 작성한 유저의 정보를 가져오기 및 이미지 값 나누기
            for (Comment comment1 : comment) {
                // 댓글을 단 유저의 정보를 가져오기
                Long commentuseridx = comment1.getCommentUseridx();
                Member members = memberRepository.findByIdx(commentuseridx).orElseThrow(() -> new RuntimeException("댓글 작성자를 찾을 수없습니다"));
                CommentsMembers.put(comment1.getCommentIdx(), members);

                // 해당 댓글에서작성한 이미지를 , 나누기
                List<String> commentImages = new ArrayList<>();
                if(comment1.getCommentImages() != null && !comment1.getCommentImages().isEmpty()) {
                    commentImages = Arrays.asList(comment1.getCommentImages().split(","));
                }
                CommentImages.put(comment1.getCommentIdx(), commentImages);
            }
        }

        model.addAttribute("commentwirtes",CommentsMembers); // 댓글을 작성한 유저의 정보
        model.addAttribute("commentList", boardComments);// 게시글에 작성한 댓글 정보
        model.addAttribute("commentImages", CommentImages); // 댓글에 작성된 이미지를 , 로 나눠서 가져옴

        model.addAttribute("member", member); // 현재 로그인한 유저의 정보
        model.addAttribute("boardList", boards); // 모든 게실물
        model.addAttribute("writers", boardMembers); // 게시물 작성자의 정보
        model.addAttribute("boardImages", boardImages);
        model.addAttribute("comment", new Comment());
        model.addAttribute("board", new Board()); // 새 게시물 작서용 객체
        model.addAttribute("commentCounts", commentCounts); // 게시물에 달린 댓글 갯수
        return "index";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }

}
