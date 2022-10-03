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
}
