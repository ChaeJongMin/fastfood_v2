package com.example.demo.Service;

import com.example.demo.domain.Product;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductRequsetDto;
import com.example.demo.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Integer> findDrinkPrice(List<Product> pList){
        List<Integer> drinkPriceList=new ArrayList<>();
        for(Product p : pList){
            if (p.getPrice() <= 1500) {
                drinkPriceList.add(0);
            } else {

                drinkPriceList.add(p.getPrice() - 1200);
            }
        }
        return drinkPriceList;
    }

    public List<Integer> findSidePrice(List<Product> pList){
        List<Integer> sidePriceList=new ArrayList<>();
        for(Product p : pList){
            if (p.getPrice() <= 1500) {
                sidePriceList.add(0);
            } else {

                sidePriceList.add(p.getPrice() - 1200);
            }
        }
        return sidePriceList;
    }
    public ProductRequsetDto getProduct(String productName){
        Product product=productRepository.findByProductName(productName).get(0);
        ProductRequsetDto productRequsetDto=new ProductRequsetDto(product.getProductName(),product.getPrice(),product.isAllSale());
        return productRequsetDto;
    }

}
