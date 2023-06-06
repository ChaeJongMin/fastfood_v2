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

import lombok.*;

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
@NoArgsConstructor
@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pid;//PK

	private String productName;

	private Integer price;

	private boolean allSale=false;

	@ManyToOne
	@JoinColumn(name="categoryId",nullable=false)
	private Categories categories;
	
	@OneToMany(mappedBy="product", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<Product_option_info> infoList=new ArrayList<Product_option_info>();

	@Builder
	public Product(String productName, int price, boolean allSale,Categories categories
	,List<Product_option_info> infoList){
		this.productName=productName;
		this.price=price;
		this.allSale=allSale;
		this.categories=categories;
		this.infoList=infoList;
	}
	//제품 정보를 수정하는 메소드이면 (가격과 품절여부만 수정)
	public void update(int price, boolean allSale){
		this.price=price;
		this.allSale=allSale;
	}
}
