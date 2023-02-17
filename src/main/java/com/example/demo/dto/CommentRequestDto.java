package com.example.demo.dto;

import com.example.demo.domain.Boards;
import com.example.demo.domain.Comment;
import com.example.demo.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class CommentRequestDto {
    private String content;
    private Boards boards;
    private Customer customer;

    public Comment toCommentEntitiy(){
        return Comment.builder()
                .content(content)
                .boards(boards)
                .customer(customer)
                .build();
    }
}
