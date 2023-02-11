package com.example.demo.Controller;

import com.example.demo.Service.BoardsService;
import com.example.demo.dto.BoardsRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class boardApiController {
    private final BoardsService boardsService;

    @PutMapping("/api/post")
    public Long save(@RequestBody BoardsRequestDto requestDto){
        System.out.println("api/post 컨트롤러 작동!!");
        return boardsService.save(requestDto);
    }
}
