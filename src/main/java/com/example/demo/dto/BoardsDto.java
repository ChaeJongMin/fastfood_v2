package com.example.demo.dto;

import com.example.demo.domain.Boards;

public class BoardsDto {
    private Long no;
    private String writer;
    private String title;
    private String content;
    private long cnt;
    private java.sql.Date createDate;


    public BoardsDto(Boards entity){
        this.no= entity.getNo();
        this.writer=entity.getWriter();
        this.title=entity.getTitle();
        this.content=entity.getContent();
        this.cnt=entity.getCnt();
        this.createDate=entity.getCreateDate();
    }
}
