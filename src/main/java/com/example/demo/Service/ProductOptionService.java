package com.example.demo.Service;

import com.example.demo.domain.Basket;
import com.example.demo.dto.Response.BasketResponseDto;
import com.example.demo.dto.Response.ProductOptionResponseDto;
import com.example.demo.dto.Response.ProductSideAndDrinkResponseDto;
import com.example.demo.persistence.BasketRepository;
import com.example.demo.persistence.OptionInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOptionService {
    private final OptionInfoRepo optionInfoRepo;
    private final BasketRepository basketRepository;

//    public Iterable<Product_option_info> findAllOption(){
//        return optionInfoRepo.findAll();
//    }
//
//    public ProductOptionInfoRequestDto getOption(ProductRequsetDto productRequsetDto, SizeRequestDTO sizeRequestDTO,
//                                             TemperatureRequestDto temperatureRequestDto){
//        Product_option_info product_option_info=optionInfoRepo.findOptionbyinfos(productRequsetDto.toProductEntitiy(),
//                sizeRequestDTO.toSizeEntity(), temperatureRequestDto.toTemperatureEntity());
//        ProductOptionInfoRequestDto productOptionInfoRequestDto=new ProductOptionInfoRequestDto(product_option_info.getProduct(),
//                product_option_info.getSize(), product_option_info.getTemperature(), product_option_info.isUse(),
//                product_option_info.getPrice());
//        return productOptionInfoRequestDto;
//
//    }
//    public String infotoString(ProductOptionInfoRequestDto[] productOptionInfoRequestDto){
//        String result="";
//        result+=productOptionInfoRequestDto[0].productOptionInfoEntity().getInfoid()+",";
//        result+=productOptionInfoRequestDto[1].productOptionInfoEntity().getInfoid()+",";
//        result+=productOptionInfoRequestDto[2].productOptionInfoEntity().getInfoid();
//        return result;
//    }
//    public List<Product_option_info> addProductOItoBakset(List<Basket> basketList){
//        List<Product_option_info> productOptionInfoList=new ArrayList<>();
//        for(Basket b:basketList){
//            productOptionInfoList.add(optionInfoRepo.findById(b.getProductinfo().getInfoid()).get());
//        }
//        return productOptionInfoList;
//    }
//    public ArrayList<Integer> priceToBasket(List<Basket> basketList){
//        ArrayList<Integer> priceList=new ArrayList<>();
//        for(Basket b:basketList) {
//            priceList.add(optionInfoRepo.findById(b.getProductinfo().getInfoid()).get().getPrice()*b.getPCount());
//        }
//        return priceList;
//    }
//    public Map<Integer,String[]> findBySetMenu(List<Basket> basketList) {
//        Map<Integer, String[]> map = new HashMap<>();
//        for (Basket b : basketList) {
//            if (b.getProductinfo().getProduct().getCategories().getCategoryName().equals("세트")) {
//
//                String[] setpanme = b.getInfo().split(",");
//
//                String[] sidemenu = {optionInfoRepo.findById(Integer.parseInt(setpanme[1])).get().getProduct().getProductName()
//                        , optionInfoRepo.findById(Integer.parseInt(setpanme[2])).get().getProduct().getProductName()};
//                map.put(b.getBid(), sidemenu);
//            }
//        }
//        return map;
//    }
    /*****************************************************************************************************/
    @Transactional(readOnly = true)
    public List<ProductOptionResponseDto> findByCustomerForProductOption(int userId){
        List<Basket> basketList=basketRepository.findByCustomer_Id(userId);
        List<ProductOptionResponseDto> result=new ArrayList<>();
        for(Basket basket:basketList){
            result.add(new ProductOptionResponseDto(basket.getProductinfo()));
        }
        return result;
    }
    @Transactional(readOnly = true)
    public List<ProductSideAndDrinkResponseDto> setForSideAndDrinkName(List<BasketResponseDto> basketList, int basketListSize){
        List<ProductSideAndDrinkResponseDto> result=new ArrayList<>();
        String sideName="";
        String drinkName="";
        for(int i=0; i<basketListSize; i++){
            if(basketList.get(i).getDrinkId()==1 && basketList.get(i).getSideId()==1 ) {
                sideName="없음";
                drinkName="없음";
            } else{
                sideName=optionInfoRepo.getById(basketList.get(i).getSideId()).getProduct().getProductName();
                drinkName=optionInfoRepo.getById(basketList.get(i).getDrinkId()).getProduct().getProductName();

            }
            result.add(new ProductSideAndDrinkResponseDto(sideName,drinkName));
        }
        return result;
    }
}
