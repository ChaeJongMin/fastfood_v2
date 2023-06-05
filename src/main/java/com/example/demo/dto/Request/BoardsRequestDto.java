package com.example.demo.dto.Request;

import com.example.demo.domain.Boards;
//import com.example.demo.domain.Comment;
import com.example.demo.domain.Comment;
import com.example.demo.domain.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardsRequestDto{
    private String writer;

    private String title;

    private String content;

    private long cnt;
    private Customer customer;
    private List<Comment> commentList;
    @Builder
    public BoardsRequestDto(String writer, String title, String content, Long cnt){
        this.writer=writer;
        this.title=title;
        this.content=content;
        this.cnt=cnt;
    }
    public Boards toBoardEntity(Customer customer){

        return Boards.builder()
                .writer(writer)
                .title(title)
                .cnt(cnt)
                .content(content)
                .customer(customer)
                .commentList(new ArrayList<>())
                .build();
    }
}
