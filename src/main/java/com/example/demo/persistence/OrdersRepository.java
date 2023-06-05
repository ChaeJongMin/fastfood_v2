package com.example.demo.persistence;
import java.util.List;

import com.example.demo.persistence.QueryFor.BestSellerNameAndCnt;
import com.example.demo.persistence.QueryFor.CategoryNameAndCnt;
import com.example.demo.persistence.QueryFor.OrderSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.domain.Orders;

public interface OrdersRepository extends CrudRepository<Orders, Long>{

    /*Orders 테이블의 createDates(LocalDateTime) 컬럼에 year, month만 뽑아와 매개변수인 year, month와 같은 튜플을
      page 객체로 반환하는 쿼리 메소드 (해당 년,월에 구매한 제품)
    */
    @Query("select o from Orders o where year(o.createDates)=:year and month(o.createDates)=:month")
    Page<Orders> findByOrdersMonth(int year, int month, Pageable pageable);

    /* Orders 테이블의 createDates(LocalDateTime) 컬럼에 year, month만 뽑아와 매개변수인 year, month와 같은 튜플의
       개수를 반환하는 쿼리 메소드 (총 판매 수)
    */
    @Query("select count(o.oid) from Orders o where month(o.createDates)= :month and year(o.createDates)= :year")
    long countCurrentByMonthOrders(int month,int year);
    /*
     Orders 테이블의 createDates(LocalDateTime) 컬럼에 year, month만 뽑아와 매개변수인 year, month와 같은 튜플의 price의 총합을
     반환하는 쿼리 메소드이며 해당 년도,월에 판매한 금액이 없으면 0을 반환 (총 판매 가격)
     */
    @Query("select COALESCE(sum(o.price),0)  from Orders o where month(o.createDates)= :month and year(o.createDates)= :year")
    long sumCurrentPriceByMonthOrders(int month,int year);

    /*
     특정 월에 Product 테이블과 Order 테이블을 join(left)하여 제품 id가 같은 Product의 productName 컬럼과 구매한 횟수를 얻어오고
      구매 횟수(cnt)로 내림차순하는 쿼리메소드(베스트셀러)
     */
    @Query("select p.productName as name, count(o.product.pid) as cnt from Orders o left join Product p on p.pid=o.product.pid " +
            "where month(o.createDates)=:month and year(o.createDates)=:year group by o.product.pid order by cnt desc")
    List<BestSellerNameAndCnt> findBestSellerNameAndCnt(int month, int year);

    /*
     Categories 테이블의 카테고리 이름 튜플과 판매된 카테고리의 제품 횟수를 반환하는 쿼리메소드입니다.(카테고리별 구매한 제품 수)
     */
    @Query ("SELECT c.categoryName as name, count(o.product.pid) as cnt FROM Orders o inner join Product p on p.pid=o.product.pid " +
            "inner join Categories c on p.categories.categoryId=c.categoryId group by p.categories.categoryId")
    List<CategoryNameAndCnt> findCategoriesNameAndCnt();

    /*
     현재 월(now()) 부터 지난 12개월 간 년도.월 , 판매액을 얻어오는 쿼리 메소드입니다.
     문제점이 order 테이블의 create_dates 컬럼이 지난 12개월 년도.월에 없으면 무시합니다. 원래는 해당 년도.월, 총가격 0을 반환해야합니다.
     (현재월 ~ 12개월 전 월까지 총 판매액)
     */
    @Query( value = "select concat(year(o.create_dates),'.', month(o.create_dates)) as yearMonth,sum(o.price) as totalPrice from orders o " +
            "where o.create_dates >= date_sub(now(), interval 12 month) " +
            "group by year(o.create_dates), month(o.create_dates) order by yearMonth", nativeQuery = true)
    List<OrderSummary> getOrderSummaries();

}
