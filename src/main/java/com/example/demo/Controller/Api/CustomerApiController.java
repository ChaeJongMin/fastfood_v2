package com.example.demo.Controller.Api;

import com.example.demo.Service.ConnectCustomerService;
import com.example.demo.Service.CustomerService;
import com.example.demo.dto.Request.CustomerSaveRequestDto;
import com.example.demo.exception.Exception.UserAuthException;
import com.example.demo.exception.message.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerApiController {
    private final CustomerService customerService;
    private final ConnectCustomerService connectCustomerService;

    @GetMapping("/api/customer")
    public ResponseEntity<?> info(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new UserAuthException(ExceptionMessage.NOT_AUTHORIZED_ACCESS);
        }
        log.info("가져온 아이디 정보: "+customerService.findByUserId(principal.getName()).getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findByUserId(principal.getName()).getUserId());
    }

    @PostMapping("/api/customer")
    public int sava(@RequestBody CustomerSaveRequestDto customerSaveRequestDto){
        //소셜 로그인한 유저일 경우!
        //최초 사용자가 소셜 로그인을 이용 시 customer 테이블에 이메일과 소셜 정보가 이미 존재하므로
        //이 정보를 바탕으로 구분
        // 해당 이메일 아이디가 이미 존재하면 넘어가야한다. (따로 처리 필요)
        log.info("/api/customer");
        if(customerService.existToSocialFromEmail(customerSaveRequestDto.getEmail())){
            return customerService.updateForInitSocialUser(customerSaveRequestDto);
        }
        return customerService.save(customerSaveRequestDto);
    }

    @PutMapping("/api/customer/{id}")
    public int update(@PathVariable int id, @RequestBody CustomerSaveRequestDto customerSaveRequestDto){
        return customerService.update(id,customerSaveRequestDto);
    }


}
