### 코드에 대한 자세한 설명은 ->  [벨로그](https://velog.io/@dhdhfkk1119/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-%EC%86%8C%EA%B0%9C%ED%8C%85-%EB%B0%8F-SNS-%EC%82%AC%EC%9D%B4%ED%8A%B8)
### 코드에 대한 자세한 구현 영상은 -> [유튜브](https://studio.youtube.com/video/tpzLvj5iiyQ/edit)
### 작품 제목 : SpringBoot를 이용한 SNS 소통 웹 사이트 

> - 게시물 작성 및 댓글 기능 <br>
> - 회원 가입 및 로그인 -> 로그인시에 승인한 유저만 게시물 볼수있음<br>
> - JSON을 사용해서 실시간으로 찜하기 및 취소하기 가능 <br>
> - 메인페이지에 제일 최신 게시물이 올라오도록 설정 <br>
> - 어드민 , 일반유저 구별 -> Mypage 이름 변경 및 이미지 변경 가능 <br>
> - 어드민 페이지 -> 일반 유저 승인 및 계정삭제 , 게시물 관리(삭제)
> - 일반유저 페이지 -> 회원 탈퇴 및 회원에 대한 정보(회원가입 정보)

## 전체적인 구현 이미지 동영상 
### 회원가입
<img src="https://velog.velcdn.com/images/dhdhfkk1119/post/dc575c69-cfd5-4497-91b3-53e0c6a31e79/image.gif">

### 로그인 하기
![image](https://github.com/user-attachments/assets/3bd4720a-007f-4aba-8803-44c7e6e3c391)

### admin 일반유저 승인하기
- admin 페이지 일반 유저 승인 및 계정 삭제 , 게시물 관리(삭제)
  
![image](https://github.com/user-attachments/assets/fb274933-d32a-45b6-8aaa-dfc2fb6ced5c)

### 게시물 작성
![image](https://github.com/user-attachments/assets/55aaf674-81d5-4f07-9595-4560412c38a3)

### 댓글 작성
![image](https://github.com/user-attachments/assets/7da8fdca-6a4a-4024-b7ac-df8914dd0605)

### 팔로우 추가 및 취소
- 현재 나를 팔로우하고있으면 -> 로그인 유저의 팔로워 및 상대 팔로잉 목록에 추가
- 내가 상대를 팔로우하고있으면 -> 나의 팔로잉 및 상대 팔로워 목록에 추가
- 자신이 팔로우 및 팔로잉 리스트를 확인 가능
  
![image](https://github.com/user-attachments/assets/c1fdd726-8208-4991-ba8c-dfdb7de32285)

### 메세지 보내기
- 유저 프로필에 들어가면 메세지를 보낼수 있음
- 메세지 목록에서 최근까지 보낸 메세지를 확인 가능 (제일 마지막 메세지확인)
- 팔로우 상대를 추가해서 메세지 기능 가능
  
![image](https://github.com/user-attachments/assets/2914781e-b751-41ea-87f1-07e1c2cb2e5f)

### 프로필 이름 및 이미지 변경
![image](https://github.com/user-attachments/assets/1813ca3a-8ed7-4c46-934a-39b29a8095fd)

### MyPage 계정 회원 탈퇴 및 회원정보
![image](https://github.com/user-attachments/assets/5814cd10-0345-425c-9446-94878583d68c)

### 간략한 전체 템플릿 및 전체 기능 정리
- 회원가입(중복확인)
- 로그인(아이디 비밀번호 확인)
- 게시판(이미지 5장까지 슬라이드),
- 댓글(5장까지(슬라이드) ,
- 어드민(페이지) 승인 및 게시물삭제
- 각 게시물 찜하기
- 프로필 페이지 - 유저이름 변경 및 이미지 변경
- 프로필 페이지 - 찜하기 및 댓글 게시물 작성한거를 확인 가능
- 게시물 검색 및 사용자 검색
- 메세지 기능(이미지도 가능)

![image](https://github.com/user-attachments/assets/6e4e0a96-4615-4de6-b346-751812347a50)























