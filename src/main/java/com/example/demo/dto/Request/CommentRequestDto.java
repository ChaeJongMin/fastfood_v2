package com.example.demo.dto.Request;

import com.example.demo.domain.Boards;
import com.example.demo.domain.Comment;
import com.example.demo.domain.Customer;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
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
