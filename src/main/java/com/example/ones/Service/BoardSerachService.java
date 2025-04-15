package com.example.ones.Service;

import com.example.ones.Entity.Board;
import com.example.ones.Repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardSerachService {

    private final BoardRepository boardRepository;

    @Transactional
    public List<Board> searchBoard(String Keyword) {
        return boardRepository.findAllSearch(Keyword);
    }
}
