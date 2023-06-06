package com.example.demo.config.auth.jwt;

import com.example.demo.config.auth.jwt.UserData.CustomLoadUserByUsername;
import com.example.demo.config.auth.jwt.domain.RefreshTokens;
import com.example.demo.exception.Exception.CustomException;
import com.example.demo.persistence.RefreshTokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.exception.errorCode.ErrorCode.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    // JWT 토큰에 관한 여러 로직이 담긴 클래스
    private final RefreshTokenRepository refreshTokenRepository;



//    public static final String ACCESS_TOKEN = "Access_Token";
//    public static final String REFRESH_TOKEN = "Refresh_Token";

    @Value("${token.secret}")
    private String secretKey;
    public static final long ACCESS_TIME = 86400000;
    public static final long REFRESH_TIME = 259200000;
//    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer-";

    private final CustomLoadUserByUsername customLoadUserByUsername;
    private Key key;


    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);

    }

    /********************************************************************************/
    //액세스 토큰을 생성하는 메소드
    // 액세스 토큰에 사용자 정보가 있어도되나 보안적으로 중요한 데이터 (비밀번호 같은 데이터)는 넣는 것은 비추
    public String generateToken(String userId, long expirationTime){
        
        UserDetails userDetails=customLoadUserByUsername.loadUserByUsername(userId);
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Claims claims=Jwts.claims().setSubject(userId);
        Date now=new Date();
        Date expireDate=new Date(now.getTime()+expirationTime);
        System.out.println(authorities);
        return Jwts.builder()
                .setSubject(userId)
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }
    //리프레쉬 토큰을 생성하는 메소드
    // 리프레쉬 토큰은 사용자의 정보가 필요없다.
    public String refreshGenerateToken(String userId, long expirationTime){
        Date now=new Date();
        Date expireDate=new Date(now.getTime()+expirationTime);
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }
    //현재 토큰의 만료 여부를 확인하는 메소드
    public boolean checkRemainTime(String token) {
        Date expiration = parseClaim(token).getExpiration();
        Date now = new Date();
        if(expiration.getTime() < now.getTime()/1000){
            log.info("만료시간: "+expiration.getTime()+" 현재시간: "+now.getTime()/1000);
            return false;
        }

        return true;
    }
//    public boolean checkRemainTime(String token) {
//        Date expiration = parseClaim(token).getExpiration();
//        Date now = new Date();
//        if(expiration.getTime() < now.getTime()/1000){
//            log.info("만료시간: "+expiration.getTime()+" 현재시간: "+now.getTime()/1000);
//            return false;
//        }
//
//        return true;
//    }
    //Bearer-액세스 토큰 value에서 value만 추출
    public String resolveToken(String token) {
        if (!ObjectUtils.isEmpty(token) && token.startsWith(JwtTokenProvider.TOKEN_PREFIX)) {
            return token.substring(JwtTokenProvider.TOKEN_PREFIX.length());
        }
        return null;
    }
    //토큰의 페이로드의 subject(유저 아이디)를 반환하는 메소
    public String parseToken(String token) {
        return parseClaim(token).getSubject();
    }
    //액세스 토큰의 만료 시간을 얻는 메소드
    public long getExpiredTime(String token){
        //리프레쉬, 액세스 차별 여부 필요
        long expiredTime=parseClaim(token).getExpiration().getTime();
        log.info("getExpiredTime메소드 : "+expiredTime);
        return expiredTime;
    }

//    public long getRefreshTokenExpiredTime(String token){
//        //리프레쉬, 액세스 차별 여부 필요
//        long expiredTime=parseClaimRefreshToken(token).getExpiration().getTime();
//        log.info("getExpiredTime메소드 : "+expiredTime);
//        return expiredTime;
//    }
//    public void setRefreshTokenAtCookie(RefreshTokens refreshToken, HttpServletResponse response) {
//        ResponseCookie refreshCookie=ResponseCookie.from("refreshToken",refreshToken.getValue())
//                .maxAge(60 * 60 * 3)
//                .secure(true)
//                .httpOnly(true)
//                .path("/")
//                .sameSite("None")
//                .build();
//
//        response.setHeader("Set-Cookie",refreshCookie.toString());
//    }

    // 리프레쉬 토큰의 만료 시간을 얻는 메소드
