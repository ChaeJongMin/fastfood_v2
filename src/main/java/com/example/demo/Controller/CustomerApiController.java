package com.example.demo.Controller;

import com.example.demo.Service.CustomerService;
import com.example.demo.dto.CustomerRequestDto;
import com.example.demo.dto.CustomerSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CustomerApiController {
    private final CustomerService customerService;
    @PostMapping("/api/customer")
    public int sava(@RequestBody CustomerSaveRequestDto customerSaveRequestDto){
        return customerService.save(customerSaveRequestDto);
    }
    @PutMapping("/api/customer/{id}")
    public int update(@PathVariable int id, @RequestBody CustomerSaveRequestDto customerSaveRequestDto){
        System.out.println("d안농!!!");
        return customerService.update(id,customerSaveRequestDto);
    }


}
