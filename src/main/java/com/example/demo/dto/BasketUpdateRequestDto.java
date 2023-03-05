package com.example.demo.dto;

import com.example.demo.domain.Basket;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BasketUpdateRequestDto {
    private int bid;
    private int pcount;
    private int price;

}
