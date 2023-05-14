package com.example.demo.Service;

import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.config.auth.jwt.domain.RefreshTokens;
import com.example.demo.domain.Customer;
import com.example.demo.dto.*;
import com.example.demo.exception.UserAuthException;
import com.example.demo.exception.message.ExceptionMessage;
import com.example.demo.persistence.CustomerRepository;
import com.example.demo.persistence.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    //private final CustomerDto customerDto;
    //private final CustomerRequestDto customerRequestDto;
//    public boolean findUsers(String userId, String userPasswd){
//        Customer customer=customerRepo.findByUserId(userId).get(0);
//        if(customer!=null){
//            if(customer.getUserPasswd().equals(userPasswd))
//                return true;
//        }
//        return false;
//    }
//    public CustomerDto getUser(String userId){
//        Customer customer=customerRepo.findByUserId(userId).get(0);
//        return new CustomerDto(customer);
//    }
//    public void updateUserInfo(int id, CustomerRequestDto customer){
//        customerRepo.save(customer.toCustomerEntitiy());
//    }
//    public void saveUserInfo(CustomerRequestDto customer){
//        customerRepo.save(customer.toCustomerEntitiy());
//    }
//    public String currentUserId(HttpSession session){
//        return ((Customer)session.getAttribute("user")).getUserId();
//    }
//    public String findByIds(int id){
//        return customerRepo.findById(id).get().getUserId();
//    }
//    public CustomerDto getCurCustomer(HttpSession session){
//        return new CustomerDto((Customer)session.getAttribute("user"));
//    }
//    public int checkCardInfo(String cardCompany, String cardNumber,HttpSession session){
//        int isCheck=0;
//        if(!cardNumber.equals("")&&!cardCompany.equals("")) {//빈칸 X
//            int checkResult=checkUserPayInfo(cardNumber,cardCompany,
//                    getCurCustomer(session)); //체크함수
//            switch(checkResult) {
//                case 1:
//                    isCheck=1;
//                    break;
//                case -1:
//                    isCheck=-1;
//                    break;
//                case -2:
//                    isCheck=-2;
//                    break;
//            }
//        }
//        else { //빈칸 O
//            isCheck=-3;
//        }
//        return isCheck;
//    }
//    public int checkUserPayInfo(String cardnum, String cardCompany,CustomerDto customerDto){
//        if(!cardnum.equals(customerDto.getCardNum())) {
//            return -1;
//        }
//        if(!cardCompany.equals(customerDto.getCardCompany())) {
//            return -2;
//        }
//        return 1; //성공
//    }
//    public CustomerRequestDto convertCustomer(CustomerDto customerDto){
//        CustomerRequestDto customerRequestDto=new CustomerRequestDto(customerDto.getUserId(),customerDto.getUserPasswd()
//        ,customerDto.getEmail(),customerDto.getCardNum(),customerDto.getCardCompany(),customerDto.getPhoneNum(),
//                customerDto.getRole());
//        return customerRequestDto;
//    }
//

    /********************************************************************************************/
    public Customer auth(String id, String password){

        if(!customerRepo.existsByUserId(id)){
            throw new UserAuthException(ExceptionMessage.NOT_FOUND_USER);
        }
        Customer customer=customerRepo.findByUserId(id).get(0);
        if(!passwordEncoder.matches(password , customer.getUserPasswd())){
            throw new UserAuthException(ExceptionMessage.MISMATCH_PASSWORD);
        }
        return customer;
    }

    @Transactional
    public void login(String id, String password, String rtoken, HttpServletResponse response) {
        String userId=this.auth(id,password).getUserId();
        RefreshTokens refreshToken=null;
        String refreshTokenValue=rtoken;
//        로그인한 유저의 리프레쉬 토큰이 일단 존재할 시
        if(!rtoken.equals("not exist") || refreshTokenService.findRefreshTokenbyUser(id)){
            //리프레쉬 토큰이 만료 또는 유효하지 않을 경우
            if(refreshTokenService.checkExpireTime(jwtTokenProvider.getExpiredTime(rtoken))){
                //새로 업데이트
                refreshTokenValue=jwtTokenProvider.refreshGenerateToken(userId,jwtTokenProvider.REFRESH_TIME);
                refreshTokenService.update(userId,refreshTokenValue,jwtTokenProvider.getExpiredTime(refreshTokenValue));
            }
        } else{
            refreshTokenValue=refreshTokenService.save(userId, JwtTokenProvider.REFRESH_TIME).getValue();
        }
        jwtTokenProvider.setAccessTokenAndRefreshToken(jwtTokenProvider.generateToken(userId,jwtTokenProvider.ACCESS_TIME),
                refreshTokenValue,response);
    }
    @Transactional(readOnly = true)
    public void logout(String accessToken){
        log.info("로그아웃 서비스");
        String userId=jwtTokenProvider.parseToken(jwtTokenProvider.resolveToken(accessToken));
        log.info("가져온 유저아이디: "+userId);
        refreshTokenRepository.deleteByKeyId(userId);

    }

    @Transactional(readOnly = true)
    public int getUserPkId(String userId){
        return customerRepo.findByUserId(userId).get(0).getId();
    }
    @Transactional
    public int save(CustomerSaveRequestDto customerSaveRequestDto){
        customerSaveRequestDto.setEncorderPasswd(passwordEncoder.encode(customerSaveRequestDto.getUserPasswd()));
        return customerRepo.save(customerSaveRequestDto.toCustomerEntitiy()).getId();
    }
    @Transactional(readOnly = true)
    public CustomerResponseDto findById(int id){
        return new CustomerResponseDto(customerRepo.findById(id).get());
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto findByUserId(String userId){
        List<Customer> list= customerRepo.findByUserId(userId);
        if(!list.isEmpty()){
            new UserAuthException(ExceptionMessage.NOT_FOUND_USER);
        }
        return new CustomerResponseDto(customerRepo.findById(list.get(0).getId()).get());
    }

    @Transactional
    public int update(int id, CustomerSaveRequestDto customerRequestDto){
        Customer customer=customerRepo.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 사용자가 없습니다."));
        customer.update(customerRequestDto.getUserId()
                ,customerRequestDto.getEmail(), customerRequestDto.getCardNum(), customerRequestDto.getCardCompany(),
                        customerRequestDto.getPhoneNum());
                return id;
    }
    @Transactional
    public boolean checkCardInfo(int userId,CardInfoRequestDto cardInfoRequestDto){
        System.out.println(userId);
        Customer customer=customerRepo.findById(userId).orElseThrow(()->
                new IllegalArgumentException("해당 유저는 존재하지 않습니다.."));
        System.out.println(customer.getUserId());
        if(customer.getCardCompany().equals(cardInfoRequestDto.getCardCompany()) &&
        customer.getCardNum().equals(cardInfoRequestDto.getCardNum())){
            return true;
        }
        return false;
    }
    public boolean compareWriter(String writer, String target){
        if(writer.equals(target)){
            return true;
        }
        return false;
    }
    @Transactional(readOnly = true)
    public String findByidForUserId(int id){
        return customerRepo.findById(id).get().getUserId();
    }
    @Transactional(readOnly = true)
    public Customer findByEmailToCustomer(String email){
        return customerRepo.findByEmail(email).orElseThrow(()->
                new IllegalArgumentException("해당 유저는 없습니다."));
    }
    @Transactional(readOnly = true)
    public boolean existToSocialFromEmail(String email){
        Customer customer=findByEmailToCustomer(email);
        if(customer.getSocialId().equals("empty"))
            return false;
        return true;
    }

    @Transactional
    public int updateForInitSocialUser(CustomerSaveRequestDto customerRequestDto){
        Customer customer=findByEmailToCustomer(customerRequestDto.getEmail());
        customerRequestDto.setEncorderPasswd(passwordEncoder.encode(customerRequestDto.getUserPasswd()));
        customer.updateForSocial(customerRequestDto.getUserId()
                ,customerRequestDto.getUserPasswd(), customerRequestDto.getCardNum(), customerRequestDto.getCardCompany(),
                customerRequestDto.getPhoneNum());
        return customer.getId();
    }
}
