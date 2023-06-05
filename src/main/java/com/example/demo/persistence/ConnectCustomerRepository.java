package com.example.demo.persistence;

import com.example.demo.domain.ConnectCustomer;
import com.example.demo.persistence.QueryFor.VisitorSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectCustomerRepository extends CrudRepository<ConnectCustomer, Integer> {
    public Optional<ConnectCustomer> findByCurrentMonth(String dateFormat);
    public boolean existsByCurrentMonth(String dateFormat);

    /*
    connect_customer 테이블에서 current_month(날짜형태 문자열)를 date 타입으로 만들어 지난 2개월 ~ 지난 9개월 비교하는 쿼리메소드
    (지난 2개월 ~ 지난 9개월까지 방문자 수)
     */
    @Query(value="SELECT sum(vcnt) as total FROM connect_customer" +
            " WHERE STR_TO_DATE(current_month, '%Y%m') >= DATE_SUB(CURDATE(), INTERVAL 9 MONTH)" +
            "AND STR_TO_DATE(current_month, '%Y%m') < DATE_SUB(CURDATE(), INTERVAL 2 MONTH)", nativeQuery = true )
    public long getPastUser();

    /*
    connect_customer 테이블에서 년도.월 기준으로 정렬 및 그룹화하여 년도.월과 방문자수를 얻어오는 쿼리 메소드
    (이번 달 기준으로 전 9개월 까지의 년도.월, 방문자 수)
     */
    @Query( value = "select CONCAT(YEAR(STR_TO_DATE(c.current_month, '%Y%m')), '.', MONTH(STR_TO_DATE(c.current_month, '%Y%m'))) AS yearMonth," +
            "sum(c.vcnt) AS totalNum " +
            "from connect_customer c  " +
            "WHERE STR_TO_DATE(c.current_month, '%Y%m') >= DATE_SUB(CURDATE(), INTERVAL 9 MONTH) " +
            "GROUP BY YEAR(STR_TO_DATE(c.current_month, '%Y%m')), MONTH(STR_TO_DATE(c.current_month, '%Y%m'))" +
            "order by YEAR(STR_TO_DATE(c.current_month, '%Y%m')), MONTH(STR_TO_DATE(c.current_month, '%Y%m')); ", nativeQuery = true)
    List<VisitorSummary> getVisitorSummaries();
}
