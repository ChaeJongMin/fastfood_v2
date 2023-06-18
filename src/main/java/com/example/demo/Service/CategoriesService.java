package com.example.demo.Service;

import com.example.demo.domain.Categories;
import com.example.demo.domain.Product;
import com.example.demo.dto.Response.ProductResponseDto;
import com.example.demo.persistence.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoriesRepository categoriesRepo;

//    public List<ProductResponseDto> getCategoriesInProductList(int id){
//        List<ProductResponseDto> productList=new ArrayList<>();
//        Categories categories=categoriesRepo.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리의 제품은 없습니다."));
//        for(Product product:categories.getProductList()){
//            productList.add(new ProductResponseDto(product));
//        }
//        return productList;
//    }
//
//    public List<Product> getFindSoda(){
//        Categories categories=categoriesRepo.findByCategoryName("탄산").get(0);
//        return categories.getProductList();
//    }
//    public List<Product> getFindDessertAndSide(){
//        Categories categories=categoriesRepo.findbyDrinkMenu("사이드","디저트").get(0);
//        return categories.getProductList();
//    }

    public String findCateName(int id){
        return categoriesRepo.findById(id).get().getCategoryName();
    }

    public List<String> findCateNameList(){
        return categoriesRepo.findByCateNameList();
    }
}
