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
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public String uploadURL = "C:/Project/OneS/src/main/resources/static/profile-images";

    public Board registerBoard(Board board, Long useridx , MultipartFile[] files) throws IOException {
        List<String> fileNames = new ArrayList<>();

        for (MultipartFile file : files) {
            if(file.getSize() <= 0){
                throw new IOException("파일이 비어 있거나 크기가 0입니다");
            }

            String OriginalfileName = file.getOriginalFilename();
            String fileName = OriginalfileName.substring(OriginalfileName.lastIndexOf("."));
            String uuid = UUID.randomUUID().toString();
            String newFileName = uuid + "_" + fileName;
            Path savePath = Paths.get(uploadURL, newFileName);

            file.transferTo(savePath.toFile());
            fileNames.add(newFileName);
        }

        board.setBoardContent(board.getBoardContent());
        board.setBoardUseridx(useridx);
        board.setBoardImages(fileNames.isEmpty() ? "" : String.join(",", fileNames));
        board.setBoardComent(0L);
        board.setBoardLike(0L);
        board.setBoardAt(LocalDateTime.now());
        return boardRepository.save(board);
    }
}
