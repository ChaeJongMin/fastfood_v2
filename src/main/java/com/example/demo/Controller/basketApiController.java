package com.example.demo.Controller;

import com.example.demo.Service.BasketService;
import com.example.demo.config.auth.LoginUser;
import com.example.demo.config.auth.dto.SessionUser;
import com.example.demo.dto.BasketUpdateRequestDto;
import com.example.demo.dto.ProductResponseDto;
import com.example.demo.dto.ProductSaveToMenuRequestDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class basketApiController {
    private final BasketService basketService;

    @PostMapping("/api/basket/{id}")
    public int saveSingle(@RequestBody ProductSaveToMenuRequestDto productSaveToMenuRequestDto,@PathVariable int id){
        basketService.save(productSaveToMenuRequestDto,id);
        return basketService.countByCustomer(id);
    }
    @PutMapping("/api/basket/{id}")
    public int update(@PathVariable int id, @RequestBody List<BasketUpdateRequestDto> list){
        return basketService.basketUpdate(list,id);
    }
    @DeleteMapping("/api/basket/{id}")
    public int delete(@PathVariable int id){
        return basketService.delete(id);
    }

}
