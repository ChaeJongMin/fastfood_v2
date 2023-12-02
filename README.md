# 1. 패스트푸트 웹

학부생 시절 저를 포함한 3명이 진행한 팀프로젝트로 패스트푸드에 접할 수 있는 기능(로그인, 구매 , 장바구니, 제품 추가 등)으로 고객과 어드민으로 나뉘어집니다.

## 기능
 1. 고객 - 로그인 / 회원가입 / 장바구니 / 유저 정보 수정 / 제품 구매(상세) / 결제 / 채팅 / 메일을 통한 비밀번호 초기화 </br>
 2. 어드민 - 대시보드 / 제품 추가,수정,삭제 / 결산 기능

## 영상
 [![Video Label](http://img.youtube.com/vi/FGMfXpsJByM/0.jpg)](https://youtu.be/FGMfXpsJByM)

## 사용 기술
 1. 언어 : Java, JS , HTML5/CSS3</br>
 2. IDE: 인텔리제이</br>
 3. 프레임워크: Spring Boot</br>
 4. 백엔드: JPA, Spring Security, OAUTH2, JPA</br> 
 5. DB : MySQL</br>
 6. OS : Windows, Linux(aws 배포 시 사용)</br>
 7. CI/CD : TravisCI, AWS Code Deploy (현재 사용하지 않고있습니다.)
 8. 인프라 : AWS

## 2. 개발 일지

1. ### 팀프로젝트 
* 개발 인원: 3인</br>
* 기간 : 21.04 ~ 21.07 

2. ### V1 (개인)
   > 사용자(고객)이 사용하는 서비스에 대한 기능에 관련된 코드 리펙토링 ( 계층 분할, API 적용 ), UI 변경, 추가 기능(게시판 관련, 보안) 추가
   
 + #### 22.09 ~ 22.10 
   AWS EC2에 서버 배포 (현재 배포 중단)
   (이후 기능 추가와 수정은 1인 개발)

 + #### 23.02
   1. 게시판 기능 추가 ( 생성, 수정 , 삭제)
   2. 댓글 기능 추가 ( 생성 , 수정 ,삭제)
   3. 고객 관련된 코드 리펙토링 및 API 추가 (고객, 장바구니 관련)
   4. form-login 및 session 기능 추가
   5. 메뉴, 상세 메뉴 UI 변경

 + #### 23.03
   1. 고객 관련된 코드 리펙토링 및 API 추가 (주문 관련)
   2. 장바구니, 결제, 로그인 UI 변경
   3. 예외 처리
 
2. ### V2 (개인)
   > 어드민이 사용하는 서비스에 대한 기능에 관련된 코드 리펙토링 ( 계층 분할, API 적용 ), UI 변경, 추가 기능 ( 보안, 채팅, 메일 인증 등 ) 추가
 
 + ### 23.05
   1. JWT를 통한 보안 기능 추가
   2. OAUTH2를 이용한 소셜 로그인 기능 추가
   3. 어드민(대시보드), 결산, 메뉴 추가/수정/삭제 UI 변경 
   
 + ### 23.05
   1. 어드민 관련 기능 리펙토링 및 API 추가
   2. 채팅 기능 추가
   3. 인증 메일로 비밀번호 초기화 기능 추가
   4. AWS의 S3를 통한 이미지(제품) 관리

## ※ 참고한 교재
1. 스프링 부트와 AWS로 혼자 구현하는 웹 서비스 
2. 스프링 부트 퀵스타트</br>

