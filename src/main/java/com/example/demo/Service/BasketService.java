package com.example.demo.Service;

import com.example.demo.domain.*;
import com.example.demo.dto.Request.BasketSaveRequestDto;
import com.example.demo.dto.Request.BasketUpdateRequestDto;
import com.example.demo.dto.Request.ProductSaveToMenuRequestDto;
import com.example.demo.dto.Response.BasketResponseDto;
import com.example.demo.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final OptionInfoRepo optionInfoRepo;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final TemperRepository temperRepository;

    /************************************************************************************************************/
    @Transactional
    public int save(ProductSaveToMenuRequestDto productSaveToMenuRequestDto, int userId){
        int result=0;
        boolean setFlag=setCheck(productSaveToMenuRequestDto.getSide(),productSaveToMenuRequestDto.getDrink());
        int price=0;
        String infoStr="";
        Product_option_info poi=null;
        //단품일 시
        if(!setFlag) {
            poi=optionInfoRepo.findByProduct_PidAndSize_SidAndTemperature_Tid(productSaveToMenuRequestDto.getPid(),
                    productSaveToMenuRequestDto.getSize(),productSaveToMenuRequestDto.getTemp());
            infoStr=String.valueOf(poi.getInfoid());
        }
        //세트일 시
        else{
            System.out.println(productSaveToMenuRequestDto.getPid()+" "+productSaveToMenuRequestDto.getSize());
            poi=optionInfoRepo.findByProduct_PidAndSize_SidAndTemperature_Tid(productSaveToMenuRequestDto.getPid(),
                    productSaveToMenuRequestDto.getSize(),1);
            int sideOptionId=optionInfoRepo.findByProduct_PidAndSize_SidAndTemperature_Tid(productSaveToMenuRequestDto.getSide(),
                    productSaveToMenuRequestDto.getSize(),1).getInfoid();
            //드링크 제품의 옵션
            int drinkOptionId=optionInfoRepo.findByProduct_PidAndSize_SidAndTemperature_Tid(productSaveToMenuRequestDto.getDrink(),
                    productSaveToMenuRequestDto.getSize(),1).getInfoid();

            infoStr=(String.valueOf(poi.getInfoid())+","+sideOptionId+","+drinkOptionId);
        }
        Optional<Basket> basket=basketRepository.findByCustomer_IdAndProductinfo_InfoidAndInfo(userId, poi.getInfoid(),infoStr);

        //저장하고자 하는 아이템이 이미 존재(중복 구별)
        if(basket.isPresent()){
            basket.get().duplicationUpdate();
            result= basket.get().getBid();
        } else {
            //세트, 단품 가격 파악
            if(setFlag){
                price=productSaveToMenuRequestDto.getPrice();

            } else{
                price=poi.getPrice();
            }

            //새로운 아이템
            BasketSaveRequestDto basketSaveRequestDto=BasketSaveRequestDto.builder()
                    .pCount(1)
                    .productOptionInfo(poi)
                    .customer(customerRepository.findById(userId).get())
                    .info(infoStr)
                    .price(price)
                    .build();
            //저장
            result=basketRepository.save(basketSaveRequestDto.toBasketEntitiy()).getBid();
            System.out.println("생성");
        }
        return result;
    }
    public boolean setCheck(int sideId, int drinkId){
        if(sideId==0 && drinkId==0)
            return false;
        return true;
    }
    @Transactional
    public int countByCustomer(int userId){
        return basketRepository.countByCustomerId(userId);
    }

    @Transactional(readOnly = true)
    public List<BasketResponseDto> findByCustomerForBasketItem(int userId){
        return basketRepository.findByCustomer_Id(userId).stream().map(BasketResponseDto::new).collect(Collectors.toList());
    }

    public int[] finalPriceAndCount(List<BasketResponseDto> basketResponseDtoList){
        int price=0;
        int cnt=0;
        for(BasketResponseDto basketResponseDto: basketResponseDtoList){
            price+=basketResponseDto.getTotalPrice();
            cnt+=basketResponseDto.getPCount();
        }
        return new int[]{price,cnt};
    }
    public int TotalPriceByCustomer(int userId){
        int result=0;
        for(Basket b: basketRepository.findByCustomer_Id(userId)){
           result+=b.getPrice();
       }
        return result;
    }
    @Transactional
    public int basketUpdate(List<BasketUpdateRequestDto> list, int userId){
        List<Basket> basketList=basketRepository.findByCustomer_Id(userId);
        if(!basketList.isEmpty()){
            //삭제할 아이템들
            //장바구니 페이지에 해당 아이템을 삭제한 경우
            basketList.stream()
                    .filter(target -> list.stream().noneMatch(compare -> target.getBid()==compare.getBid()))
                    .forEach(deleteB -> basketRepository.delete(deleteB));

            //수량이 변경된 아이템들
            for(Basket updateB : basketList){
                list.stream().filter(compare -> compare.getBid()==updateB.getBid())
                        .forEach(compareB -> updateB.update(compareB.getPcount(),compareB.getPrice()));
            }
        }
        else{
            System.out.println("해당 유저에 아이템이 없습니다.");
            return -1;
        }

        return userId;

    }

    @Transactional
    public int delete(int userId){
        basketRepository.findByCustomer_Id(userId).stream()
                .forEach(target -> basketRepository.delete(target));
        return userId;
    }


}
