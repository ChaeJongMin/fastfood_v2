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

    //이미지 파일을 받기위해 @RequestPart 어노테이션을 사용했습니다.
    //그 이유는 “multipart/form-data” 요청에 특화되어 있으며 일부를 메소드 인수와 연결하는 데 사용할 수 있는 어노테이션이기 떄문
    //dto를 받아올떄도 굳이 @RequestPart 를 사용한 이유는 따로 포스트하기
    @PostMapping(value="/api/product")
    public int save(@RequestPart(value="productData") ProductSaveRequestDto requestDto,
                    @RequestPart(value="file") MultipartFile file) throws IOException {

        //제품 저장
        int result=productService.save(requestDto,file);
        return 1;
    }

    //위 PostMapping과 구성이 똑같습니다. (이미지를 받아올 수 있어 @RequestPart 어노테이션을 사용)
    @PutMapping(value="/api/product/{id}" )
    public int update(@RequestPart(value="productData") ProductUpdateRequestDto requestDto,
                      @RequestPart(value="file",required = false) MultipartFile file, @PathVariable int id) throws IOException {
        //가져온 정보를 토대로 제품을 수정
        productService.update(id,requestDto,file);
        log.info(requestDto.getPrice()+" "+id);
        return id;
    }


    @DeleteMapping("/api/product/{id}")
    public int delete(@PathVariable int id){
        //삭제할 제품의 id를 얻어와 삭제
        productService.delete(id);
        log.info("가져온 제품 아이디 "+id);
        return id;
    }

}
