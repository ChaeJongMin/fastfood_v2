package com.example.demo.dto;

import com.example.demo.domain.Boards;
import com.example.demo.domain.Customer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardsDto {
    private Long no;
    private String title;
    private String content;
    private long cnt;
    private LocalDateTime createDate;
    private String userId;
    private List<CommentResponseDto> comments;
    public BoardsDto(Boards entity){
        this.no= entity.getNo();
        this.title=entity.getTitle();
        this.content=entity.getContent();
        this.cnt=entity.getCnt();
        this.createDate=entity.getCreateDates();
        this.userId=entity.getCustomer().getUserId();
        this.comments=entity.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
}
