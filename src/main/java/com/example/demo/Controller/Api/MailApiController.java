package com.example.demo.Controller.Api;

import com.example.demo.Service.MailService;
import com.example.demo.dto.Request.MailCheckDto;
import com.example.demo.dto.Request.MailUpdatedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MailApiController {
    private final MailService mailService;

    @GetMapping("/api/mail/{name}/{email}")
    public ResponseEntity<String> findUserEmail(@PathVariable String name,
                                                @PathVariable String email) throws Exception {
        String msg="";
        log.info("/api/mail Get 매핑 작동");
        MailCheckDto mailCheckDto=new MailCheckDto(name,email);
        if(mailService.checkExistEmail(mailCheckDto)){
            String code=mailService.sendSimpleMessage(mailCheckDto.getEmail());
            return ResponseEntity.ok(code);
        } else {
            msg="아이디 또는 이메일 정보가 올바르지 않습니다.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

    }
//    @GetMapping("/api/mail/{email}")
//    public ResponseEntity<String> sendEmail(@PathVariable String email) throws Exception {
//
//        return ResponseEntity.status(HttpStatus.OK).body(code);
//    }

    @PatchMapping("/api/mail")
    public int updatePasswd(@RequestBody MailUpdatedDto mailUpdatedDto){
        return mailService.updatePasswd(mailUpdatedDto);
    }
}
