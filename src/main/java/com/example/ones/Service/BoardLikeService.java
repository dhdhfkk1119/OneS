package com.example.ones.Service;

import com.example.ones.Entity.Board;
import com.example.ones.Entity.Board_like;
import com.example.ones.Repository.BoardLikeRepository;
import com.example.ones.Repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;

    // 현재 찜하기 상태 체크
    public boolean isBoardLiked(Long boardidx , Long useridx) {
        Optional<Board_like> existingBoardLike = boardLikeRepository.findByLikeBoardidxAndLikeUseridx(boardidx,useridx);
        return existingBoardLike.isPresent();
    }

    // 찜하기를 누르면 실제로 저장 되는 것
    @Transactional
    public boolean addLike(Long boardidx,Long useridx) {

        Board board = boardRepository.findByBoardIdx(boardidx)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Optional<Board_like> existingLike = boardLikeRepository.findByLikeBoardidxAndLikeUseridx(boardidx, useridx);

        if (existingLike.isPresent()) {
            // 이미 좋아요를 눌렀으면 취소
            board.setBoardLike(board.getBoardLike() - 1); // 좋아요 수 감소
            boardRepository.save(board);

            boardLikeRepository.delete(existingLike.get()); // 좋아요 삭제
            return false; // 좋아요 취소 상태 반환
        } else {
            // 좋아요 추가
            board.setBoardLike(board.getBoardLike() + 1); // 좋아요 수 증가
            board.setBoardView(board.getBoardView() + 1);
            boardRepository.save(board);

            Board_like boardLike = new Board_like(); // ✅ 객체 생성이 빠졌음
            boardLike.setLikeBoardidx(boardidx);
            boardLike.setLikeUseridx(useridx);
            boardLike.setLikeAt(LocalDateTime.now());

            boardLikeRepository.save(boardLike);
            return true; // 좋아요 추가 상태 반환
        }
    }

}
