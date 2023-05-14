package com.example.demo.config.auth;

//import com.example.demo.config.auth.CustomAuthenticationSuccessHandler;
import com.example.demo.config.auth.Oauth.CustomOAuth2UserService;
import com.example.demo.config.auth.Oauth.OAuth2SuccessHandler;
import com.example.demo.config.auth.jwt.*;
import com.example.demo.config.auth.jwt.domain.Util.TokenUtils;
import com.example.demo.domain.Role;
import com.example.demo.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

//    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
//    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
//    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final TokenUtils tokenUtils;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2SuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers("/api/customer/login","/api/customer/logout","/fastfood/login","/fastfood/register","/fastfood/menu").permitAll()
                .antMatchers("/css/**","/js/**","/img/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/customer").permitAll()
//                .antMatchers("/fastfood/**","/api/**").hasAnyRole(Role.USER.name())
                .anyRequest().authenticated()
                .and()

                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .userInfoEndpoint().userService(customOAuth2UserService);

//                .exceptionHandling()
                //인증 실패
//                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                //인가 실패
//                .accessDeniedHandler(new JwtAccessDeniedHandler())
        httpSecurity.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider,tokenUtils), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);


        return httpSecurity.build();
    }



//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//        ;
//
//        http
//                .authorizeRequests()
//                .antMatchers("/fastfood/menu").hasAnyRole(Role.USER.name())
//                .antMatchers("/fastfood/CustomerInfoUpdate").hasAnyRole(Role.USER.name())
//                .antMatchers("/fastfood/Hdetailmenu").hasAnyRole(Role.USER.name())
//                .antMatchers("/fastfood/mybasket").hasAnyRole(Role.USER.name())
//                .antMatchers("/fastfood/Payment").hasAnyRole(Role.USER.name())
//                .antMatchers("/fastfood/superhome").hasAnyRole(Role.ADMIN.name())
//                .anyRequest().permitAll()
//        ;
//
//        http
//                .formLogin()
//                .loginPage("/fastfood/login")
//                .loginProcessingUrl("/fastfood/login")
//                .usernameParameter("userId")
//                .passwordParameter("userPasswd")
//                .successHandler(customAuthenticationSuccessHandler)
//                .failureHandler(customAuthenticationFailureHandler)
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/fastfood/logout"))
//                .logoutSuccessUrl("/fastfood/login")
//                .invalidateHttpSession(true)
//        ;
//
//        http
//                .sessionManagement()
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(true)
//        ;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(customAuthenticationProvider);
//    }


}