package com.example.demo.dto;

import com.example.demo.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class ProductRequsetDto {
    private String productName;
    private Integer price;
    private boolean allSale=false;

    @Builder
    public ProductRequsetDto(String productName, int price, boolean allSale){
        this.productName=productName;
        this.price=price;
        this.allSale=allSale;
    }
    public Product toProductEntitiy(){
        return Product.builder()
                .productName(productName)
                .price(price)
                .allSale(allSale)
                .build();
    }
}
