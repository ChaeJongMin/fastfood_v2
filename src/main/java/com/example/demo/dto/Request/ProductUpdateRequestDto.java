package com.example.demo.dto.Request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class ProductUpdateRequestDto {
    private int price;
    private boolean allSale;
}
