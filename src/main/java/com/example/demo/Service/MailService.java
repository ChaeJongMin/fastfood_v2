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

<<<<<<< HEAD

// 메일 기능을 처리하는 서비스 계층
=======
>>>>>>> fastfoodv2/master
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
<<<<<<< HEAD
        //랜덤한 10자리 코드 = 알파벳(대소문자) + 숫자
=======

>>>>>>> fastfoodv2/master
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(3);

            switch (index) {
<<<<<<< HEAD
                // 소문자 알파벳 추가
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                // 대문자 알파벳 추가
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                // 숫자 추가
=======
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
>>>>>>> fastfoodv2/master
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
<<<<<<< HEAD
        // 생성된 코드를 문자열로 변환하여 저장
        authCode = key.toString();
    }

    // 보낼 메일을 설정하는 메소드
    public MimeMessage sendVerificationEmail(String to) throws MessagingException, UnsupportedEncodingException {
        // MIME 형식의 이메일을 나타내는 객체인 MimeMessage을 생성합니다.
        // createMimeMessage()를 통해 보낼 메일 양식과 정보를 설정합니다.
        MimeMessage message = javaMailSender.createMimeMessage();

        // MimeMessageHelper 객체는 MimeMessage 객체를 편리하게 사용할 수 있도록 도와주는 객체 입니다.
        // setText(), setSubject(), setFrom(), setTo() 메서드를 사용하여 MimeMessage 객체의 속성을 설정 가능하다.
        // message 는 MimeMessage 객체 , true 는 multipart 여부 , UTF-8 는 콘텐츠 인코딩
=======
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
>>>>>>> fastfoodv2/master
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("BUGGERQUEEN 비밀번호 초기화 인증 코드");

<<<<<<< HEAD
        //인증 코드를 포함하는 HTML 이메일 본문을 생성
=======
>>>>>>> fastfoodv2/master
        String htmlContent = generateVerificationEmailContent(authCode);
        helper.setText(htmlContent, true);

        helper.setFrom(new InternetAddress("dico9797@naver.com", "버거퀸"));

        return message;
    }

<<<<<<< HEAD
    //  HTML 이메일 본문을 생성하는 메소드
=======
>>>>>>> fastfoodv2/master
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

<<<<<<< HEAD
    // 고객 이메일로 메일을 보내는 메소드
    public String sendSimpleMessage(String to) throws Exception {
        try {
            // 랜덤 인증번호 생성
            createCode();
            // MimeMessage 객체 생성
            // sendVerificationEmail() 메소드를 통해 메일에 여러 정보를 설정
            MimeMessage message = sendVerificationEmail(to);
            // javaMailSender 객체의 send() 메서드를 사용하여 이메일을 전송
=======

    public String sendSimpleMessage(String to) throws Exception {
        try {
            createCode(); // 랜덤 인증번호 생성
            MimeMessage message = sendVerificationEmail(to);
>>>>>>> fastfoodv2/master
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to send email.");
        }
<<<<<<< HEAD
        return authCode;
    }

    // 이메일이 존재하는 검사하는 메소드
=======
        return authCode; // 메일로 보냈던 인증 코드를 서버로 반환
    }

>>>>>>> fastfoodv2/master
    @Transactional(readOnly = true)
    public boolean checkExistEmail(MailCheckDto mailCheckDto) throws Exception {
       return customerRepository.existsByUserIdAndEmail(mailCheckDto.getName(),mailCheckDto.getEmail());

    }

<<<<<<< HEAD
    // 새로운 비밀번호를 업데이트하는 메소드
    @Transactional
    public int updatePasswd(MailUpdatedDto mailUpdatedDto){
        // 비밀번호를 업데이트를 할 Customer 객체를 생성
        Customer customer = customerRepository.findByEmail(mailUpdatedDto.getEmail()).orElseThrow(
                ()-> new IllegalArgumentException("해당 사용자가 없습니다.")
        );

=======
    @Transactional
    public int updatePasswd(MailUpdatedDto mailUpdatedDto){
        Customer customer = customerRepository.findByEmail(mailUpdatedDto.getEmail()).orElseThrow(
                ()-> new IllegalArgumentException("해당 사용자가 없습니다.")
        );
>>>>>>> fastfoodv2/master
        //비밀번호 변경
        customer.updateEncryptedPasswd(passwordEncoder.encode(mailUpdatedDto.getPasswd()));
        return customer.getId();
    }


}
