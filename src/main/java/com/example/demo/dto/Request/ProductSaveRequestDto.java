package com.example.demo.dto.Request;

import com.example.demo.domain.Categories;
import com.example.demo.domain.Product;
import com.example.demo.domain.ProductImage;
import com.example.demo.domain.Product_option_info;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductSaveRequestDto {
    //이미지 파일 기본 경로
    public static final String BASE_PATH = "D:\\Project\\fastfood_v2\\src\\main\\resources\\static\\img\\";

    private String name;
    private int price;
    //품절 여부(or 판매 여부)
    private boolean allSale;
    private String cateName;

    @Builder
    public ProductSaveRequestDto(String name, int price, String cateName, boolean allSale){
        this.name=name;
        this.price=price;
        this.cateName=cateName;
        this.allSale=allSale;
    }
    // categories 객체를 받아 product 객체를 생성하는 메소드
    public Product toProductEntity(Categories categories){
        return Product.builder()
                .productName(name)
                .price(price)
                .allSale(allSale)
                .categories(categories)
                .infoList(new ArrayList<>())
                .build();
    }
    // product 객체를 받아 productImage 객체를 생성하는 메소드
    public ProductImage toProductImageEntity(Product product){
        return ProductImage.builder()
                .product(product)
                .path(BASE_PATH+name+".jpg")
                .name(name)
                .build();
    }

}
