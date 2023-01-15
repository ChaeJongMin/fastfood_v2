package com.example.demo.dto;

import com.example.demo.domain.Product;
import com.example.demo.domain.Product_option_info;
import com.example.demo.domain.Size;
import com.example.demo.domain.Temperature;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Prouduct_option_infoDTO {
    private Integer infoid;//PK
    private Product product;
    private Size size;
    private Temperature temperature;
    private boolean isUse=true; //옵션 사용 여부
    private Integer price;

    public Prouduct_option_infoDTO(Product_option_info pdoptinfEntity){
        this.infoid= pdoptinfEntity.getInfoid();
        this.product= pdoptinfEntity.getProduct();
        this.size= pdoptinfEntity.getSize();
        this.temperature= pdoptinfEntity.getTemperature();
        this.isUse= pdoptinfEntity.isUse();
        this.price= pdoptinfEntity.getPrice();
    }
}
