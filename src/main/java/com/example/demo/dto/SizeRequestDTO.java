package com.example.demo.dto;

import com.example.demo.domain.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SizeRequestDTO {
    private String sizename;

    @Builder
    public SizeRequestDTO(String sizename){
        this.sizename=sizename;
    }
    public Size toSizeEntity(){
        return Size.builder()
                .sizename(sizename)
                .build();
    }
}
