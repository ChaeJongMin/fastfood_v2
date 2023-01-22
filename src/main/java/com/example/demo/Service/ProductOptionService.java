package com.example.demo.Service;

import com.example.demo.domain.Product;
import com.example.demo.domain.Product_option_info;
import com.example.demo.dto.*;
import com.example.demo.persistence.OptionInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
