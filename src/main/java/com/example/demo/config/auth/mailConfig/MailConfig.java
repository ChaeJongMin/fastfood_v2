package com.example.demo.config.auth.mailConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
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