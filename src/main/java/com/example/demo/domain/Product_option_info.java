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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product_option_info {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer infoid;//PK
   
   @ManyToOne
   @JoinColumn(name="pid",nullable=false)
   private Product product;
   
   @ManyToOne
   @JoinColumn(name="sid",nullable=false)
   private Size size;
   
   @ManyToOne
   @JoinColumn(name="tid",nullable=false)
   private Temperature temperature;

   private boolean isUse=true; //옵션 사용 여부
   private Integer price;

   @Builder
   public Product_option_info(Product product, Size size, Temperature temperature,
                              boolean isUse, int price){
      this.product=product;
      this.size=size;
      this.temperature=temperature;
      this.isUse=isUse;
      this.price=price;

   }
   public void update(int price){
      this.price+=price;
   }
}
