package com.example.demo.config.auth;

import com.example.demo.Service.ConnectCustomerService;
import com.example.demo.Service.RefreshTokenService;
import com.example.demo.config.auth.JsonCustomLogin.Manager.CustomAuthenticationManager;
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
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
                .antMatchers("/api/customer/login","/api/customer/logout","/fastfood/login"
                        ,"/fastfood/register","/fastfood/menu","/fastfood/ResetPasswd").permitAll()
                .antMatchers("/css/**","/js/**","/img/**").permitAll()
                .antMatchers("/api/auth/**", "/api/mail/**").permitAll()
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

        //필터를 등록하는 부분
        // JwtExceptionFilter -> JwtAuthenticationFilter -> customJsonUsernamePasswordAuthenticationFilter -> LogoutFilter 순서
        httpSecurity.addFilterBefore(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        httpSecurity.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider,tokenUtils), CustomJsonUsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(new JwtExceptionFilter(),JwtAuthenticationFilter.class );


        return httpSecurity.build();
    }
    // customJsonUsernamePasswordAuthenticationFilter 제가 직접 만든 필터이므로 각 필터에 필요한 직접 만든 핸들러, authenticationManager
    // customLoginService,passwordEncoder를 미리 설정 해줘야한다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CustomLoginService customLoginService(){
        return new CustomLoginService(customerRepository);
    }
    @Bean
    public CustomAuthenticationManager authenticationManager(){
        return new CustomAuthenticationManager(customerRepository,passwordEncoder(),customLoginService());
    }
    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler(connectCustomerService,jwtTokenProvider,refreshTokenService);
    }
    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }
    //CustomJsonUsernamePasswordAuthenticationFilter 빈 등록을 한다. (직접 만든 커스텀 필터이므로)
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