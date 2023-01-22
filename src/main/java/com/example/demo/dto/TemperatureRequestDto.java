package com.example.demo.dto;

import com.example.demo.domain.Temperature;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TemperatureRequestDto {
    private String tempname;

    @Builder
    public TemperatureRequestDto(String tempname){
        this.tempname=tempname;
    }

    public Temperature toTemperatureEntity(){
        return Temperature.builder()
                .tempname(tempname)
                .build();
    }
}
