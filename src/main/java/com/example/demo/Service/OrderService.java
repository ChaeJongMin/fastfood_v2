package com.example.demo.Service;

import com.example.demo.domain.Basket;
import java.util.Calendar;
import com.example.demo.domain.Orders;
import com.example.demo.dto.Response.OrdersResponseDto;
import com.example.demo.dto.Response.PageDto;
import com.example.demo.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrdersRepository ordersRepo;
    private final BasketRepository basketRepository;
    private final SizeRepository sizeRepository;
    private final TemperRepository temperRepository;
    private final OptionInfoRepo optionInfoRepo;
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
    //현재 월 부터 지난 12개월 간 년.월을 구하는 메소드
    public List<String> get12LastMonth(){
        //람다식 사용
        //Calendar 객체를 사용하여 날짜를 구했습니다.
        //자바에서는 date 객체보다 새롭게 만들어진 Calendar 객체 사용을 추천하고 있다.
        return java.util.stream.IntStream.range(0,12)
                .mapToObj(i->{
                    Calendar calendar=Calendar.getInstance();
                    //현재 월에서 -i를 하여 원하는 날짜를 얻는다.
                    calendar.add(Calendar.MONTH, -i);
                    return calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1);
                })
                //리스트 형태로 변환
                .collect(Collectors.toList());
    }
    //Page 객체를 얻는 메소드
    @Transactional(readOnly = true)
    public Page<OrdersResponseDto> getOrdersInfo(int year, int month, Pageable pageable){

        Page<OrdersResponseDto> ordersDtoPage=null;
        //page 객체를 얻기위해 쿼리메소드에 pageable 객체를 인자로 넘겨줘야한다.
        ordersDtoPage=ordersRepo.findByOrdersMonth(year,month,pageable).map(orders -> new OrdersResponseDto(orders,setEtc(orders)));
        return ordersDtoPage;
    }
    //pagination을 위한 페이지 dto를 만드는 메소드
    public PageDto makePageDto(int currentPage, int mostEndPage){
        return PageDto.builder()
                .currentPage(currentPage)
                .mostEndPage(mostEndPage)
                .build();
    }

    //구매한 제품의 옵션 예를 들어 세트이면 사이드, 드링크 메뉴, 사이즈 등을 문자열로 얻어오는 메소드
    public String setEtc(Orders orders){
        String etc="";
        String[] option=orders.getInfo().split(",");
        //사이즈
        etc+="size:"+optionInfoRepo.findById(Integer.parseInt(option[0])).get().getSize().getSizename();
        //온도
        etc+=" 온도:"+optionInfoRepo.findById(Integer.parseInt(option[0])).get().getTemperature().getTempname();
        //세트일 시
        if(orders.getProduct().getCategories().getCategoryId()==6 ){
            //사이드
            etc+=" 사이드:"+optionInfoRepo.findById(Integer.parseInt(option[1])).get().getProduct().getProductName();
            //드링크
            etc+=" 음료:"+optionInfoRepo.findById(Integer.parseInt(option[2])).get().getProduct().getProductName();
        }
        return etc;
    }
    //특정 날짜의 총 판매액을 구하는 메소드
    @Transactional(readOnly = true)
    public long getTotalPrice(int month,int year){
        return ordersRepo.sumCurrentPriceByMonthOrders(month,year);
    }
}
