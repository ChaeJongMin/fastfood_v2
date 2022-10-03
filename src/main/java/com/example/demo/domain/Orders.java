package com.example.demo.domain;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@ToString
@Entity
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date orederDate;
	
	private String state;
	@ManyToOne
	@JoinColumn(name="pId",nullable=false)
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="id",nullable=false)
	private Customer customer;
	
	private String info;
	private Integer price;
	
}
