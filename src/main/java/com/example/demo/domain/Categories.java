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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(exclude="productList")
@Entity
public class Categories {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer categoryId;
	private String categoryName;
	
	@OneToMany(mappedBy="categories", fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	   private List<Product> productList = new ArrayList<Product>();
	
}