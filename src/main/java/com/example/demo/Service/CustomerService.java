package com.example.demo.Service;

import com.example.demo.domain.Customer;
import com.example.demo.dto.CustomerDto;
import com.example.demo.dto.CustomerRequestDto;
import com.example.demo.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepo;
    //private final CustomerDto customerDto;
    //private final CustomerRequestDto customerRequestDto;
    public boolean findUsers(String userId, String userPasswd){
        Customer customer=customerRepo.findByUserId(userId).get(0);
        if(customer!=null){
            if(customer.getUserPasswd().equals(userPasswd))
                return true;
        }
        return false;
    }
    public CustomerDto getUser(String userId){
        Customer customer=customerRepo.findByUserId(userId).get(0);
        return new CustomerDto(customer);
    }
    public void updateUserInfo(int id, CustomerRequestDto customer){
        Customer c=customerRepo.findById(id).get();
        c.setPhoneNum(customer.getPhoneNum());
        c.setCardCompany(customer.getCardCompany());
        c.setCardNum(customer.getCardNum());
        c.setEmail(customer.getEmail());
        c.setUserId(customer.getUserId());
        c.setUserPasswd(customer.getUserPasswd());
        customerRepo.save(c);
    }
    public void saveUserInfo(CustomerRequestDto customer){
        customerRepo.save(customer.toEntitiy());
    }

}
