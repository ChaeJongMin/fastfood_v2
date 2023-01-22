package com.example.demo.domain;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.*;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String userId;
	private String userPasswd;
	private String email;
	private String cardNum;
	private String cardCompany;
	private String phoneNum;
	private Integer role=0;

	@Builder
	public Customer(int id,String userId,String userPasswd,String email
			,String cardNum,String cardCompany,String phoneNum,int role){
		this.id=id;
		this.userId=userId;
		this.userPasswd=userPasswd;
		this.email=email;
		this.cardNum=cardNum;
		this.cardCompany=cardCompany;
		this.phoneNum=phoneNum;
		this.role=role;
	}
}