//    public long getRefreshTokenExpiredTime(String token){
//        //리프레쉬, 액세스 차별 여부 필요
//        //차별을 둔 이유는 서로 다른 예외 메시지를 발급해주기 위해
//        long expiredTime=parseClaimRefreshToken(token).getExpiration().getTime();
//        log.info("getExpiredTime메소드 : "+expiredTime);
//        return expiredTime;
//    }
    //리프레쉬 토큰을 쿠키에 설정하는 메소드드
    public void setRefreshTokenAtCookie(RefreshTokens refreshToken, HttpServletResponse response) {
        ResponseCookie refreshCookie=ResponseCookie.from("refreshToken",refreshToken.getValue())
                .maxAge(60 * 60 * 3)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie",refreshCookie.toString());
    }
    //액세스 토큰을 쿠키에 설정하는 메소드드

    public void setAccessTokenAtCookie(String AccessToken, HttpServletResponse response) {
        ResponseCookie accessCookie=ResponseCookie.from("accessToken",TOKEN_PREFIX+AccessToken)
                .maxAge(60*30)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie",accessCookie.toString());
    }
    //액세스, 리프레쉬 토큰을 둘 다 쿠키에 설정
    //액세스 , 액세스+리프레쉬로 나눈 이유는 각 토큰이 따로 발급할 필요가 있다
    //액세스만 발급은 액세스 토큰이 만료될 떄
    //액세스+리프레쉬는 로그인 했을 떄떄
    public void setAccessTokenAndRefreshToken(String accessTokenInfo, String refreshTokenInfo, HttpServletResponse response){
        Cookie accessCookie=new Cookie("accessToken",TOKEN_PREFIX+accessTokenInfo);
        accessCookie.setPath("/");
        accessCookie.setSecure(true);
        accessCookie.setHttpOnly(true);
        accessCookie.setMaxAge(60*30);
        response.addCookie(accessCookie);

        Cookie refreshCookie=new Cookie("refreshToken",refreshTokenInfo);
        refreshCookie.setPath("/");
        refreshCookie.setSecure(true);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(60*60*30);
        response.addCookie(refreshCookie);
    }
    //  JWT 토큰을 파싱하여 클레임(Claims) 객체로 변환하는 메소드
    // 
    public Claims parseClaim(String token) {
        log.info("parseClaim 메소드 작동");
        try {
            //토큰이 유효한지 검증하고 Claim 객체를 반환
            // parserBuilder() -> JWT 파서 빌더를 생성 
            // setSiginKey(key) -> 파싱 및 검증에 사용할 서명 키를 설정
            // parseClaimsJws(token) -> 주어진 토큰(token)을 파싱하여 검증
            // getBody() -> 검증된 JWT의 본문(Claims==Payload)을 반환
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new CustomException(FAIL_TOKEN_CHECK);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ACCESS_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(FAIL_TOKEN_CHECK);
        } catch (IllegalArgumentException e) {
            throw new CustomException(FAIL_TOKEN_CHECK);
        }
    }

//    public Claims parseClaimRefreshToken(String token) {
//        try {
//            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            throw new CustomException(INVALID_REFRESH_TOKEN);
//        } catch (UnsupportedJwtException e) {
//            throw new CustomException(UNSUPPORT_REFRESH_TOKEN);
//        } catch (IllegalArgumentException e) {
//            throw new CustomException(EMPTY_REFRESH_TOKEN);
//        }
//    }
    public Authentication getAuthentication(String token){
        String userName = this.parseToken(token);
        if(userName == null){
            throw new CustomException(MISMATCH_USERNAME_TOKEN);
        }
        UserDetails userDetails=customLoadUserByUsername.loadUserByUsername(userName);
        if (!userDetails.getUsername().equals(userName)) {
            throw new CustomException(MISMATCH_USERNAME_TOKEN);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    public boolean vaildateToken(String token) throws JsonProcessingException {
        if(!StringUtils.hasText(token))
            throw new CustomException(FAIL_TOKEN_CHECK);
        else if(this.parseClaim(token).getExpiration().before(new Date())){
            throw new CustomException(ACCESS_TOKEN_EXPIRED);
        }
        System.out.println("** vaildateToken 메소드 종료 **");
        return true;
    }
    public boolean vaildateRefreshToken(String token) throws JsonProcessingException {
        if(!StringUtils.hasText(token))
            throw new CustomException(REFRESH_TOKEN_NOT_FOUND);
        else if(this.parseClaim(token).getExpiration().before(new Date())){
            throw new CustomException(TIMELIMIT_REFRESH_TOKEN);
        }
        System.out.println("** vaildateToken 메소드 종료 **");
        return true;
    }

}


