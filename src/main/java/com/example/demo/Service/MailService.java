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

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authCode = key.toString();
    }


//    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
//
//        MimeMessage message = javaMailSender.createMimeMessage();
//
//        message.addRecipients(RecipientType.TO, to);
//        message.setSubject("BUGGERQUEEN 회원가입 이메일 인증");
//
//        String msgg = "<div style='background-color:#fce4ec; padding:30px;'>";
//        msgg += "<h1 style='color:#e91e63;'>안녕하세요</h1>";
//        msgg += "<h2 style='color:#e91e63;'>최고의 맛과 서비스를 자랑하는 BuggerQueen 입니다</h2>";
//        msgg += "<br>";
//        msgg += "<p style='color:#000;'>아래 코드를 비밀번호 찾기 창에서 인증번호를 입력해주세요.</p>";
//        msgg += "<br>";
//        msgg += "<div align='center' style='border:1px solid #e91e63; font-family:verdana; padding:10px;'>";
//        msgg += "<h3 style='color:#e91e63;'>회원가입 인증 코드입니다.</h3>";
//        msgg += "<div style='font-size:130%'>";
//        msgg += "<strong style='color:#e91e63;'>CODE: " + authCode + "</strong>"; // 메일에 인증번호 넣기
//        msgg += "</div>";
//        msgg += "</div>";
//        msgg += "</div>";
//        message.setContent(msgg, "text/html; charset=utf-8");
//
//        message.setFrom(new InternetAddress("dico9797@naver.com", "버거퀸"));
//
//        return message;
//    }
    public MimeMessage sendVerificationEmail(String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("BUGGERQUEEN 비밀번호 초기화 인증 코드");

        String htmlContent = generateVerificationEmailContent(authCode);
        helper.setText(htmlContent, true);

        helper.setFrom(new InternetAddress("dico9797@naver.com", "버거퀸"));

        return message;
    }

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


    public String sendSimpleMessage(String to) throws Exception {
        try {
            createCode(); // 랜덤 인증번호 생성
            MimeMessage message = sendVerificationEmail(to);
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to send email.");
        }
        return authCode; // 메일로 보냈던 인증 코드를 서버로 반환
    }

    @Transactional(readOnly = true)
    public boolean checkExistEmail(MailCheckDto mailCheckDto) throws Exception {
       return customerRepository.existsByUserIdAndEmail(mailCheckDto.getName(),mailCheckDto.getEmail());

    }

    @Transactional
    public int updatePasswd(MailUpdatedDto mailUpdatedDto){
        Customer customer = customerRepository.findByEmail(mailUpdatedDto.getEmail()).orElseThrow(
                ()-> new IllegalArgumentException("해당 사용자가 없습니다.")
        );
        //비밀번호 변경
        customer.updateEncryptedPasswd(passwordEncoder.encode(mailUpdatedDto.getPasswd()));
        return customer.getId();
    }


}
