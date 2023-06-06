package com.example.demo.config.mailConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

// @Configuration 어노테이션을 사용하여 Spring에서 구성 클래스로 인식되도록 표시
@Configuration
 //이메일 전송을 위한 JavaMailSender 구성을 담당하는 클래스
// 지정된 값들에 따라 SMTP 호스트, 포트, 계정 정보, 보안 옵션 등이 구성되며, JavaMailSender를 주입받아 이메일 전송 기능을 사용
public class MailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnabled;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean authEnabled;

    @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
    private boolean sslEnabled;


    // JavaMailSender 빈
    // Spring Boot가 JavaMailSender 객체를 생성하고 애플리케이션 컨텍스트에 bean으로 등록
    // 인스턴스는 이메일 전송을 위한 기본적인 설정을 가지고 있다.
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setJavaMailProperties(getProperties());

        return javaMailSender;
    }

    // 메일 전송에 필요한 추가적인 프로퍼티 값을 설정하는 데 사용
    // 메일 속성을 포함하는 Properties 객체를 반환 및 메일 속성은 JavaMailSender 객체를 구성하는 데 사용
    private Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", String.valueOf(authEnabled));
        properties.setProperty("mail.smtp.starttls.enable", String.valueOf(starttlsEnabled));
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.ssl.enable", String.valueOf(sslEnabled));

        return properties;
    }

}