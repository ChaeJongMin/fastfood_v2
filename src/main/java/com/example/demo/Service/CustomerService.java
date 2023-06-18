package com.example.demo.Service;

//import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.domain.Customer;
import com.example.demo.dto.Request.CardInfoRequestDto;
import com.example.demo.dto.Request.CustomerSaveRequestDto;
import com.example.demo.dto.Response.CustomerResponseDto;
import com.example.demo.dto.chat.ChatFindUserResponseDto;
import com.example.demo.exception.UserAuthException;
import com.example.demo.exception.message.ExceptionMessage;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepo;
    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final RefreshTokenService refreshTokenService;
//    private final RefreshTokenRepository refreshTokenRepository;



    /********************************************************************************************/
//    @Transactional(readOnly = true)
//    public Customer auth(String id, String password){
//
//        if(!customerRepo.existsByUserId(id)){
//            throw new UserAuthException(ExceptionMessage.NOT_FOUND_USER);
//        }
//        Customer customer=customerRepo.findByUserId(id).get(0);
//        if(!passwordEncoder.matches(password , customer.getUserPasswd())){
//            throw new UserAuthException(ExceptionMessage.MISMATCH_PASSWORD);
//        }
//        return customer;
//    }
//
//    @Transactional
//    public Role login(String id, String password, HttpServletResponse response) {
//        Customer customer=this.auth(id,password);
//        String userId=customer.getUserId();
//        Role role=customer.getRole();
//        RefreshTokens refreshToken=null;
//        로그인한 유저의 리프레쉬 토큰이 일단 존재할 시
//        if(!rtoken.equals("not exist") && refreshTokenService.findRefreshTokenbyUser(id)){
//            //리프레쉬 토큰이 만료 또는 유효하지 않을 경우
//            if(refreshTokenService.checkExpireTime(jwtTokenProvider.getRefreshTokenExpiredTime(rtoken))){
//                //새로 업데이트
//                refreshTokenValue=jwtTokenProvider.refreshGenerateToken(userId,jwtTokenProvider.REFRESH_TIME);
//                refreshTokenService.update(userId,refreshTokenValue,jwtTokenProvider.getRefreshTokenExpiredTime(refreshTokenValue));
//            }
//        } else{
//            refreshTokenValue=refreshTokenService.save(userId, JwtTokenProvider.REFRESH_TIME).getValue();
//        }
//
//        String refreshTokenValue=refreshTokenValue=refreshTokenService.save(userId, JwtTokenProvider.REFRESH_TIME).getValue();
//        jwtTokenProvider.setAccessTokenAndRefreshToken(jwtTokenProvider.generateToken(userId,jwtTokenProvider.ACCESS_TIME),
//                refreshTokenValue,response);
//        return role;
//    }
    @Transactional(readOnly = true)
    public void logout(String accessToken){
//        log.info("로그아웃 서비스");
//        String userId=jwtTokenProvider.parseToken(jwtTokenProvider.resolveToken(accessToken));
//        log.info("가져온 유저아이디: "+userId);
//        refreshTokenRepository.ㅇByKeyId(userId);

    }

    @Transactional(readOnly = true)
    public int getUserPkId(String userId){
        return customerRepo.findByUserId(userId).get(0).getId();
    }
    @Transactional
    public int save(CustomerSaveRequestDto customerSaveRequestDto){
        customerSaveRequestDto.setEncorderPasswd(passwordEncoder);
        Customer customer=customerRepo.save(customerSaveRequestDto.toCustomerEntitiy());
        log.info("새로 회원가입한 유저의 권한 "+customer.getRole());
        return customer.getId();
    }
    @Transactional(readOnly = true)
    public CustomerResponseDto findById(int id){
        return new CustomerResponseDto(customerRepo.findById(id).get());
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto findByUserId(String userId){
        List<Customer> list= customerRepo.findByUserId(userId);
        if(list.isEmpty()){
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
    public boolean checkCardInfo(int userId, CardInfoRequestDto cardInfoRequestDto){
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
        customerRequestDto.setEncorderPasswd(passwordEncoder);
        customer.updateForSocial(customerRequestDto.getUserId()
                ,customerRequestDto.getUserPasswd(), customerRequestDto.getCardNum(), customerRequestDto.getCardCompany(),
                customerRequestDto.getPhoneNum());
        return customer.getId();
    }

    @Transactional(readOnly = true)
    public List<ChatFindUserResponseDto> findUserName(String keyword, int myId){
        return customerRepo.findByUserIdContainingIgnoreCase(keyword).stream()
                .map(customer->new ChatFindUserResponseDto(customer,existsChatListByKeyword(myId,customer.getId()))).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existsChatListByKeyword(int myId, int opponentId){
        log.info("가져온 번호 "+myId+" "+opponentId);
        Optional<Integer> result=customerRepo.getChatCount(myId,opponentId);
        if(result.isPresent())
            return true;
        return false;

    }

}
