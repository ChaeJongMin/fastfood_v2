package com.example.demo.dto.Response;

import com.example.demo.domain.Categories;
import com.example.demo.domain.Product;
import com.example.demo.domain.Product_option_info;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class ProductResponseDto {
    private int pId;
    private String productName;
    private Integer price;
    private boolean allSale=false;
    private List<Product_option_info>  infoList;

    private String cateName;
    public ProductResponseDto(Product entitiy){
        this.productName= entitiy.getProductName();
        this.price=entitiy.getPrice();
        this.allSale=entitiy.isAllSale();
        this.pId=entitiy.getPid();
        this.infoList=entitiy.getInfoList();
        this.cateName=entitiy.getCategories().getCategoryName();
    }

    public ArrayList<String> getSize() {
        ArrayList<String> size = new ArrayList<String>();
        for (Product_option_info info : infoList) {
            if(!(size.contains(info.getSize().getSizename()))) {
                size.add(info.getSize().getSizename());
            }
        }
        return (size);
    }

    public HashMap<String, Integer> getSizemap() {
        HashMap<String, Integer> size = new HashMap<String, Integer>();
        for (Product_option_info info : infoList) {

            size.put(info.getSize().getSizename(),info.getPrice());
        }

        return (size);
    }
    public ArrayList<String> getTemp() {
        ArrayList<String> temp = new ArrayList<String>();
        for (Product_option_info info : infoList) {
            if(!(temp.contains(info.getTemperature().getTempname()))) {
                temp.add(info.getTemperature().getTempname());
            }
        }
        return (temp);
    }

    public Product_option_info getInfo(String size, String temp) {
        for (Product_option_info info : infoList) {

            if(info.getSize().getSizename().equals(size)) {
                if(info.getTemperature().getTempname().equals(temp)) {
                    return info;
                }
            }
        }

        return null;
    }
}
