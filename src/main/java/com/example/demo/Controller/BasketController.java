package com.example.demo.Controller;

import com.example.demo.Service.BasketService;
import com.example.demo.Service.ProductOptionService;
import com.example.demo.config.auth.LoginUser;
import com.example.demo.config.auth.dto.SessionUser;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import com.example.demo.dto.BasketResponseDto;
import com.example.demo.dto.ProductOptionResponseDto;
import com.example.demo.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/fastfood")
public class BasketController {
    private final BasketService basketService;
    private final ProductOptionService productOptionService;
    @GetMapping("/my_baket")
    public String showCartView(@AuthenticationPrincipal CustomUserDetail customUser, Model model){
        List<BasketResponseDto> basketList=basketService.findByCustomerForBasketItem(customUser.getId());
        List<ProductOptionResponseDto> productList=productOptionService.findByCustomerForProductOption(customUser.getId());
        int[] SumPriceAndCount= basketService.finalPriceAndCount(basketList);
        model.addAttribute("basketList", basketList);
        model.addAttribute("productList", productList);
        //총가격
        model.addAttribute("totalPrice",SumPriceAndCount[0]);
        //총개수
        model.addAttribute("totalCnt",SumPriceAndCount[1]);
        model.addAttribute("setForSideAndDrink",productOptionService.setForSideAndDrinkName(
                basketList,basketList.size()));
        model.addAttribute("userId",customUser.getId());
        return "fastfood/my_baket";
    }

}
