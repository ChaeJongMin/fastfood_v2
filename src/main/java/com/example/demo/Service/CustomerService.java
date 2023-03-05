package com.example.demo.Service;

import com.example.demo.domain.Customer;
import com.example.demo.dto.*;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
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
    public int getUserPkId(String userId){
        return customerRepo.findByUserId(userId).get(0).getId();
    }
    /********************************************************************************************/
    @Transactional
    public int save(CustomerSaveRequestDto customerSaveRequestDto){
        customerSaveRequestDto.setEncorderPasswd(passwordEncoder.encode(customerSaveRequestDto.getUserPasswd()));
        return customerRepo.save(customerSaveRequestDto.toCustomerEntitiy()).getId();
    }
    @Transactional(readOnly = true)
    public CustomerResponseDto findById(int id){
        return new CustomerResponseDto(customerRepo.findById(id).get());
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

}
