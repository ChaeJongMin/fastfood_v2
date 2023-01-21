package com.example.demo.Service;

import com.example.demo.domain.Categories;
import com.example.demo.domain.Product;
import com.example.demo.dto.CategoriesDTO;
import com.example.demo.persistence.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoriesRepository categoriesRepo;

    public List<Product> getCategoriesInProductList(int id){
        Categories categories=categoriesRepo.findById(id).get();
        return categories.getProductList();
    }

    public List<Product> getFindSoda(){
        Categories categories=categoriesRepo.findByCategoryName("탄산").get(0);
        return categories.getProductList();
    }
    public List<Product> getFindDessertAndSide(){
        Categories categories=categoriesRepo.findbyDrinkMenu("사이드","디저트").get(0);
        return categories.getProductList();
    }
}
