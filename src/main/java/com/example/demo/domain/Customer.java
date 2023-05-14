package com.example.demo.domain;
import javax.persistence.*;

import java.util.*;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
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

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
		private List<Boards> boardList=new ArrayList<>();

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Comment> commentList=new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	@Enumerated(EnumType.STRING)
	private SocialType socialType;
	@ColumnDefault("empty")
	private String socialId;
	@Builder
	public Customer(String userId,String userPasswd,String email
			,String cardNum,String cardCompany,String phoneNum,List<Boards> boardList
			,List<Comment> commentList, Role role,SocialType socialType,String socialId){
		this.userId=userId;
		this.userPasswd=userPasswd;
		this.email=email;
		this.cardNum=cardNum;
		this.cardCompany=cardCompany;
		this.phoneNum=phoneNum;
		this.boardList=boardList;
		this.commentList=commentList;
		this.role=role;
		this.socialType=socialType;
		this.socialId=socialId;
	}

	public void update(String userId,String email
			,String cardNum,String cardCompany,String phoneNum){
		this.userId=userId;
		this.email=email;
		this.cardNum=cardNum;
		this.cardCompany=cardCompany;
		this.phoneNum=phoneNum;
	}
	public void updateForSocial(String userId,String userPasswd
			,String cardNum,String cardCompany,String phoneNum){
		this.userId=userId;
		this.userPasswd=userPasswd;
		this.cardNum=cardNum;
		this.cardCompany=cardCompany;
		this.phoneNum=phoneNum;
	}
}
