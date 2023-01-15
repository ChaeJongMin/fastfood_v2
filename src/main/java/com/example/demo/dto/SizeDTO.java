package com.example.demo.dto;

import com.example.demo.domain.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeDTO {
    private Integer sId;
    private String sizename;

    public SizeDTO(Size sizeEntity){
        this.sId= sizeEntity.getSId();
        this.sizename= sizeEntity.getSizename();
    }
}
