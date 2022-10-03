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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Entity
public class Product_option_info {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer infoid;//PK
   
   @ManyToOne
   @JoinColumn(name="pid",nullable=false)
   private Product product;
   
   @ManyToOne
   @JoinColumn(name="sId",nullable=false)
   private Size size;
   
   @ManyToOne
   @JoinColumn(name="tId",nullable=false)
   private Temperature temperature;
   
   private boolean isUse=true; //옵션 사용 여부
   
   
   private Integer price;
}
