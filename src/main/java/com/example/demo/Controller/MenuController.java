package com.example.demo.Controller;

import com.example.demo.Service.BasketService;
import com.example.demo.Service.CategoriesService;
import com.example.demo.Service.CustomerService;
import com.example.demo.Service.ProductService;
import com.example.demo.config.auth.LoginUser;
import com.example.demo.config.auth.dto.SessionUser;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import com.example.demo.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping(path = "/fastfood")
public class MenuController {
    private final ProductService productService;
    private final CategoriesService categoriesService;
    private final CustomerService customerService;
//    private SecurityUtil securityUtil;
    private final BasketService basketService;

//    @GetMapping("/menu")
//    public String menupageView(@LoginUser SessionUser user,Model model) {
//        model.addAttribute("user",user);
//        return "fastfood/menu";
//    }
    @GetMapping("/menu")
    public String menupageView(Model model, HttpServletRequest request, @AuthenticationPrincipal CustomUserDetail customUser) {
        int id=customUser.getId();
        String user=customerService.findByidForUserId(id);
        model.addAttribute("id",id);
        model.addAttribute("user",user);
        System.out.println("menu 작동");
        log.info(id+" "+user);
        return "fastfood/menu";
    }


    @GetMapping("/Hdetailmenu")
    public String DetailMenuShow(Model model, @RequestParam("menuid") int menuid, @AuthenticationPrincipal CustomUserDetail customUser) {
        //세트일떄
        if (menuid == 6) {
            List<ProductResponseDto> sideList=productService.findByDessertAndSide();
            List<ProductResponseDto> drinkList=productService.findByCate(2);
            for(ProductResponseDto p : sideList){
                System.out.println(p.getProductName());
            }
            //온도, 크기 리스트
            model.addAttribute("sideList",sideList);
            model.addAttribute("drinkList",drinkList);
            //각 온도, 크기별로 가격리스트
            model.addAttribute("spList",productService.findSidePrice(sideList));
            model.addAttribute("dpList",productService.findDrinkPrice(drinkList));
        }
        List<ProductResponseDto> list=productService.findByCate(menuid);
        for(int i=0; i<list.size(); i++){
            ProductResponseDto p=list.get(0);
        }
        model.addAttribute("productlist", productService.findByCate(menuid));
        model.addAttribute("menuid", menuid);
        model.addAttribute("menuname", categoriesService.findCateName(menuid));
        model.addAttribute("basketSize",basketService.countByCustomer(customUser.getId()));
        model.addAttribute("userId",customUser.getId());
        return "fastfood/Hdetailmenu";
    }


}
