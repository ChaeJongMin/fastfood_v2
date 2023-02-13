package com.example.demo.Service;

import com.example.demo.domain.Boards;
import com.example.demo.domain.Customer;
import com.example.demo.dto.BoardUpdateRequestDto;
import com.example.demo.dto.BoardsDto;
import com.example.demo.dto.BoardsRequestDto;
import com.example.demo.dto.CustomerDto;
import com.example.demo.persistence.BoardsRepository;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardsService {
    private final BoardsRepository boardsRepository ;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<BoardsDto> findAll(){
        return boardsRepository.findAllDesc().stream()
                .map(BoardsDto::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public Long save(BoardsRequestDto boardsRequestDto){
        Customer customer=customerRepository.findByUserId(boardsRequestDto.getWriter()).get(0);
        Long result=boardsRepository.save(boardsRequestDto.toBoardEntity(customer)).getNo();
        System.out.println("board서비스 save함수 반환값: "+result);
        return result;
    }
    @Transactional(readOnly = true)
    public BoardsDto findById(long id){
        BoardsDto boardsDto=new BoardsDto(boardsRepository.findById(id).get());
        return boardsDto;
    }
    @Transactional
    public Long update(Long id,BoardUpdateRequestDto boardUpdateDto){
            Boards boards=boardsRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
            boards.updates(boardUpdateDto.getTitle(), boardUpdateDto.getContent());
            return id;
    }
    @Transactional
    public void delete (Long id) {
       Boards boards = boardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        boardsRepository.delete(boards);
    }

    @Transactional
    public void viewsCount(Long id){
        Boards boards=boardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
        boards.increaseViews();

    }
}
