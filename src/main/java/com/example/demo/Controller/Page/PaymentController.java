package com.example.demo.Controller.Page;

import com.example.demo.Service.BasketService;
import com.example.demo.Service.CustomerService;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/fastfood")
public class PaymentController {
    private final BasketService basketService;
    private final CustomerService customerService;
    @GetMapping("/Payment")
    public String showPayment(@AuthenticationPrincipal CustomUserDetail customUser, Model model){
        model.addAttribute("userId",customerService.findByidForUserId(customUser.getId()));
        model.addAttribute("ids",customUser.getId());
        model.addAttribute("totalprice",basketService.TotalPriceByCustomer(customUser.getId()));
        return "fastfood/customer/payment/Payment";
    }
}
