package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Comment extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name="board_id",nullable = false)
    private Boards boards;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private Customer customer;

    @Builder
    public Comment(String content, Boards boards, Customer customer){
        this.content=content;
        this.boards=boards;
        this.customer=customer;
    }

    public void update(String content){
        this.content=content;
    }
}
