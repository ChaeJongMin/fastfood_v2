package com.example.demo.dto.Response;

import com.example.demo.domain.Boards;
import com.example.demo.domain.Comment;
import com.example.demo.domain.Customer;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long no;
    private String content;
    private Long boardId;
    private String writer;
    private LocalDateTime createDate;
    public CommentResponseDto(Comment entitiy){
        this.no=entitiy.getNo();
        this.writer=entitiy.getCustomer().getUserId();
        this.content=entitiy.getContent();
        this.boardId=entitiy.getBoards().getNo();
        this.createDate=entitiy.getCreateDates();
    }
}
