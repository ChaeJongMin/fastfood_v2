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
public class Basket {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer bid;//PK
   private Integer pCount;
   
   @ManyToOne
   @JoinColumn(name="infoid",nullable=false)
   private Product_option_info productinfo;
   
   @ManyToOne
   @JoinColumn(name="userid",nullable=false)
   private Customer customer;
   
   private String info;
   private Integer price;

   @Builder
   public Basket(int pCount, Product_option_info productOptionInfo, Customer customer,
                 String info, int price){
      this.pCount=pCount;
      this.productinfo=productOptionInfo;
      this.customer=customer;
      this.info=info;
      this.price=price;
   }
   public void duplicationUpdate(){
      this.price+=(this.price/this.pCount);
      this.pCount+=1;
      System.out.println("변경된 데이터: "+this.price+" "+this.pCount);
   }

   public void update(int pCount, int price){
      this.pCount=pCount;
      this.price=price;
   }

}
