package com.example.demo.config.auth;

import com.example.demo.Service.ConnectCustomerService;
import com.example.demo.Service.RefreshTokenService;
import com.example.demo.config.auth.JsonCustomLogin.CustomAuthenticationManager;
import com.example.demo.config.auth.JsonCustomLogin.Service.CustomLoginService;
import com.example.demo.config.auth.JsonCustomLogin.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.example.demo.config.auth.JsonCustomLogin.handler.LoginFailureHandler;
import com.example.demo.config.auth.JsonCustomLogin.handler.LoginSuccessHandler;
import com.example.demo.config.auth.Oauth.CustomOAuth2UserService;
import com.example.demo.config.auth.Oauth.OAuth2SuccessHandler;
import com.example.demo.config.auth.jwt.*;
import com.example.demo.config.auth.jwt.domain.Util.TokenUtils;
import com.example.demo.persistence.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor

public class SecurityConfig {
    private final RefreshTokenService refreshTokenService;
    private final CustomerRepository customerRepository;
    private final ConnectCustomerService connectCustomerService;
    private final TokenUtils tokenUtils;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2SuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final ObjectMapper objectMapper;
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
                .logout()
                .logoutUrl("/customer/logout")
                .logoutSuccessUrl("/fastfood/login")
                .deleteCookies("JSESSIONID", "accessToken", "refreshToken")
                .and()
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .userInfoEndpoint().userService(customOAuth2UserService);


//                .exceptionHandling()
                //인증 실패
//                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                //인가 실패
//                .accessDeniedHandler(new JwtAccessDeniedHandler())
        httpSecurity.addFilterBefore(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        httpSecurity.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider,tokenUtils), CustomJsonUsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(new JwtExceptionFilter(),JwtAuthenticationFilter.class );


        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CustomAuthenticationManager authenticationManager(){
        return new CustomAuthenticationManager(customerRepository,passwordEncoder());
    }
    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler(connectCustomerService,jwtTokenProvider,refreshTokenService);
    }
    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }
    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtTokenProvider,tokenUtils);
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