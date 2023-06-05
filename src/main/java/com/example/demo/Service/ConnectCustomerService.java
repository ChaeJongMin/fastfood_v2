package com.example.demo.Service;

import com.example.demo.domain.ConnectCustomer;
import com.example.demo.persistence.ConnectCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ConnectCustomerService {
    private final ConnectCustomerRepository connectCustomerRepository;
    @Transactional
    public void increaseCurrentMonthCnt(){
        //날짜 추출
        LocalDate now = LocalDate.now();
        //년도.월 순으로 추출
        String nowDate=now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        if(!connectCustomerRepository.existsByCurrentMonth(nowDate)){
            connectCustomerRepository.save(ConnectCustomer.builder()
                            .nowDate(nowDate)
                    .build());
        }
        //객체 얻어와서 cnt 증가 메소드 호출
        ConnectCustomer connectCustomer=connectCustomerRepository.findByCurrentMonth(nowDate).orElseThrow(
                ()-> new IllegalArgumentException("해당 사용자가 없습니다."));
        connectCustomer.increaseCnt();

    }
    @Transactional(readOnly = true)
    public long sumPastUser(){
        return connectCustomerRepository.getPastUser();
    }
}
