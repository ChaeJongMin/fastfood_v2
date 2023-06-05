package com.example.demo.Controller.Api;

import com.example.demo.Service.ConnectCustomerService;
import com.example.demo.Service.CustomerService;
import com.example.demo.domain.Role;
import com.example.demo.dto.Request.CustomerSaveRequestDto;
import com.example.demo.exception.UserAuthException;
import com.example.demo.exception.message.ExceptionMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Map;


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
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findByUserId(principal.getName()));
    }
//    @PostMapping("/api/customer/login")
//    public ResponseEntity<?> loginSuccess(@RequestBody Map<String, String> loginForm,
//                                       HttpServletResponse response) throws JsonProcessingException {
//
//        ObjectMapper mapper = new ObjectMapper();
//        connectCustomerService.increaseCurrentMonthCnt();
//        Role role=customerService.login(loginForm.get("userId"), loginForm.get("userPasswd"),response);
//        return ResponseEntity.status(HttpStatus.OK).body(role);
//    }
    @PostMapping("/api/customer")
    public int sava(@RequestBody CustomerSaveRequestDto customerSaveRequestDto){
        //소셜 로그인한 유저일 경우!
        //최초 사용자가 소셜 로그인을 이용 시 customer 테이블에 이메일과 소셜 정보가 이미 존재하므로
        //이 정보를 바탕으로 구분
        if(customerService.existToSocialFromEmail(customerSaveRequestDto.getEmail())){
            return customerService.updateForInitSocialUser(customerSaveRequestDto);
        }
        return customerService.save(customerSaveRequestDto);
    }
    @PutMapping("/api/customer/{id}")
    public int update(@PathVariable int id, @RequestBody CustomerSaveRequestDto customerSaveRequestDto){
        return customerService.update(id,customerSaveRequestDto);
    }
//    @DeleteMapping("/api/customer/logout")
//    public void logout(@RequestHeader("Authorization") String accessToken){
//        log.info("/api/customer/logout 작동");
//        customerService.logout(accessToken);
//    }

}
