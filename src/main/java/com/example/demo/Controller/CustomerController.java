package com.example.demo.Controller;

import com.example.demo.Service.CustomerService;
import com.example.demo.config.auth.LoginUser;
import com.example.demo.config.auth.dto.SessionUser;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping(path = "/fastfood")
public class CustomerController {
    private final CustomerService customerService;

//    @GetMapping("/login")
//    public String showLogin(@RequestParam(value = "error", required = false)Boolean error,
//                            @RequestParam(value = "msg", required = false)String msg,
//                            Model model){
//        System.out.println("error: "+error+" msg: "+msg);
//        model.addAttribute("error",error);
//        model.addAttribute("msg",msg);
//        return "fastfood/login";
//    }
    @GetMapping("/login")
    public String showLogin(Model model){
        log.info("로그인 페이지");
        return "fastfood/login";

    }

    @GetMapping("/logout")
    public void logouts(){
        return;
    }
    @GetMapping("/register")
    public String signupView(@RequestParam(value = "mail", required = false)String mail, Model model) {
        model.addAttribute("mail",mail);
        return "fastfood/register";
    }

    @GetMapping("/CustomerInfoUpdate")
    public String updateView(@AuthenticationPrincipal CustomUserDetail customUser, Model model){
        int id=customUser.getId();
        model.addAttribute("customer",customerService.findById(id));
        model.addAttribute("id", id);
        return "fastfood/CustomerInfoUpdate";
    }
}

