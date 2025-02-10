# AnTech
AI를 활용하여 금융뉴스를 요약한 후 시각화하여 사용자에게 제공하는 서비스

## 프로젝트 소개
- ANSight는 Ai를 활용한 금융 뉴스 요약 및 시각화 제공하는 웹사이트 프로젝트이다.

## 프로젝트 개발기간
2025/12/31 ~ 2025/2/13
약 4주

## 개발인원
총 4인
팀장1 팀원3

## 개발환경
- 언어 : Java, HTML/CSS/JS, Python  
- 서버 : Tomcat Server, Flask  
- 프레임워크 : Lombok 라이브러리, JDBC, Maven 프로젝트  
- DB : MYSQL 
- IDE : VSCODE
- API : Yahoo Finance API
- 협업툴 : GITHUB

## 기술스택
백엔드: Spring Boot, Flask  
프론트엔드: HTML/CSS/JavaScript, Bootstrap  
데이터베이스: MySQL  
AI 기술: Hugging Face Transformers (감정 분석), Crawl4 (요약)  
API 연동: Yahoo Finance API  
시각화: Chart.js  

## 주요기능 
- 금융 뉴스 크롤링: 네이버 금융 뉴스를 크롤링하여 최신 정보를 제공.
- AI 기반 뉴스 요약: Crawl4를 사용해 뉴스 본문을 자동으로 요약.
- 키워드 분석: Kiwi 라이브러리를 활용해 뉴스 요약에서 주요 키워드 추출.
- 워드 클라우드 생성: Kiwi로 추출한 키워드 데이터를 기반으로 wordc.js를 사용해 동적 워드 클라우드를 생성.  
  키워드 클릭 시 관련 뉴스 검색 페이지로 이동.
- 감정 분석: Hugging Face 모델을 사용해 뉴스 요약의 감정(긍정, 부정, 중립)을 분석.
- 데이터 시각화: Chart.js를 사용해 키워드 워드 클라우드 및 감정 분석 결과를 동적으로 시각화.
- 검색 기능: 키워드 클릭 시 관련 뉴스를 검색 및 조회 가능.
- 증권정보 데이터 연동: Yahoo Finance API를 통해 증권 데이터를 실시간으로 가져와 정보를 제공.

## GitHub 링크 
[https://github.com/2024-SMHRD-KDT-BigData-26/SJ_Team-SSM/edit/master](https://github.com/2024-SMHRD-KDT-BigData-26/SJ_Team-SSM/tree/master)

