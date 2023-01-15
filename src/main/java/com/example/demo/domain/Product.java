package com.example.demo.domain;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude="categories")
@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pId;//PK
	private String productName;
	private Integer price;
	private boolean allSale=false;
	@ManyToOne
	@JoinColumn(name="categoryId",nullable=false)
	private Categories categories;
	
	@OneToMany(mappedBy="product",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private List<Product_option_info> infoList=new ArrayList<Product_option_info>();

	//엔터티에서 setter은 무조건 금지 나중에 dto에서 추가해야한다.
	public void setCategories(Categories c) {
		this.categories = c;
		c.getProductList().add(this);
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
