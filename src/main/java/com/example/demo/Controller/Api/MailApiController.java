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

    //name과 email을 받아와 해당 유저가 존재하고 이메일이 맞는지 검사하고
    // dto를 응답해주는 메소드
    @GetMapping("/api/mail/{name}/{email}")
    public ResponseEntity<String> findUserEmail(@PathVariable String name,
                                                @PathVariable String email) throws Exception {
        String msg = "";
        log.info("/api/mail Get 매핑 작동");
        //dto 생성
        MailCheckDto mailCheckDto = new MailCheckDto(name, email);
        // 이메일 존재 여부 확인
        if (mailService.checkExistEmail(mailCheckDto)) {
            // 이메일 전송 및 인증 코드 반환
            String code = mailService.sendSimpleMessage(mailCheckDto.getEmail());
            return ResponseEntity.ok(code);
        } else {
            msg = "아이디 또는 이메일 정보가 올바르지 않습니다.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
    }

    //새로운 비밀번호를 받아 업데이트하는 메소드
    @PatchMapping("/api/mail")
    public int updatePasswd(@RequestBody MailUpdatedDto mailUpdatedDto){
        return mailService.updatePasswd(mailUpdatedDto);
    }
}
