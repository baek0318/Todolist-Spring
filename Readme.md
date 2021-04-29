# Todolist-Spring ![deploy](https://travis-ci.com/baek0318/Todolist-Spring.svg?branch=main)

## 💡 주제
### Todolist
유저가 인증을 통해 로그인을 하고 자신의 하루의 할일 목록을 등록할 수 있는 서비스를 제공
### 지켜야할 규약
1. `Unit Test`를 작성하기
2. `OOP`의 개념을 지키면서 만들기
3. 작성시 `indent 2`를 넘기지 않기
4. 메서드에 `10줄 이상` 작성하지 않기

위의 4가지 것들을 지키며 프로젝트를 진행할려고 한다

## 🔍 요구사항
### 회원
- 회원가입
- 로그인
- 회원정보 조회
### 권한
- 회원가입시 권한 부여(일반은 USER)
### Todo 기능
- 카테고리 지정(1개만 가능)
- 날짜 지정 (년 월 일)
- Todo 작성
- Todo 수정
- Todo 삭제
- Todo 완료
### 카테고리 기능
- Todo에 카테고리 적용
- 카테고리 생성
- 카테고리 수정
- 카테고리 삭제

## 🔨 기술 스택
- Spring Framework
- Spring Web
- Spring Security
- H2 (Test)
- Mysql
- JPA(Hibernate)
- jjwt
- Rest api


## 🛠 설계
### DB설계
<details>
<summary>
펼쳐보기
</summary>

![DB](./img/todolist_back.png)
</details>

### 백엔드 설계
<details>
<summary>
펼쳐보기
</summary>

**아키텍처**
![Architecture](./img/Todolist-Architecture-2.png)

**다이어그램**
![Diagram](./img/Todolist-Diagram.png)
</details>

## 📡 API

### Todo API

|HttpMethod|URL|Parameters|
|---|---|---|
|POST|/todo/{member-id}|title : String, status : Enum, category-id : Long|
|GET|/todo/{member-id}/all||
|GET|/todo/{member-id}/{todo-id}||
|GET|/todo/{member-id}|datetime : String|
|GET|/todo/{member-id}|status : boolean|
|PUT|/todo/{member-id}|title : String|
|PUT|/todo/{member-id}|datetime : String|
|PUT|/todo/{member-id}|status : boolean|
|DELETE|/todo/|todoId : Long|

### Category API
|HttpMethod|URL|Parameters|
|---|---|---|
|POST|category/{member-id}|title : String|
|GET|category/{member-id}/all||
|GET|category/{member-id}|title : String|
|PUT|category/{member-id}|title : String|
|DELETE|category/{member-id}/{category-id}||

### Authentication API
|HttpMethod|URL|Parameters|
|---|---|---|
|POST|/auth/signup|member-name : String, email : String, password : String|
|POST|/auth/signin|email : String, password : String|
|GET|/auth/signout||
|GET|/auth/issue-access||

### Member API
|HttpMethod|URL|Parameters|
|---|---|---|
|GET|/member/{member-id}||


## 오류 해결 & 개선사항
- PathVariable 사용할때에 name 명시해주기
<details>
<summary>
테스트는 성공했지만 기본 gradle 패키지 테스트가 실패했을 경우
</summary>

![error](./img/error.png)

위와 같은 에러를 마주칠 경우가 생긴다 물론 각자의 상황마다 다른 에러일 수 있지만 내가 마주친 에러에 대해서 풀어보고자 한다  

![cause](./img/cause.png)

test 상황을 보여주는 index.html에 들어가보면 어디서 에러가 났는지 상세하게 볼 수 있는데  
자세히 살펴보면 잘못된 이름에 클래스 파일이 존재한다는 이야기다  
그래서 gradle의 build 디렉토리에 들어가본다  

![directory](./img/directory.png)

그러면 위의 상황과 같이 2나 3이 붙은 클래스 파일들이 존재하는 것이 보이는데  
위와 같은 문제가 발생하는 이유가 다양할 수 있겠지만  
내가 겪은 바로는 인텔리제이 gradle 설정에서 test할때 gradle을 기본적으로 사용하게 되는데  
테스트 코드 같은 경우는 gradle로 할 경우 항상 새롭게 build 파일에 중복해서 쌓이는 경우가 발생하게 되는데  
그래서 위와 같은 오류가 발생하게 된다  

![solution](./img/solution.png)

위의 사진과 같이 Run tests using 부분을 Gradle -> IntelliJ 로 변경해주면 문제없이 작동되는 것을 볼 수 있다  

</details>

- [controller layer 커스텀 필터 끼고 테스트](https://peachberry0318.tistory.com/32)

<details>
<summary>
동적 쿼리를 위해 querydsl 도입하기
</summary>

</details>