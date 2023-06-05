package com.example.demo.Controller.Api;

import com.example.demo.Service.ProductService;
import com.example.demo.dto.Request.ProductSaveRequestDto;
import com.example.demo.dto.Request.ProductUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class productApiController {
    private final ProductService productService;

    @PostMapping(value="/api/product")
    public int save(@RequestPart(value="productData") ProductSaveRequestDto requestDto,
                    @RequestPart(value="file") MultipartFile file) throws IOException {
        log.info("가져온 값: "+requestDto.getName()+" "+requestDto.getCateName()+" "+requestDto.getPrice());

        //제품 저장
        int result=productService.save(requestDto,file);
        return 1;
    }

    @PutMapping(value="/api/product/{id}" )
    public int update(@RequestPart(value="productData") ProductUpdateRequestDto requestDto,
                      @RequestPart(value="file",required = false) MultipartFile file, @PathVariable int id) throws IOException {
        productService.update(id,requestDto,file);
        log.info(requestDto.getPrice()+" "+id);
        return id;
    }


    @DeleteMapping("/api/product/{id}")
    public int delete(@PathVariable int id){
        productService.delete(id);
        log.info("가져온 제품 아이디 "+id);
        return id;
    }

}
