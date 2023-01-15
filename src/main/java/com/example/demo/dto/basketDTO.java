package com.example.demo.dto;

import com.example.demo.domain.Basket;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Product_option_info;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class basketDTO {
    private Integer bid;//PK
    private Integer pCount;
    private Product_option_info productinfo;
    private Customer customer;
    private String info;
    private Integer price;

    public basketDTO(Basket basketEntity){
        this.bid= basketEntity.getBid();
        this.pCount= basketEntity.getPCount();
        this.productinfo= basketEntity.getProductinfo();
        this.customer= basketEntity.getCustomer();
        this.info= basketEntity.getInfo();
        this.price= basketEntity.getPrice();
    }
}
