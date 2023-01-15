package com.example.demo.dto;

import com.example.demo.domain.Categories;
import com.example.demo.domain.Product;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoriesDTO {
    private Integer categoryId;
    private String categoryName;
    private List<Product> productList = new ArrayList<Product>();

    public CategoriesDTO(Categories categoryEntity){
        this.categoryId=categoryEntity.getCategoryId();
        this.categoryName=categoryEntity.getCategoryName();
        this.productList=categoryEntity.getProductList();
    }
}
