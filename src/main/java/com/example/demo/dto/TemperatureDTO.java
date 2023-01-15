package com.example.demo.dto;

import com.example.demo.domain.Temperature;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemperatureDTO {
    private Integer tId;
    private String tempname;

    public TemperatureDTO(Temperature temperatureEntity){
        this.tId= temperatureEntity.getTId();
        this.tempname=temperatureEntity.getTempname();
    }
}
