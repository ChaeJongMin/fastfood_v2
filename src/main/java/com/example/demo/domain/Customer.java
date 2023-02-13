package com.example.demo.domain;
import javax.persistence.*;

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

	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
		private List<Boards> boardList=new ArrayList<>();

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Comment> commentList=new ArrayList<>();
	@Builder
	public Customer(int id,String userId,String userPasswd,String email
			,String cardNum,String cardCompany,String phoneNum,int role,List<Boards> boardList
					){
		this.userId=userId;
		this.userPasswd=userPasswd;
		this.email=email;
		this.cardNum=cardNum;
		this.cardCompany=cardCompany;
		this.phoneNum=phoneNum;
		this.role=role;
		this.boardList=boardList;
		this.commentList=commentList;
	}
}
