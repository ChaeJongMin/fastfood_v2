package com.example.demo.Service;

import com.example.demo.domain.Temperature;
import com.example.demo.dto.TemperatureDTO;
import com.example.demo.dto.TemperatureRequestDto;
import com.example.demo.persistence.TemperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemperatureService {
    private final TemperRepository temperRepository;

    public TemperatureRequestDto getTemperature(String temp){
        Temperature temperature=temperRepository.findByTempname(temp).get(0);
        TemperatureRequestDto temperatureRequestDto=new TemperatureRequestDto(temperature.getTempname());
        return temperatureRequestDto;
    }
}
