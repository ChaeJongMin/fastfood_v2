package com.example.demo.Service;

import com.example.demo.dto.Response.ManageReponseDto;
import com.example.demo.persistence.BoardsRepository;
import com.example.demo.persistence.OrdersRepository;
import com.example.demo.persistence.ConnectCustomerRepository;
import com.example.demo.persistence.QueryFor.BestSellerNameAndCnt;
import com.example.demo.persistence.QueryFor.CategoryNameAndCnt;
import com.example.demo.persistence.QueryFor.OrderSummary;
import com.example.demo.persistence.QueryFor.VisitorSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManageService {
    private final OrdersRepository ordersRepository;
    private final BoardsRepository boardsRepository;
    private final ConnectCustomerRepository connectCustomerRepository;

    /* 어드민 메인페이지의 최상단의 4가지 박스(유저 수, 판매한 제품 수, 판매 총가격, 게시판 수 (이번 달) )와
	   저번달 유저의 수와 2개월 전~9개월 전까지 유저의 수를 구하는 실제 메소드
    */
    public ManageReponseDto setData(){
        int[] splitDate=splitDateToYearAndMonth();
        int year=splitDate[0], month=splitDate[1];
        log.info("setData "+year+" "+month);
        //이번 달 기준으로 작성된 게시글 수를 구하는 메소드
        long boardCount=boardsRepository.countCurrentMonthBorads(month,year);
        //이번달 기준으로 판매한 제품 수를 구하는 메소드
        long orderCount=ordersRepository.countCurrentByMonthOrders(month,year);
        //이번달 기준으로 판매액을 구하는 쿼리 메소드
        long totalSalePrice=ordersRepository.sumCurrentPriceByMonthOrders(month,year);
        //이번 달 기준으로 현재 로그인한 유저 수를 구하는 메소드
        long userCount=connectCustomerRepository.findByCurrentMonth(convertDate(true)).orElseThrow(()->
                new IllegalArgumentException("해당 월 카운트는 없습니다.")).getVcnt();
        //이번 달 기준으로 저번 달에 로그인한 유저 수를 구하는 메소드
        long yesterdayUser=connectCustomerRepository.findByCurrentMonth(convertDate(false)).orElseThrow(()->
                new IllegalArgumentException("해당 월 카운트는 없습니다.")).getVcnt();
        //이번 달 기준으로 2개월 전 ~ 9개월 전까지 로그인한 총 유저 수를 구하는 메소드
        long pastUser=connectCustomerRepository.getPastUser();

        return new ManageReponseDto(boardCount,orderCount,totalSalePrice,userCount,yesterdayUser,pastUser);
    }

    //년.월을 문자열로 만들어 반환해주는 메소드
    public String convertDate(boolean flag){
        LocalDate now = LocalDate.now();
        //flag가 false일 시 저번 달을 구합니다.
        if(!flag)
            now=now.minusMonths(1);
        //날짜를 년도.월 패턴으로 변경
        String nowDate=now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        log.info("convertDate: "+nowDate);
        return nowDate;
    }
    //가져온 날짜를 년도, 월만 추출하는 메소드
    public int[] splitDateToYearAndMonth(){
        LocalDate now = LocalDate.now();
        int year=now.getYear();
        int month=now.getMonthValue();
        return new int[]{year,month};
    }

    //현재 년도.월 기준으로 지난 12개월 간 년도.월과 판매 총액을 반환하는 메소드
    public List<OrderSummary> getOrderSummary(){
        return ordersRepository.getOrderSummaries();
    }

    //현재 년도.월 기준으로 지난 9개월 간 년도.월과 방문한 유저 수를 반환하는 메소드
    public List<VisitorSummary> getVisitorSummary(){
            return connectCustomerRepository.getVisitorSummaries();
        }

    //카테고리별(버거, 음료수, ....) 판매한 제품 수를 구하는 메소드
    public List<CategoryNameAndCnt> getCategoryNameAndCnt(){
        return ordersRepository.findCategoriesNameAndCnt();
    }

    //가장 많이 팔린 제품 수를 구하는 메소드 (최대 7개)
    public List<BestSellerNameAndCnt> getBestSellerNameAndCnt(){
        int[] splitDate=splitDateToYearAndMonth();
        int year=splitDate[0], month=splitDate[1];
        return ordersRepository.findBestSellerNameAndCnt(month,year);
    }
}
