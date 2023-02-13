package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Boards extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(updatable = false)
    private String writer;
    @Column(length = 500 ,nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    private long cnt=0;
    @ManyToOne
    @JoinColumn(name="user",nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "boards",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    public void setCustomer(Customer customer){
        System.out.println("--------------------------------------------");
        if(customer==null)
            System.out.println("setCustomer 함수에서 객체는 널입니다.");
        else
            System.out.println("가져온 값: "+customer.getUserId());
        if(this.customer!=null)
            this.customer.getBoardList().remove(this);
        this.customer=customer;
        customer.getBoardList().add(this);
    }
    @Builder
    public Boards(String writer, String title, String content, long cnt
     ,Customer customer ,List<Comment> commentList ){
        System.out.println("--------------------------------------------");
        System.out.println("board 엔티티 빌드 실행");
        this.writer=writer;
        this.title=title;
        this.content=content;
        this.cnt=cnt;
        this.setCustomer(customer);
        this.comments=commentList;
    }

    public void updates(String title, String content){
        this.title=title;
        this.content=content;
    }

    public void increaseViews(){
        this.cnt++;
    }
}
