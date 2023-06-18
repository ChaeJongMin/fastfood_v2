package com.example.demo;

import com.example.demo.Service.ChatService;
import com.example.demo.Service.CustomerService;
import com.example.demo.Service.OrderService;
import com.example.demo.Service.ProductService;
import com.example.demo.domain.*;
import com.example.demo.dto.Response.OrdersResponseDto;
import com.example.demo.dto.Response.ProductResponseDto;
import com.example.demo.dto.chat.ChatChattedDto;
import com.example.demo.dto.chat.ChatFindUserResponseDto;
import com.example.demo.dto.chat.ChatUserDto;
import com.example.demo.persistence.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class QueryTest {
    @Autowired
    private WorkerRepository WorkerRepo;
    @Autowired
    private CustomerRepository CustomerRepo;
    @Autowired
    private ProductRepository ProductRepo;
    @Autowired
    private CategoriesRepository CategoRepo;
    @Autowired
    private BasketRepository basketRepo;
    @Autowired
    private ProductImageRepository ProImgRepo;
    @Autowired
    private ConnectCustomerRepository connectCustomerRepository;
    @Autowired
    private OptionInfoRepo OptionRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private BoardsRepository boardsRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    @Transactional
    public void queryTest() {
//        ProductRepo.deleteByPid(53);

//        ProductRepo.delete(product);
    }

}
