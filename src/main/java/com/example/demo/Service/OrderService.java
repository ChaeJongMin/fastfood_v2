package com.example.demo.Service;

import com.example.demo.domain.Basket;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Orders;
import com.example.demo.persistence.BasketRepository;
import com.example.demo.persistence.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrdersRepository ordersRepo;
    private final BasketRepository basketRepository;
    //세이브
//    public void saveOrder(List<Basket> basketList, CustomerRequestDto c){
//        CustomerDto customer;
//        for(Basket b:basketList){
//            Orders o=new Orders();
//            o.setCustomer(c.toCustomerEntitiy());
//            o.setProduct(b.getProductinfo().getProduct());
//            o.setPrice(b.getPrice());
//            o.setInfo(b.getInfo());
////            o.setOrederDate(new Date());
//            ordersRepo.save(o);
//        }
//    }
    /*********************************************************************/
    @Transactional
    public int save(int userId){
        List<Basket> list=basketRepository.findByCustomer_Id(userId);
        for(Basket b:list){
            Orders orders=Orders.builder()
                    .price(b.getPrice())
                    .customer(b.getCustomer())
                    .product(b.getProductinfo().getProduct())
                    .info(b.getInfo())
                    .build();
            ordersRepo.save(orders);
            basketRepository.delete(b);
        }
        return userId;
    }


}
