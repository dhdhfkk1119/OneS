package com.example.ones.Controller;

import com.example.ones.Entity.Board;
import com.example.ones.Entity.Board_like;
import com.example.ones.Entity.Comment;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.BoardLikeRepository;
import com.example.ones.Repository.BoardRepository;
import com.example.ones.Repository.CommentRepository;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Array;
import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final BoardLikeRepository boardLikeRepository;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {

        String username = principal.getName();
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        // 자신이 작성한 게시물에 대한 API
        Map<Long,List<Comment>> commentMap = new HashMap<>(); // 게시물에 대한 댓글 가져오기
        Map<Long,List<String>> commentImagesMap = new HashMap<>(); // 댓글에 대한 이미지 가져오기
        Map<Long,List<String>> boardImagesMap = new HashMap<>(); // 게시물에 대한 이미지 가져오기
        Map<Long,Member> commentMemberMap = new HashMap<>(); // 댓글 작성자유저 정보 가져오기

        // 좋아요 section 부분
        Map<Long,Board> UserboardLikeMap = new HashMap<>();// 자신이 좋아요를 누른 게시물에 정보를 가져옴
        Map<Long, Member> UserboardMemberMap = new HashMap<>(); // 좋아요 누른 게시물에 대한 작성자 정보
        Map<Long,List<String>> likeboardImagesMap = new HashMap<>(); // 좋아요 누른 게시물에 이미지 , 구분해서 가져오기

        // 댓글 section 부분
        Map<Long,Board> UsercommentBoardMap = new HashMap<>();// 자신이 게시물에 단 댓글 게시물 가져오기
        Map<Long,Member> UsercommentMemberMap = new HashMap<>(); // 댓글은 단 게시물에 작성자를 가져오기
        Map<Long,List<String>> commentboardImagesMap = new HashMap<>(); // 댓글단 게시물에 이미지 , 구분해서 가져오기
        Map<Long,List<String>> usercommentImagesMap = new HashMap<>(); // 유저가 단 댓글에 이미지 , 구분해서 가져오기
        List<Comment> userComments = commentRepository.findByCommentUseridx(member.getIdx()); // 현재 로그인한 유저가 단 댓글을 가져옴

        List<Board_like> boardLikeList = boardLikeRepository.findByLikeUseridx(member.getIdx()); // 현재 로그인한 유저가 좋아요 누른 게시물

        // 좋아요 누른 게시물 정보 받아오기
        for (Board_like boardLike : boardLikeList) {
            boardRepository.findByBoardIdx(boardLike.getLikeBoardidx())
                    .ifPresent(board -> {
                        UserboardLikeMap.put(boardLike.getLikeBoardidx(), board);

                        Long boardWriteidx = board.getBoardUseridx();
                        memberRepository.findByIdx(boardWriteidx)
                                .ifPresent(writer ->
                                        UserboardMemberMap.put(boardLike.getLikeIdx(), writer));

                        List<String> images = new ArrayList<>();
                        if (board.getBoardImages() != null && !board.getBoardImages().isEmpty()) {
                            images = Arrays.asList(board.getBoardImages().split(","));
                        }

                        likeboardImagesMap.put(boardLike.getLikeBoardidx(), images);
                    });

        }
        // 댓글단 게시물 정보 받아오기
        for(Comment comment : userComments) {
            boardRepository.findByBoardIdx(comment.getCommentBoardidx())
                    .ifPresent(board -> {
                        UsercommentBoardMap.put(comment.getCommentIdx(), board);

                        // 게시물에 대한 작성자 정보 가져오기
                        Long boardWriteidx = board.getBoardUseridx();
                        memberRepository.findByIdx(boardWriteidx)
                                .ifPresent(writer ->
                                        UsercommentMemberMap.put(comment.getCommentIdx(), writer));

                        // 게시물 이미지 분리해서 저장
                        List<String> images = new ArrayList<>();
                        if (board.getBoardImages() != null && !board.getBoardImages().isEmpty()) {
                            images = Arrays.asList(board.getBoardImages().split(","));
                        }
                        commentboardImagesMap.put(comment.getCommentIdx(), images);
                    });

            List<String> images = new ArrayList<>();
            if (comment.getCommentImages() != null && !comment.getCommentImages().isEmpty()) {
                images = Arrays.asList(comment.getCommentImages().split(","));
            }
            usercommentImagesMap.put(comment.getCommentIdx(), images);

        }



        List<Board> board = boardRepository.findByBoardUseridx(member.getIdx());
        for(Board boardOpt : board) {
            List<Comment> comments = commentRepository.findByCommentBoardidx(boardOpt.getBoardIdx());
            commentMap.put(boardOpt.getBoardIdx(), comments);

            List<String> images = new ArrayList<>();
            if(boardOpt.getBoardImages() != null && !boardOpt.getBoardImages().isEmpty()){
                images = Arrays.asList(boardOpt.getBoardImages().split(","));
            }
            boardImagesMap.put(boardOpt.getBoardIdx(), images);

            for(Comment comment : comments) {
                List<String> commentImages = new ArrayList<>();
                if(comment.getCommentImages() != null && !comment.getCommentImages().isEmpty()){
                    commentImages = Arrays.asList(comment.getCommentImages().split(","));
                }
                commentImagesMap.put(comment.getCommentIdx(), commentImages);

                Member memberOpt = memberRepository.findByIdx(comment.getCommentUseridx())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                commentMemberMap.put(comment.getCommentIdx(), memberOpt);
            }
        }

        model.addAttribute("boardList", board); // 로그인한 유저의 게시물 가져오기
        model.addAttribute("commentList", commentMap); // 게시물에 대한 댓글 정보 가져오기
        model.addAttribute("commentImages", commentImagesMap); // 댓글에 대한 이미지 정보 가져오기
        model.addAttribute("boardImages", boardImagesMap); // 게실에 대한 이미지 정보 가져오기
        model.addAttribute("member", member); // 현재 로그인한 유저의 정보 가져오기
        model.addAttribute("commentMember", commentMemberMap); // 댓글단 유저의 정보 가져오기
        model.addAttribute("comment",new Comment()); // 댓글 작성 객체 만들기

        model.addAttribute("userLike",boardLikeList); // 유저가 좋아요 누른 목록
        model.addAttribute("userboardLike",UserboardLikeMap); // 좋아요 누른 게시물
        model.addAttribute("userComment",userComments); // 댓글 내용
        model.addAttribute("userCommentBoard",UsercommentBoardMap); // 댓글을 단 게시물
        model.addAttribute("likeboardWriter",UserboardMemberMap); // 자신이 좋아요 누른 게시물에 작성자 정보
        model.addAttribute("commentboardWriter",UsercommentMemberMap); // 자신이 댓글을 단 게시물에 작성자 정보 
        model.addAttribute("commentboardImages", commentboardImagesMap); // 댓글단 게시물에 이미지 정보
        model.addAttribute("usercommentImages", usercommentImagesMap); // 로그인 유저가 단 댓글의 이미지 정보
        model.addAttribute("likeboardImages", likeboardImagesMap); // 좋아요 누른 게시물 이미지 정보
        
        return "profile";

    }
}
