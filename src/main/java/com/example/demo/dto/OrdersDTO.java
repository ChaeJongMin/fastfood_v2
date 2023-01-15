package com.example.demo.dto;

import com.example.demo.domain.Customer;
import com.example.demo.domain.Orders;
import com.example.demo.domain.Product;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class OrdersDTO {
    private Integer oId;
    private Date orederDate;
    private String state;
    private Product product;
    private Customer customer;
    private String info;
    private Integer price;

    public OrdersDTO(Orders orderEntity){
        this.oId= orderEntity.getOId();
        this.orederDate=orderEntity.getOrederDate();
        this.state= orderEntity.getState();
        this.product= orderEntity.getProduct();
        this.customer= orderEntity.getCustomer();
        this.info= orderEntity.getInfo();
        this.price= orderEntity.getPrice();
    }
}
