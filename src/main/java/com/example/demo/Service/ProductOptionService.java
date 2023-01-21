package com.example.demo.Service;

import com.example.demo.domain.Product;
import com.example.demo.domain.Product_option_info;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.Prouduct_option_infoDTO;
import com.example.demo.dto.SizeDTO;
import com.example.demo.dto.TemperatureDTO;
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

//    public Prouduct_option_infoDTO getOption(ProductDTO productDTO, SizeDTO sizeDTO, TemperatureDTO temperatureDTO){
//
//
//    }
}
