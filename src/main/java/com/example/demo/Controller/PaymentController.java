package com.example.demo.Controller;

import com.example.demo.Service.BasketService;
import com.example.demo.config.auth.LoginUser;
import com.example.demo.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/fastfood")
public class PaymentController {
    private final BasketService basketService;
    @GetMapping("/Payment")
    public String showPayment(@LoginUser SessionUser user, Model model){
        model.addAttribute("userId",user.getUserId());
        model.addAttribute("ids",user.getId());
        model.addAttribute("totalprice",basketService.TotalPriceByCustomer(user.getId()));
        return "fastfood/Payment";
    }
}
