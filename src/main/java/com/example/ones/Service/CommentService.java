package com.example.ones.Service;

import com.example.ones.Entity.Board;
import com.example.ones.Entity.Comment;
import com.example.ones.Entity.Member;
import com.example.ones.Repository.BoardRepository;
import com.example.ones.Repository.CommentRepository;
import com.example.ones.Repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Value("${file.upload-comment}")
    public String uploadURL;

    @Transactional
    public Comment registerComment(Comment comment, MultipartFile[] files, Long boardidx, Long useridx) throws IOException {
        List<String> fileNames = new ArrayList<>();

        Optional<Board> optionalBoard = boardRepository.findByBoardIdx(boardidx);

        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get(); // 실제 Board 객체 꺼내기
            board.setBoardView(board.getBoardView() + 1);
            boardRepository.save(board); // 저장
        }

        // files가 null이 아니고, 최소한 하나 이상의 파일이 있을 때만 실행
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) { // 파일이 비어 있지 않을 때만 처리
                    String originalFileName = file.getOriginalFilename();
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                    String uuid = UUID.randomUUID().toString();
                    String newFileName = uuid + "_" + fileExtension;
                    Path savePath = Paths.get(uploadURL, newFileName);

                    file.transferTo(savePath.toFile());
                    fileNames.add(newFileName);
                }
            }
        }
        comment.setCommentContent(comment.getCommentContent());
        comment.setCommentImages(fileNames.isEmpty() ? null : String.join(",", fileNames));
        comment.setCommentAt(LocalDateTime.now());
        comment.setCommentUseridx(useridx);
        comment.setCommentBoardidx(boardidx);
        comment.setCommentLike(0L);
        comment.setCommentRelay(0L);
        comment.setCommentView(0L);

        return commentRepository.save(comment);
    }
}
