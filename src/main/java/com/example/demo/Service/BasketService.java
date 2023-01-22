package com.example.demo.Service;

import com.example.demo.domain.Basket;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Product;
import com.example.demo.domain.Product_option_info;
import com.example.demo.dto.ProductOptionInfoRequestDto;
import com.example.demo.dto.Prouduct_option_infoDTO;
import com.example.demo.persistence.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;

    public void saveSingleMenu(ProductOptionInfoRequestDto productOptionInfoRequestDto, HttpSession session){
        Basket basket = new Basket();
        basket.setProductinfo(productOptionInfoRequestDto.productOptionInfoEntity());
        basket.setCustomer((Customer)session.getAttribute("user"));
        basket.setPCount(1);
        basket.setInfo(String.valueOf(productOptionInfoRequestDto.productOptionInfoEntity().getInfoid()));
        basket.setPrice(productOptionInfoRequestDto.getPrice());
        basketRepository.save(basket);
    }
    public void saveSetMenu(ProductOptionInfoRequestDto productOptionInfoRequestDto, HttpSession session,String info){
        Basket basket = new Basket();
        basket.setProductinfo(productOptionInfoRequestDto.productOptionInfoEntity());
        basket.setCustomer((Customer)session.getAttribute("user"));
        basket.setPCount(1);
        basket.setInfo(info);
        basket.setPrice(productOptionInfoRequestDto.getPrice());
        basketRepository.save(basket);
    }
}
