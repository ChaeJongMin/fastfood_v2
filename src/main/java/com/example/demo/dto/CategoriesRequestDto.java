package com.example.demo.dto;

import com.example.demo.domain.Categories;
import com.example.demo.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CategoriesRequestDto {
    private String categoryName;

    @Builder
    public CategoriesRequestDto(String categoryName){
        this.categoryName=categoryName;
    }

    public Categories toCategoriesEntitiy(){
        return Categories.builder()
                .categoryName(categoryName)
                .build();
    }
}
