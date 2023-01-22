package com.example.demo.dto;

import com.example.demo.domain.Product;
import com.example.demo.domain.Product_option_info;
import com.example.demo.domain.Size;
import com.example.demo.domain.Temperature;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductOptionInfoRequestDto {
    private Product product;
    private Size size;
    private Temperature temperature;
    private boolean isUse=true; //옵션 사용 여부
    private Integer price;

    @Builder
    public ProductOptionInfoRequestDto(Product product, Size size, Temperature temperature,
                                       boolean isUse, int price){
        this.product=product;
        this.size=size;
        this.temperature=temperature;
        this.isUse=isUse;
        this.price=price;
    }
    public Product_option_info productOptionInfoEntity(){
        return Product_option_info.builder()
                .product(product)
                .size(size)
                .temperature(temperature)
                .isUse(isUse)
                .build();
    }
}
