package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PageDto {
    int startPage, endPage, mostEndPage,currentPage;
    boolean prev, next;
    @Builder
    public PageDto(int currentPage, int mostEndPage, boolean prev, boolean next){
        this.currentPage=currentPage;
        this.mostEndPage=mostEndPage;
        this.endPage=initEndPage(currentPage,mostEndPage);
        this.startPage=Math.max(currentPage-4, 1);
        this.prev=currentPage>5;
        this.next=endPage<mostEndPage;
    }

    @Override
    public String toString(){
        return "현재페이지: "+currentPage+" 제일마지막 페이지: "+ mostEndPage+" 시작페이지: "+startPage+" 끝페이지: "+endPage
                +"이전 버튼: "+prev+" 다음버튼: "+next;
    }

    public int initEndPage(int currentPage, int mostEndPage){
        if(mostEndPage==0)
            return 1;
        int result=Math.min(currentPage+5,mostEndPage);
        if(result<10 && mostEndPage>=10){
            result=10;
        }
        return result;
    }
}
