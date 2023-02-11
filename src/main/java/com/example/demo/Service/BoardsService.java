package com.example.demo.Service;

import com.example.demo.domain.Boards;
import com.example.demo.dto.BoardsDto;
import com.example.demo.dto.BoardsRequestDto;
import com.example.demo.persistence.BoardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardsService {
    private final BoardsRepository boardsRepository ;

    @Transactional(readOnly = true)
    public List<BoardsDto> findAll(){
        return boardsRepository.findAll().stream()
                .map(BoardsDto::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public Long save(BoardsRequestDto boardsRequestDto){
        return boardsRepository.save(boardsRequestDto.toBoardEntity()).getNo();
    }

}
