package com.example.demo.Controller.Page;

import com.example.demo.Service.CustomerService;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping(path = "/fastfood")
public class CustomerController {
    private final CustomerService customerService;


    @GetMapping("/login")
    public String showLogin(Model model){
        log.info("로그인 페이지");
        return "fastfood/customer/customer/login";

    }

    @PostMapping("/logout")
    public void logouts(){
        log.info("logout 컨트롤러 작동");
        return;
    }
    @GetMapping("/register")
    public String signupView(@RequestParam(value = "mail", required = false)String mail, Model model) {
        model.addAttribute("mail",mail);
        return "fastfood/customer/customer/register";
    }

    @GetMapping("/CustomerInfoUpdate")
    public String updateView(@AuthenticationPrincipal CustomUserDetail customUser, Model model){
        int id=customUser.getId();
        model.addAttribute("customer",customerService.findById(id));
        model.addAttribute("id", id);
        return "fastfood/customer/customer/CustomerInfoUpdate";
    }
}

