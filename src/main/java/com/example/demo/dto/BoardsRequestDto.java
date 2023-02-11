package com.example.demo.dto;

import com.example.demo.domain.Boards;
import com.example.demo.domain.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
@NoArgsConstructor
public class BoardsRequestDto {
    private String writer;

    private String title;

    private String content;

    private long cnt;

    @Builder
    public BoardsRequestDto(String writer, String title, String content, Long cnt){
        this.writer=writer;
        this.title=title;
        this.content=content;
        this.cnt=cnt;
    }
    public Boards toBoardEntity(){
        return Boards.builder()
                .writer(writer)
                .title(title)
                .content(content)
                .cnt(cnt)
                .build();
    }
}
