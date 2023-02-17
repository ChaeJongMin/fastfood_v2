package com.example.demo.Service;

import com.example.demo.domain.Boards;
import com.example.demo.domain.Comment;
import com.example.demo.domain.Customer;
import com.example.demo.dto.CommentRequestDto;
import com.example.demo.dto.CommentUpdateRequestDto;
import com.example.demo.persistence.BoardsRepository;
import com.example.demo.persistence.CommentRepository;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CustomerRepository customerRepository;
    private final BoardsRepository boardsRepository;
    @Transactional
    public Long save(CommentRequestDto requestDto, String userId, long id){

        Customer customer=customerRepository.findByUserId(userId).get(0);
        Boards boards=boardsRepository.findById(id).orElseThrow(()->
                    new IllegalArgumentException("해당 게시글은 존재 X!!")
                );
        requestDto.setCustomer(customer);
        requestDto.setBoards(boards);
        return commentRepository.save(requestDto.toCommentEntitiy()).getNo();
    }

    @Transactional
    public Long update(Long id,CommentUpdateRequestDto commentUpdateRequestDto){
        Comment comment=commentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글은 없습니다."));
        comment.update(commentUpdateRequestDto.getContent());
        return id;
    }
    @Transactional
    public void delete (Long id) {
        Comment comment=commentRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 댓글은 없습니다."));
        commentRepository.delete(comment);
    }
}
