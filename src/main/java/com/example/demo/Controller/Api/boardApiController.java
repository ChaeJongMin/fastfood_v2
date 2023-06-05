package com.example.demo.Controller.Api;

import com.example.demo.Service.BoardsService;
import com.example.demo.Service.CustomerService;
import com.example.demo.dto.Request.BoardUpdateRequestDto;
import com.example.demo.dto.Request.BoardsRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class boardApiController {
    private final BoardsService boardsService;
    private final CustomerService customerService;
    @PostMapping ("/api/post")
    public Long save(@RequestBody BoardsRequestDto requestDto){
        return boardsService.save(requestDto);
    }
    @PutMapping("/api/put/{id}")
    public Long update(@PathVariable Long id, @RequestBody BoardUpdateRequestDto requestDto) {
        return boardsService.update(id, requestDto);
    }

    @DeleteMapping("/api/delete/{id}")
    public Long delete(@PathVariable Long id) {
        boardsService.delete(id);
        return id;
    }
}
