package com.example.demo.dto.Response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ManageReponseDto {
    private long userCount;
    private long orderCount;
    private long totalSalePrice;
    private long boardCount;
    private long yesterdayUser;
    private long postUser;
    //변수명 변경 필요
    public ManageReponseDto(long boardCount, long orderCount,
                            long totalSalePrice, long userCount
                            ,long yesterdayUser, long postUser){
        this.boardCount=boardCount;
        this.orderCount=orderCount;
        this.totalSalePrice=totalSalePrice;
        this.userCount=userCount;
        this.yesterdayUser=yesterdayUser;
        this.postUser=postUser;
    }
}
