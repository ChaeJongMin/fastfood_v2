package com.example.demo.Service;

import com.example.demo.domain.Size;
import com.example.demo.dto.SizeDTO;
import com.example.demo.persistence.SizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SizeService {
    private  final SizeRepository sizeRepository;

    public SizeDTO getSize(String size){
        Size sizes=sizeRepository.findBySizename(size).get(0);
        return new SizeDTO(sizes);
    }
}
