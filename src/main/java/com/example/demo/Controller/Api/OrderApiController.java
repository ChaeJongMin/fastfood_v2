package com.example.demo.Controller.Api;

import com.example.demo.Service.CustomerService;
import com.example.demo.Service.OrderService;
import com.example.demo.dto.Request.CardInfoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderService orderService;
    private final CustomerService customerService;

    @PostMapping("/api/order/{id}")
    public int save(@PathVariable int id, @RequestBody CardInfoRequestDto cardInfoRequestDto){
        boolean flag=customerService.checkCardInfo(id,cardInfoRequestDto);
        if(flag){
            return orderService.save(id);
        }
        return -1;
    }

}
