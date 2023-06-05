package com.example.demo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;


@Getter
@ToString
@NoArgsConstructor
@Entity
public class ConnectCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;
    private long vcnt;
    // 월 패턴은 yyyy.mm 형식으로 저장
    private String currentMonth;
    @Builder
    public ConnectCustomer(String nowDate) {
        this.currentMonth=nowDate;
        this.vcnt=0;
    }

    public void increaseCnt(){
        this.vcnt++;
    }
}
