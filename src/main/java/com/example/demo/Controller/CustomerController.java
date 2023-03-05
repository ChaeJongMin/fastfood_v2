package com.example.demo.Controller;

import com.example.demo.Service.CustomerService;
import com.example.demo.config.auth.LoginUser;
import com.example.demo.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/fastfood")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/login")
    public String showLogin(@RequestParam(value = "error", required = false)Boolean error,
                            @RequestParam(value = "msg", required = false)String msg,
                            Model model){
        System.out.println("error: "+error+" msg: "+msg);
        model.addAttribute("error",error);
        model.addAttribute("msg",msg);
        return "fastfood/login";
    }
    @GetMapping("/logout")
    public void logouts(){
        return;
    }
    @GetMapping("/register")
    public String signupView() {
        return "fastfood/register";
    }

    @GetMapping("/CustomerInfoUpdate")
    public String updateView(@LoginUser SessionUser user, Model model){
        model.addAttribute("customer",customerService.findById(user.getId()));
        model.addAttribute("id", user.getId());
        return "fastfood/CustomerInfoUpdate";
    }
}

