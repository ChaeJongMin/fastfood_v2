package com.example.demo.Service;

import com.example.demo.dto.ProductResponseDto;
import com.example.demo.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

//    public ProductRequsetDto getProductList(String productName){
//        Product product=productRepository.findByProductName(productName).get(0);
//        ProductRequsetDto productRequsetDto=new ProductRequsetDto(product.getProductName(),product.getPrice(),product.isAllSale());
//        return productRequsetDto;
//    }
    /**************************************************************************************************************/
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByCate(int id){
        return productRepository.findByCategories_CategoryId(id).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByDessertAndSide(){
        return productRepository.findByCategories_CategoryIdOrCategories_CategoryId(4,5).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());

    }

    public List<Integer> findDrinkPrice(List<ProductResponseDto> pList){
        List<Integer> drinkPriceList=new ArrayList<>();
        for(ProductResponseDto p : pList){
            if (p.getPrice() <= 1500) {
                drinkPriceList.add(0);
            } else {

                drinkPriceList.add(p.getPrice() - 1200);
            }
        }
        return drinkPriceList;
    }

    public List<Integer> findSidePrice(List<ProductResponseDto> pList){
        List<Integer> sidePriceList=new ArrayList<>();
        for(ProductResponseDto p : pList){
            if (p.getPrice() <= 1500) {
                sidePriceList.add(0);
            } else {

                sidePriceList.add(p.getPrice() - 1200);
            }
        }
        return sidePriceList;
    }

}
