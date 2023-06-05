package com.example.demo.dto.Request;

import com.example.demo.domain.Basket;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter

public class ProductSaveToMenuRequestDto {
    private int pid;
    private int size;
    private int temp;
    private int side;
    private int drink;
    private int price;

}
