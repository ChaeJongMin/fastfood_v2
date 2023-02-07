package com.example.demo.Service;

import com.example.demo.domain.Basket;
import com.example.demo.domain.Customer;
import com.example.demo.domain.Product;
import com.example.demo.domain.Product_option_info;
import com.example.demo.dto.ProductOptionInfoRequestDto;
import com.example.demo.dto.Prouduct_option_infoDTO;
import com.example.demo.persistence.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;

    public void saveSingleMenu(ProductOptionInfoRequestDto productOptionInfoRequestDto, HttpSession session){
        Basket basket = new Basket();
        basket.setProductinfo(productOptionInfoRequestDto.productOptionInfoEntity());
        basket.setCustomer((Customer)session.getAttribute("user"));
        basket.setPCount(1);
        basket.setInfo(String.valueOf(productOptionInfoRequestDto.productOptionInfoEntity().getInfoid()));
        basket.setPrice(productOptionInfoRequestDto.getPrice());
        basketRepository.save(basket);
    }
    public void saveSetMenu(ProductOptionInfoRequestDto productOptionInfoRequestDto, HttpSession session,String info){
        Basket basket = new Basket();
        basket.setProductinfo(productOptionInfoRequestDto.productOptionInfoEntity());
        basket.setCustomer((Customer)session.getAttribute("user"));
        basket.setPCount(1);
        basket.setInfo(info);
        basket.setPrice(productOptionInfoRequestDto.getPrice());
        basketRepository.save(basket);
    }

    public List<Basket>  basketDeleteDuplicationToItem(HttpSession session){
        List<Basket> basketList=getBasketListByUser(session);
        int basketListSize=basketList.size();
        for(int i=0;i<basketListSize; i++) {
            for (int j = i + 1; j < basketListSize; j++) {
                if(basketList.get(i).getCustomer().getId()==basketList.get(i).getCustomer().getId()){
                    basketList.get(i).setPCount(basketList.get(i).getPCount()+1); //수량 증가
                    basketRepository.save(basketList.get(i)); // 정보 변경
                    basketRepository.deleteById(basketList.get(j).getBid()); //중복된 아이템 테이블에서 제거
                    basketList.remove(j); //중복된 아이템 리스트에서 제거
                }
            }
        }
        return basketList;
    }
    public List<Basket> getBasketListByUser(HttpSession session){
        return basketRepository.findByCustomer((Customer)session.getAttribute("user"));
    }
    public int getTotalProductCnt(List<Basket> basketList){
        int result=0;
        for(Basket b:basketList) {
            result+=b.getPCount();
        }
        return result;
    }

    public List<Basket> saveChangeBakset(List<String> basketArray){
        List<Basket> basketList=new ArrayList<>();
        for(String b : basketArray){
            basketList.add(basketRepository.findById(Integer.parseInt(b)).get());
        }
        return basketList;
    }
    public void insertChangeBasket(List<Basket> basketList,List<String> counts){
        for (int i = 0; i < basketList.size(); i++) { //다시 가져온 바켓 객체 데베에 삽입
            Basket newbasket = new Basket();
            newbasket = basketList.get(i);
            newbasket.setPCount(Integer.parseInt((counts.get(i))));
            System.out.println("바스켓 아이디: " + newbasket.getBid() + " 바스켓 수량: " + newbasket.getPCount());
            basketRepository.save(newbasket);
        }
    }
    public void deleteBakset(List<Basket> basketList){
        for (Basket b : basketList) {
            basketRepository.delete(b);
        }
    }

    public int calcTotalPrice(List<Basket> basketList){
        int totalprice=0;
        for(Basket b:basketList) {
            int price=b.getProductinfo().getPrice();
            price=price*b.getPCount();
            totalprice+=price;
        }

        return totalprice;
    }

}
