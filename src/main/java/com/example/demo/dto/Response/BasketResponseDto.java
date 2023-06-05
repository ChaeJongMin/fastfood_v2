package com.example.demo.dto.Response;

import com.example.demo.domain.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
public class BasketResponseDto {
    private int bid;//PK
    private int pCount;
    private boolean isSet;
    private int price;
    private int totalPrice;
    private int sideId;
    private int drinkId;


    public BasketResponseDto(Basket basketEntitiy){
        this.bid=basketEntitiy.getBid();
        this.pCount=basketEntitiy.getPCount();
        this.isSet=checkSet(basketEntitiy.getProductinfo().getProduct().getCategories().getCategoryId());
        this.price=(basketEntitiy.getPrice()/this.pCount);
        this.totalPrice=basketEntitiy.getPrice();
        if(this.isSet){
            int[] infos=infoSplit(basketEntitiy.getInfo());
             this.sideId=infos[1];
             this.drinkId=infos[2];
        } else{
            this.sideId=1;
            this.drinkId=1;
        }
    }

    public boolean checkSet(int cid){
        if(cid==6)
            return true;
        return false;
    }

    public int[] infoSplit(String infoStr){
        String[] infoArr=infoStr.split(",");
        int[] infoInt=new int[3];
        for(int i=0; i<infoArr.length; i++){
            infoInt[i]=Integer.parseInt(infoArr[i]);
        }
        return infoInt;
    }
}
