package com.example.demo.Service;

import com.example.demo.domain.Basket;
import com.example.demo.domain.Product;
import com.example.demo.domain.Product_option_info;
import com.example.demo.dto.*;
import com.example.demo.persistence.OptionInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductOptionService {
    private final OptionInfoRepo optionInfoRepo;

    public Iterable<Product_option_info> findAllOption(){
        return optionInfoRepo.findAll();
    }

    public ProductOptionInfoRequestDto getOption(ProductRequsetDto productRequsetDto, SizeRequestDTO sizeRequestDTO,
                                             TemperatureRequestDto temperatureRequestDto){
        Product_option_info product_option_info=optionInfoRepo.findOptionbyinfos(productRequsetDto.toProductEntitiy(),
                sizeRequestDTO.toSizeEntity(), temperatureRequestDto.toTemperatureEntity()).get(0);
        ProductOptionInfoRequestDto productOptionInfoRequestDto=new ProductOptionInfoRequestDto(product_option_info.getProduct(),
                product_option_info.getSize(), product_option_info.getTemperature(), product_option_info.isUse(),
                product_option_info.getPrice());
        return productOptionInfoRequestDto;

    }
    public String infotoString(ProductOptionInfoRequestDto[] productOptionInfoRequestDto){
        String result="";
        result+=productOptionInfoRequestDto[0].productOptionInfoEntity().getInfoid()+",";
        result+=productOptionInfoRequestDto[1].productOptionInfoEntity().getInfoid()+",";
        result+=productOptionInfoRequestDto[2].productOptionInfoEntity().getInfoid();
        return result;
    }
    public List<Product_option_info> addProductOItoBakset(List<Basket> basketList){
        List<Product_option_info> productOptionInfoList=new ArrayList<>();
        for(Basket b:basketList){
            productOptionInfoList.add(optionInfoRepo.findById(b.getProductinfo().getInfoid()).get());
        }
        return productOptionInfoList;
    }
    public ArrayList<Integer> priceToBasket(List<Basket> basketList){
        ArrayList<Integer> priceList=new ArrayList<>();
        for(Basket b:basketList) {
            priceList.add(optionInfoRepo.findById(b.getProductinfo().getInfoid()).get().getPrice()*b.getPCount());
        }
        return priceList;
    }
    public Map<Integer,String[]> findBySetMenu(List<Basket> basketList) {
        Map<Integer, String[]> map = new HashMap<>();
        for (Basket b : basketList) {
            if (b.getProductinfo().getProduct().getCategories().getCategoryName().equals("세트")) {

                String[] setpanme = b.getInfo().split(",");

                String[] sidemenu = {optionInfoRepo.findById(Integer.parseInt(setpanme[1])).get().getProduct().getProductName()
                        , optionInfoRepo.findById(Integer.parseInt(setpanme[2])).get().getProduct().getProductName()};
                map.put(b.getBid(), sidemenu);
            }
        }
        return map;
    }
}
