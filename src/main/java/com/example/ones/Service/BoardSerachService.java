package com.example.ones.Service;

import com.example.ones.Entity.Board;
import com.example.ones.Repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardSerachService {

    private final BoardRepository boardRepository;

    
    // 검색 및 정렬 기능
    @Transactional
    public List<Board> searchBoard(String keyword, String sort) {
        Sort sortOption;

        switch (sort) {
            case "latest":
                sortOption = Sort.by(Sort.Direction.DESC, "boardIdx");
                break;
            case "user":
                sortOption = Sort.by(Sort.Direction.DESC, "boardUseridx"); // 사용자 idx 기준
                break;
            case "viewst":
            default:
                sortOption = Sort.by(Sort.Direction.DESC, "boardView");
                break;
        }

        return boardRepository.findByBoardContentContainingIgnoreCase(keyword, sortOption);
    }

}
