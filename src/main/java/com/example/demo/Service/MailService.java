package com.example.demo.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.example.demo.domain.Customer;
import com.example.demo.dto.Request.MailCheckDto;
import com.example.demo.dto.Request.MailUpdatedDto;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.core.context.SecurityContextHolder.setContext;
// 메일 기능을 처리하는 서비스 계층
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    private String authCode;

    // 인증 코드 생성
    public void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();
        //랜덤한 10자리 코드 = 알파벳(대소문자) + 숫자
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(3);

            switch (index) {
                // 소문자 알파벳 추가
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                // 대문자 알파벳 추가
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                // 숫자 추가
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        // 생성된 코드를 문자열로 변환하여 저장
        authCode = key.toString();
    }

    // 보낼 메일을 설정하는 메소드
    public MimeMessage sendVerificationEmail(String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("BUGGERQUEEN 비밀번호 초기화 인증 코드");

        //인증 코드를 포함하는 HTML 이메일 본문을 생성
        String htmlContent = generateVerificationEmailContent(authCode);
        helper.setText(htmlContent, true);

        helper.setFrom(new InternetAddress("dico9797@naver.com", "버거퀸"));

        return message;
    }

    //  HTML 이메일 본문을 생성하는 메소드
    public String generateVerificationEmailContent(String authCode) {
        StringBuilder sb = new StringBuilder();

        sb.append("<div style='background-color: #ffffff; padding: 30px; border-radius: 5px;'>");
        sb.append("<h1 style='color: #e91e63;'>안녕하세요</h1>");
        sb.append("<h2 style='color: #e91e63;'>최고의 맛과 서비스를 자랑하는 BuggerQueen입니다</h2>");
        sb.append("<br>");
        sb.append("<p style='color: #000;'>아래 코드를 비밀번호 찾기 창에서 인증번호를 입력해주세요.</p>");
        sb.append("<br>");
        sb.append("<div style='background-color: #fce4ec; border: none; padding: 10px; text-align: center; font-family: verdana;'>");
        sb.append("<h3 style='color: #ffffff;'>비밀번호 초기화 인증 코드입니다.</h3>");
        sb.append("<div style='font-size: 130%; color: #ffffff;'>");
        sb.append("<strong>CODE: ").append(authCode).append("</strong>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");

        return sb.toString();
    }

    // 고객 이메일로 메일을 보내는 메소드
    public String sendSimpleMessage(String to) throws Exception {
        try {
            // 랜덤 인증번호 생성
            createCode();
            // MimeMessage 객체 생성
            // sendVerificationEmail() 메소드를 통해 메일에 여러 정보를 설정
            MimeMessage message = sendVerificationEmail(to);
            // javaMailSender 객체의 send() 메서드를 사용하여 이메일을 전송
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to send email.");
        }
        // 이메일이 존재하는 검사하는 메소드
        return authCode; // 메일로 보냈던 인증 코드를 서버로 반환
    }


    @Transactional(readOnly = true)
    public boolean checkExistEmail(MailCheckDto mailCheckDto) throws Exception {
       return customerRepository.existsByUserIdAndEmail(mailCheckDto.getName(),mailCheckDto.getEmail());

    }

    // 새로운 비밀번호를 업데이트하는 메소드
    public int updatePasswd(MailUpdatedDto mailUpdatedDto){
        Customer customer = customerRepository.findByEmail(mailUpdatedDto.getEmail()).orElseThrow(
                ()-> new IllegalArgumentException("해당 사용자가 없습니다.")
        );
        //비밀번호 변경
        customer.updateEncryptedPasswd(passwordEncoder.encode(mailUpdatedDto.getPasswd()));
        return customer.getId();
    }
}