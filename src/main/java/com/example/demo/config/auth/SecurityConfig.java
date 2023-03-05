package com.example.demo.config.auth;

import com.example.demo.config.auth.CustomAuthenticationSuccessHandler;
import com.example.demo.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
        ;

        http
                .authorizeRequests()
                .antMatchers("/fastfood/menu").hasAnyRole(Role.USER.name())
                .antMatchers("/fastfood/CustomerInfoUpdate").hasAnyRole(Role.USER.name())
                .antMatchers("/fastfood/Hdetailmenu").hasAnyRole(Role.USER.name())
                .antMatchers("/fastfood/mybasket").hasAnyRole(Role.USER.name())
                .antMatchers("/fastfood/Payment").hasAnyRole(Role.USER.name())
                .antMatchers("/fastfood/superhome").hasAnyRole(Role.ADMIN.name())
                .anyRequest().permitAll()
        ;

        http
                .formLogin()
                .loginPage("/fastfood/login")
                .loginProcessingUrl("/fastfood/login")
                .usernameParameter("userId")
                .passwordParameter("userPasswd")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/fastfood/logout"))
                .logoutSuccessUrl("/fastfood/login")
                .invalidateHttpSession(true)
        ;

        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

}