package com.example.demo.dto.Response;

import com.example.demo.domain.Boards;
import com.example.demo.dto.Response.CommentResponseDto;
import lombok.Getter;

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
