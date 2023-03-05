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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Orders extends BaseTime{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@ManyToOne
	@JoinColumn(name="pid",nullable=false)
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="id",nullable=false)
	private Customer customer;
	
	private String info;
	private Integer price;

	@Builder
	public Orders(Product product, Customer customer, String info, int price){
		this.product=product;
		this.customer=customer;
		this.info=info;
		this.price=price;
	}

}
