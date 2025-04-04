package com.example.ones.Service;

import com.example.ones.Entity.Board;
import com.example.ones.Repository.BoardRepository;
import com.example.ones.Repository.MemberRepository;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public String uploadURL = "C:/Project/OneS/src/main/resources/static/board-images";

    // 게시물 등록하기 index 에서
    public Board registerBoard(Board board, Long useridx, MultipartFile[] files) throws IOException {
        List<String> fileNames = new ArrayList<>();

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

        board.setBoardUseridx(useridx);
        board.setBoardContent(board.getBoardContent());
        board.setBoardImages(fileNames.isEmpty() ? null : String.join(",", fileNames));
        board.setBoardComent(0L);
        board.setBoardLike(0L);
        board.setBoardAt(LocalDateTime.now());
        board.setBoardView(0L);

        return boardRepository.save(board);
    }

}
