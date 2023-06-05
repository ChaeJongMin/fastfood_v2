package com.example.demo.dto.Response;

import com.example.demo.domain.Orders;
import lombok.Getter;

@Getter
public class OrdersResponseDto {
    //제품명
    private String productName;
    //가격
    private int price;
    //구매자
    private String userId;
    //카테고리
    private String categoryName;
    //사이즈, 온도, 세트일 경우 사이드, 디저트
    private String etc;


    public OrdersResponseDto(Orders orders,String etc){
        this.productName=orders.getProduct().getProductName();
        this.price=orders.getPrice();
        this.userId=orders.getCustomer().getUserId();
        this.categoryName=orders.getProduct().getCategories().getCategoryName();
        this.etc=etc;
    }


}
