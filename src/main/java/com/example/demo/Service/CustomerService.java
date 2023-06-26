package com.example.demo.Service;

import com.example.demo.domain.Customer;
import com.example.demo.dto.Request.CardInfoRequestDto;
import com.example.demo.dto.Request.CustomerSaveRequestDto;
import com.example.demo.dto.Response.CustomerResponseDto;
import com.example.demo.dto.chat.ChatFindUserResponseDto;
import com.example.demo.exception.Exception.DuplicateException;
import com.example.demo.exception.errorCode.ControllerErrorCode;
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
        log.info("save");
        if(customerRepo.existsByUserId(customerSaveRequestDto.getUserId())) {
            log.info("existsByUserId");
            throw new DuplicateException(ControllerErrorCode.USERID_DUPLICATION);
        }

        if(customerRepo.existsByEmail(customerSaveRequestDto.getEmail())) {
            log.info("existsByEmail");
            throw new DuplicateException(ControllerErrorCode.EMAIL_DUPLICATION);
        }

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
//        if(list.isEmpty()){
//            new UserAuthException(ExceptionMessage.NOT_FOUND_USER);
//        }
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
        Optional<Customer> customer= customerRepo.findByEmail(email);
        //소셜로 회원가입할 소셜 유저는 이메일, 소셜 값만 존재해야한다.
        //만약 다른 사용자가 소셜 유저의 이메일을 입력할 수 있기 떄문이다.(업데이트 되버린다.)
        if(customer.isPresent() && customer.get().getUserId().isEmpty()){
            return customer.get();
        }
        return null;

    }
    @Transactional(readOnly = true)
    public boolean existToSocialFromEmail(String email){
        Customer customer=findByEmailToCustomer(email);
        if(customer==null)
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
