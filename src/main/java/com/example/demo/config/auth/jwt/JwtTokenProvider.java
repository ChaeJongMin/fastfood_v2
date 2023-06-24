package com.example.demo.config.auth.jwt;

import com.example.demo.config.auth.JsonCustomLogin.Service.CustomLoginService;
import com.example.demo.config.auth.jwt.UserData.CustomLoadUserByUsername;
import com.example.demo.config.auth.jwt.domain.RefreshTokens;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.errorCode.RefreshErrorCode;
import com.example.demo.persistence.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.exception.errorCode.ErrorCode.*;
import static com.example.demo.exception.errorCode.RefreshErrorCode.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    // JWT 토큰에 관한 여러 로직이 담긴 클래스
    private final RefreshTokenRepository refreshTokenRepository;

    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";
    public static final long ACCESS_TIME =   1000L * 60*30;
    //1000L * 60 * 60 * 1;
    public static final long REFRESH_TIME =   1000L * 60*60*3;
    //1000L * 60 * 60 * 6
//    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer-";
    private Key key;
    private final CustomLoadUserByUsername customLoadUserByUsername;
    @Value("${jwt.secret}")
    private String secretKey;
    @PostConstruct
    public void init() {
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
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
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }
    //리프레쉬 토큰을 생성하는 메소드
    // 리프레쉬 토큰은 사용자의 정보가 필요없다.
    public String refreshGenerateToken(String userId, long expirationTime){
        Date now=new Date();
        Date expireDate=new Date(now.getTime()+expirationTime);
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256).compact();
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
    // 리프레쉬 토큰의 만료 시간을 얻는 메소드
    public long getRefreshTokenExpiredTime(String token){
        //리프레쉬, 액세스 차별 여부 필요
        //차별을 둔 이유는 서로 다른 예외 메시지를 발급해주기 위해
        long expiredTime=parseClaimRefreshToken(token).getExpiration().getTime();
        log.info("getExpiredTime메소드 : "+expiredTime);
        return expiredTime;
    }
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
    //필요없는 코드이나 커스텀 예외/ 토큰 별로 에러 메시지 확인하기 위해 만들어둠
    public Claims parseClaimRefreshToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(UNSUPPORT_REFRESH_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(EMPTY_REFRESH_TOKEN);
        }
    }

}
//    public String getHeaderToken(HttpServletRequest request, String type) {
//        Cookie[] cookies=request.getCookies();
//        if(cookies==null){
//            return null;
//        }
//        String bearerToken=type.equals("Access") ? cookies[0].getValue() :cookies[1].getValue();
//       return bearerToken;
//    }
//    public TokenDto generateAllToken(String id) {
//        return new TokenDto("Bearer",createToken(id, "Access"),createToken(id, "Refresh"));
//    }
//    public String createToken(String id, String type){
//        System.out.println("--------------------------createToken--------------------------");
//        System.out.println("id: "+id);
//        UserDetails userDetails=customLoadUserByUsername.loadUserByUsername(id);
//
//        System.out.println("id: "+id);
//        String authorities = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//        Date date=new Date();
//        long time= type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;
//        if(type.equals("Access")) {
//            return Jwts.builder()
//                    .setSubject(id)
//                    .claim("auth", authorities)
//                    .setExpiration(new Date(System.currentTimeMillis() + time))
//                    .setIssuedAt(date)
//                    .signWith(key, SignatureAlgorithm.HS256)
//                    .compact();
//        }
//        return Jwts.builder()
//                .setSubject(id)
//                .setExpiration(new Date(System.currentTimeMillis() + time))
//                .setIssuedAt(date)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public Authentication getAuthentication(String accessToken) {
//        //토큰 복호화
//        Claims claims = parseClaims(accessToken);
//
//        if (claims.get("auth") == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }
//
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get("auth").toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
//        System.out.println("getAuthentication : "+principal.getUsername());
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//    }
//    public Authentication setAuthentication(String loginId){
//        UserDetails userDetails= customLoadUserByUsername.loadUserByUsername(loginId);
//        System.out.println("setAuthentication "+userDetails.getUsername());
//        return new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());
//    }
//
//
//    public boolean refreshValidateToken(String token) {
//        if(!validateToken(token)) return false;
//        Optional<RefreshToken> refreshToken =refreshTokenRepository.findByKeyId(getIdFromToken(token));
//        return refreshToken.isPresent() && token.equals(refreshToken.get().getValue());
//    }
//    private Claims parseClaims(String accessToken) {
//        try {
//            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//
//    }
//    public String getIdFromToken(String token){
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
//    }
//    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
//        ResponseCookie accessCookie=ResponseCookie.from("accessToken",accessToken)
//                .maxAge(10)
//                .secure(true)
//                .httpOnly(true)
//                .path("/")
//                .sameSite("None")
//                .build();
//        response.setHeader("Access-Cookie",accessCookie.toString());
//    }
//
//    // 리프레시 토큰 헤더 설정
//    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
//        ResponseCookie refreshCookie=ResponseCookie.from("refreshToken",refreshToken)
//                .maxAge(100)
//                .secure(true)
//                .httpOnly(true)
//                .path("/")
//                .sameSite("None")
//                .build();
//        response.setHeader("Refresh-Cookie",refreshCookie.toString());
//    }

