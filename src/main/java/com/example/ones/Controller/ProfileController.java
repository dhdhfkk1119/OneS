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
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/profile/{memberIdx}")
    public String profile(Model model, @PathVariable Long memberIdx, Principal principal) {

        // 현재 로그인한 유저의 정보를 가져오기
        Member memberlogin = null;
        if (principal != null) {
            String username = principal.getName();
            memberlogin = memberRepository.findByUserId(username)
                    .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다"));
        }

        // 게시물 작성한 사람의 유저 정보
        Member member = memberRepository.findByIdx(memberIdx)
                    .orElseThrow(() -> new RuntimeException("User not found"));



        Map<Long, Long> commentCounts = new HashMap<>(); // 게시물에 달린 댓글 갯수

        // 자신이 작성한 게시물에 대한 API
        Map<Long,List<Comment>> commentMap = new HashMap<>(); // 게시물에 대한 댓글 가져오기
        Map<Long,List<String>> commentImagesMap = new HashMap<>(); // 댓글에 대한 이미지 가져오기
        Map<Long,List<String>> boardImagesMap = new HashMap<>(); // 게시물에 대한 이미지 가져오기
        Map<Long,Member> commentMemberMap = new HashMap<>(); // 댓글 작성자유저 정보 가져오기


        // 좋아요 section 부분
        Map<Long,Board> UserboardLikeMap = new HashMap<>();// 자신이 좋아요를 누른 게시물에 정보를 가져옴
        Map<Long, Member> UserboardMemberMap = new HashMap<>(); // 좋아요 누른 게시물에 대한 작성자 정보
        Map<Long,List<String>> likeboardImagesMap = new HashMap<>(); // 좋아요 누른 게시물에 이미지 , 구분해서 가져오기
        Map<Long,List<Comment>> likeboardCommentMap = new HashMap<>(); // 좋아요 누른 게시물에 댓글 가져오기
        Map<Long,List<String>> likeboardCommentImagesMap = new HashMap<>(); // 좋아요 누른 게시물에 댓글 이미지 가져오기
        Map<Long,Member> likeboardCommentMemberMap = new HashMap<>(); // 좋아요 누른 게시물에 댓글 작성자 가져오기

        // 댓글 section 부분
        Map<Long,Board> UsercommentBoardMap = new HashMap<>();// 자신이 게시물에 단 댓글 게시물 가져오기
        Map<Long,Member> UsercommentMemberMap = new HashMap<>(); // 댓글은 단 게시물에 작성자를 가져오기
        Map<Long,List<String>> commentboardImagesMap = new HashMap<>(); // 댓글단 게시물에 이미지 , 구분해서 가져오기
        Map<Long,List<String>> usercommentImagesMap = new HashMap<>(); // 유저가 단 댓글에 이미지 , 구분해서 가져오기
        List<Comment> userComments = commentRepository.findByCommentUseridx(member.getIdx()); // 현재 로그인한 유저가 단 댓글을 가져옴
        List<Board_like> boardLikeList = boardLikeRepository.findByLikeUseridx(member.getIdx()); // 현재 로그인한 유저가 좋아요 누른 게시물

        Map<Board, List<Comment>> userCommentsGroupedByBoard = new LinkedHashMap<>(); // 댓글 단 게시물에 중복 리스트 하나로 합치기 (그룹화)

        // 좋아요 누른 게시물 정보 받아오기
        for (Board_like boardLike : boardLikeList) {
            boardRepository.findByBoardIdx(boardLike.getLikeBoardidx())
                    .ifPresent(board -> {
                        UserboardLikeMap.put(boardLike.getLikeBoardidx(), board);

                        Long boardWriteidx = board.getBoardUseridx();
                        memberRepository.findByIdx(boardWriteidx)
                                .ifPresent(writer ->
                                        UserboardMemberMap.put(boardLike.getLikeIdx(), writer));

                        Long CommentCount = commentRepository.countByCommentBoardidx(board.getBoardIdx());
                        commentCounts.put(board.getBoardIdx(), CommentCount);

                        List<String> images = new ArrayList<>();
                        if (board.getBoardImages() != null && !board.getBoardImages().isEmpty()) {
                            images = Arrays.asList(board.getBoardImages().split(","));
                        }

                        likeboardImagesMap.put(boardLike.getLikeBoardidx(), images);

                        List<Comment> comments = commentRepository.findByCommentBoardidx(board.getBoardIdx());
                        likeboardCommentMap.put(boardLike.getLikeBoardidx(), comments);


                        for (Comment comment : comments){
                            List<String> commentimages = new ArrayList<>();
                            if (comment.getCommentImages() != null && !comment.getCommentImages().isEmpty()) {
                                commentimages = Arrays.asList(comment.getCommentImages().split(","));
                            }
                            likeboardCommentImagesMap.put(comment.getCommentIdx(), commentimages);

                            Member member1 = memberRepository.findByIdx(comment.getCommentUseridx()).orElseThrow(() -> new RuntimeException("User not found"));
                            likeboardCommentMemberMap.put(comment.getCommentIdx(), member1);
                        }

                    });

        }
        
        // 댓글단 게시물 정보 받아오기
        for(Comment comment : userComments) {
            boardRepository.findByBoardIdx(comment.getCommentBoardidx())
                    .ifPresent(board -> {
                        // 이미 board가 있으면 기존 리스트에 추가, 없으면 새 리스트 생성
                        userCommentsGroupedByBoard
                                .computeIfAbsent(board, k -> new ArrayList<>())
                                .add(comment);

                        // 댓글단 게시물에 게시물 댓글 수
                        Long CommentCount = commentRepository.countByCommentBoardidx(board.getBoardIdx());
                        commentCounts.put(board.getBoardIdx(), CommentCount);

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


        // 현재 로그인한 유저의 게시물 가져오기
        List<Board> board = boardRepository.findByBoardUseridx(member.getIdx());
        for(Board boardOpt : board) {
            Long CommentCount = commentRepository.countByCommentBoardidx(boardOpt.getBoardIdx());
            commentCounts.put(boardOpt.getBoardIdx(), CommentCount);


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
        model.addAttribute("member", member); // 게시물을 작성한 유저의 정보를 가져오기 
        model.addAttribute("loginMember",memberlogin); // 현재 로그인안 유저의 정보
        model.addAttribute("commentMember", commentMemberMap); // 댓글단 유저의 정보 가져오기
        model.addAttribute("comment",new Comment()); // 댓글 작성 객체 만들기
        model.addAttribute("board",new Board()); // 게시글 작성 객체 만들기

        model.addAttribute("userLike",boardLikeList); // 유저가 좋아요 누른 목록
        model.addAttribute("userboardLike",UserboardLikeMap); // 좋아요 누른 게시물
        model.addAttribute("userComment",userComments); // 댓글 내용
        model.addAttribute("userCommentBoard",UsercommentBoardMap); // 댓글을 단 게시물
        model.addAttribute("likeboardWriter",UserboardMemberMap); // 자신이 좋아요 누른 게시물에 작성자 정보
        model.addAttribute("commentboardWriter",UsercommentMemberMap); // 자신이 댓글을 단 게시물에 작성자 정보 
        model.addAttribute("commentboardImages", commentboardImagesMap); // 댓글단 게시물에 이미지 정보
        model.addAttribute("usercommentImages", usercommentImagesMap); // 자신이 단 댓글의 이미지 정보
        model.addAttribute("likeboardImages", likeboardImagesMap); // 좋아요 누른 게시물 이미지 정보
        model.addAttribute("userCommentMap", userCommentsGroupedByBoard); // 댓글단 게시물의 정보 가져오기(반복이 있으면 하나만 그룹화)

        model.addAttribute("likeboardComment",likeboardCommentMap); // 자신이 좋아요 누른 게시물의 댓글을 가져오기
        model.addAttribute("likeboardCommentImages",likeboardCommentImagesMap); // 자신이 누른 좋아아요 게시물의 이미지 , 구분하기
        model.addAttribute("likeboardCommentMember",likeboardCommentMemberMap); // 자신이 누른 좋아요의 게시물에 댓글에 작성자를 가져오기

        model.addAttribute("commentCounts", commentCounts); // 게시물에 달린 댓글 갯수
        return "profile";

    }
}
