package com.example.demo.dto;

import com.example.demo.domain.Categories;
import com.example.demo.domain.Product;
import com.example.demo.domain.Product_option_info;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductDTO {
    private Integer pId;
    private String productName;
    private Integer price;
    private boolean allSale=false;
    private Categories categories;
    private List<Product_option_info> infoList=new ArrayList<Product_option_info>();

    public ProductDTO(Product productEntity){
        this.pId= productEntity.getPId();
        this.productName= productEntity.getProductName();
        this.price= productEntity.getPrice();
        this.categories= productEntity.getCategories();
        this.infoList=productEntity.getInfoList();
    }
}
