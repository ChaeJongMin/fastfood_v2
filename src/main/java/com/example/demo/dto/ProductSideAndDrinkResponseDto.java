package com.example.demo.dto;

import lombok.Getter;

@Getter
public class ProductSideAndDrinkResponseDto {
    private String sideName;
    private String drinkName;

    public ProductSideAndDrinkResponseDto(String sideName, String drinkName){
        this.drinkName=drinkName;
        this.sideName=sideName;
    }
}
