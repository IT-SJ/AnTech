# AnTech
AI를 활용하여 금융뉴스를 요약한 후 시각화하여 사용자에게 제공하는 서비스

## 프로젝트 소개 (은진이 바꾸기)
- SSM이란 Sweet Street Meet의 약자로 JSP/Servlet 기반의 동기통신과 AJAX 비동기 통신을 이용한 웹사이트 프로젝트이다.
- SSM 프로젝트는 한국의 겨울철 대표간식인 붕어빵, 떡볶이, 어묵, 등을 판매하고있는 포장마차 위치를 파악하는것에 중점을 두었으며, 추가적으로 사용자의 '동네'라는 키워드에 맞춰 커뮤니티 기능을 추가하여 사용자에게 편리성을 제공하는 것을 목표로 하고있다.
- 위치 파악, 주소 검색을 위하여 카카오 지도 API를 사용하였으며, 대부분의 기술은 JSP/Servlet이용한 동기통신 방식으로 데이터가 전달되고있다.
- 사용자가 입력한 정보는 Oracle DB에 저장된다.
- 포장마차, 커뮤니티 정보는 DB에 저장된 데이터를 바탕으로 사용자에게 보여지는 화면을 서블릿에서 SQL 쿼리문으로 DB에서 Select하여 출력하여 데이터를 전달하여 JSP에서 표현하는 방식으로 사용되고 있다. 이 또한, Front 화면에서 입력되는 방식으로 DB에 저장된다.
- 사용자의 '동네' 키워드에 맞춘 필터 정보는 JS를 사용하여 구현하었으며, 모든 정보는 Oracle DB에 저장된 데이터에 맞춰 보여질 수 있게 설계되어있다.
- 필요한 정보가 Oracle DB에서 나오는 만큼 초반 DB설계 과정에서 테이블, 컬럼, 관계를 상세하게 설정하는 것이 이 프로젝트의 핵심이다.
- 웹사이트로 구현된 만큼 '반응형 웹'으로 구현하여 화면 크기에 맞춰 자유롭게 변화하는 것을 확인할 수 있다.

## 프로젝트 개발기간 (은진)
2024/11/20 ~ 2024/12/09
약 2주

## 개발인원 (예빈)
총 5인
팀장1 팀원4

## 개발환경 (예빈)
- 언어 : Java, JSP/Servlet, HTML/CSS/JS
- 서버 : Tomcat Server
- 프레임워크 : Lombok 라이브러리, Mybatis XML, JSTL, COS 라이브러리, JDBC, GSON, Maven 프로젝트
- DB : ORACLE
- IDE : ECLIPSE, VSCODE
- API : 카카오 API, 주소검색 다음 API
- 협업툴 : GITHUB

## 기술스택 ( 엔젤 빈)
- Front : HTML/CSS/JS
- Back : JSP/Servlet

## 주요기능 (엔젤 빈)
![image](https://github.com/user-attachments/assets/f26bf504-f0a0-479a-b719-a1aa612fc5e0)

## GitHub 링크 (엔젤 빈)
[https://github.com/2024-SMHRD-KDT-BigData-26/SJ_Team-SSM/edit/master](https://github.com/2024-SMHRD-KDT-BigData-26/SJ_Team-SSM/tree/master)

