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
<<<<<<< HEAD

    private final MailService mailService;

    //name과 email을 받아와 해당 유저가 존재하고 이메일이 맞는지 검사하고
    // dto를 응답해주는 메소드
=======
    private final MailService mailService;

>>>>>>> fastfoodv2/master
    @GetMapping("/api/mail/{name}/{email}")
    public ResponseEntity<String> findUserEmail(@PathVariable String name,
                                                @PathVariable String email) throws Exception {
        String msg="";
        log.info("/api/mail Get 매핑 작동");
<<<<<<< HEAD
        //dto 생성
        MailCheckDto mailCheckDto=new MailCheckDto(name,email);
        // 이메일 존재 여부 확인
        if(mailService.checkExistEmail(mailCheckDto)){
            // 이메일 전송 및 인증 코드 반환
            String code=mailService.sendSimpleMessage(mailCheckDto.getEmail());
            return ResponseEntity.ok(code);
        } else {
            // 정보가 틀림으로 에러메시지와 400 상태 반환
=======
        MailCheckDto mailCheckDto=new MailCheckDto(name,email);
        if(mailService.checkExistEmail(mailCheckDto)){
            String code=mailService.sendSimpleMessage(mailCheckDto.getEmail());
            return ResponseEntity.ok(code);
        } else {
>>>>>>> fastfoodv2/master
            msg="아이디 또는 이메일 정보가 올바르지 않습니다.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

    }
<<<<<<< HEAD

    //새로운 비밀번호를 받아 업데이트하는 메소드
    @PatchMapping("/api/mail")
    public int updatePasswd(@RequestBody MailUpdatedDto mailUpdatedDto){
        // 패스워드 업데이트 처리
=======
//    @GetMapping("/api/mail/{email}")
//    public ResponseEntity<String> sendEmail(@PathVariable String email) throws Exception {
//
//        return ResponseEntity.status(HttpStatus.OK).body(code);
//    }

    @PatchMapping("/api/mail")
    public int updatePasswd(@RequestBody MailUpdatedDto mailUpdatedDto){
>>>>>>> fastfoodv2/master
        return mailService.updatePasswd(mailUpdatedDto);
    }
}
