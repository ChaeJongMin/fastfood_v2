package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(updatable = false)
    private String writer;
    @Column(length = 500 ,nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(updatable = false, insertable = false, columnDefinition = "DATE DEFAULT (CURRENT_DATE)")
    private java.sql.Date createDate;

    private long cnt=0;

    @ManyToOne
    @JoinColumn(name="user",nullable = false)
    private Customer customer;

    @Builder
    public Boards(String writer, String title, String content, java.sql.Date createDate, long cnt
     ,Customer customer){
        this.writer=writer;
        this.title=title;
        this.content=content;
        this.cnt=cnt;
    }
}
