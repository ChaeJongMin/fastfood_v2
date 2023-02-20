package com.example.demo.Service;

import com.example.demo.domain.Boards;
import com.example.demo.domain.Customer;
import com.example.demo.dto.*;
import com.example.demo.persistence.BoardsRepository;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.attoparser.dom.Text;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardsService {
    private final BoardsRepository boardsRepository ;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public Page<BoardsDto> findAll(Pageable pageable){
        return boardsRepository.findAllDesc(pageable)
                .map(BoardsDto::new);
    }
    @Transactional(readOnly = true)
    public Page<Boards> findAllToPage(Pageable pageable){
        return boardsRepository.findAll(pageable);
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


    /*************************************************************/
    //page 처리
    public PageDto makePageDto(int currentPage, int mostEndPage){
        return PageDto.builder()
                .currentPage(currentPage)
                .mostEndPage(mostEndPage)
                .build();
    }

    /*************************************************************/
    //검색 처리
    @Transactional(readOnly = true)
    public Page<BoardsDto> boardSearchList(String keyword, Pageable pageable,String target){
        Page<BoardsDto> boardsDtoPage=null;

        if(target.equals("Title")){
            boardsDtoPage=boardsRepository.findByTitleContainingIgnoreCaseOrderByNoDesc(keyword,pageable)
                    .map(BoardsDto::new);
        }
        else if(target.equals("Content")){
            boardsDtoPage=boardsRepository.findByContentContainingIgnoreCaseOrderByNoDesc(keyword,pageable)
                    .map(BoardsDto::new);
        }
        else if(target.equals("TitleOrContent")){
            boardsDtoPage=boardsRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByNoDesc(keyword,keyword,pageable)
                    .map(BoardsDto::new);
        }
        else{
            boardsDtoPage=boardsRepository.findByWriterContainingIgnoreCaseOrderByNoDesc(keyword,pageable)
                    .map(BoardsDto::new);
        }
        return boardsDtoPage;
    }
}
