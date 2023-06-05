package com.example.demo.dto.Response;

import com.example.demo.domain.Product_option_info;
import lombok.Getter;

@Getter
public class ProductOptionResponseDto {
    private String productName;
    private String sizeName;
    private String tempName;
    private String categoryName;

    public ProductOptionResponseDto(Product_option_info entitiy){
        this.productName=entitiy.getProduct().getProductName();
        this.sizeName=entitiy.getSize().getSizename();
        this.tempName=entitiy.getTemperature().getTempname();
        this.categoryName=entitiy.getProduct().getCategories().getCategoryName();
    }

}
