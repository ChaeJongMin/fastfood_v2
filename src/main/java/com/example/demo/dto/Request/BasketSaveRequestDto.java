package com.example.demo.dto.Request;

import com.example.demo.domain.Basket;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Product_option_info;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
public class BasketSaveRequestDto {
    private Integer pCount;
    private Product_option_info productinfo;
    private Customer customer;
    private String info;
    private Integer price;

    @Builder
    public BasketSaveRequestDto(int pCount, Product_option_info productOptionInfo, Customer customer,
                                String info, int price){
        this.pCount=pCount;
        this.productinfo=productOptionInfo;
        this.customer=customer;
        this.info=info;
        this.price=price;

    }
    public Basket toBasketEntitiy(){
        return Basket.builder()
                .pCount(pCount)
                .productOptionInfo((productinfo))
                .customer(customer)
                .info(info)
                .price(price)
                .build();
    }

}
