package com.example.demo.domain;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
public class ProductImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ImageNum;
	private String ImageLoad;
	private String ImageName;
	@ManyToOne
	@JoinColumn(name="pId",nullable=false)
	private Product product;

	@Builder
	public ProductImage(String path, String name, Product product){
		this.ImageLoad=path;
		this.ImageName=name;
		this.product=product;
	}

	public void update(String load){
		this.ImageLoad=load;
	}
}
