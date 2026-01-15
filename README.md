SNS 프로젝트 (Spring Boot + Next.js)

📌 개요

이 프로젝트는 Next.js 15 프론트엔드와 Spring Boot 3 + JPA 백엔드로 구성된 SNS 애플리케이션입니다.
초기에는 Next.js API + Prisma로 구현되었으나, 안정적인 운영을 위해 Spring Boot JPA 백엔드로 마이그레이션되었습니다.

⸻

🏗️ 기술 스택

프론트엔드
•	Next.js 15
•	React 19
•	Tailwind CSS

백엔드
•	Spring Boot 3.5
•	Spring Data JPA (Hibernate)
•	PostgreSQL
•	Spring Security + JWT
•	OAuth2 Client (구글/네이버/카카오 로그인 지원 가능)
•	Springdoc OpenAPI (Swagger UI)
•	Querydsl (복잡한 검색 쿼리 지원)
•	Lombok


📂 프로젝트 구조 (백엔드)
src/main/java/com/example/project/                      
├── api/                 # API 계층 
│   ├── controller/      # REST API 컨트롤러                         
│   ├── dto/             # 요청/응답 DTO                                
│   ├── service/         # 비즈니스 로직                             
│   └── validator/       # 커스텀 검증                                
├── domain/              # 도메인 계층                                
│   ├── entity/          # JPA 엔티티                                    
│   ├── repository/      # JPA 레포지토리                                    
│   └── enums/           # 도메인 열거형                                     
├── security/            # 인증/인가 모듈                                  
│   ├── jwt/             # JWT 토큰 처리                                     
│   ├── oauth2/          # OAuth2 처리                                        
│   └── config/          # Spring Security 설정                                   
├── config/              # 설정 클래스                                     
│   ├── database/        # DB 설정                                          
│   ├── swagger/         # Swagger 설정                                
│   └── external/        # 외부 연동                           
├── common/              # 공통 모듈                                   
│   ├── exception/       # 예외 처리                                     
│   ├── response/        # 공통 응답 객체                                     
│   └── util/            # 유틸리티                                     
└── Application.java     # Spring Boot 메인 클래스       



📖 API 문서 (Swagger)
•	개발 서버 실행 후 접속:
👉 http://localhost:8080/swagger-ui.html


application.yml

•	applica tion-dev.yml: 개발 환경 (SQL 출력, ddl-auto=update)

•	application-prod.yml: 운영 환경 (SQL 최소화, ddl-auto=validate)

주요 환경 변수 (app.*)
- `app.upload-dir` : 서버 로컬에서 파일이 저장될 경로 (기본값 `uploads`)
- `app.public-base-url` : 클라이언트가 이미지를 조회할 때 사용할 도메인/포트 (기본값 `http://localhost:8080`)

✅ 주요 기능

•	회원가입 / 로그인

•	JWT 기반 인증

•	OAuth2 소셜 로그인 확장 가능

•	게시글 CRUD

•	이미지 업로드 지원 (/uploads/)

•	댓글 기능

•	좋아요 기능

•	Swagger UI 기반 API 문서 제공

•	Querydsl을 이용한 복잡한 검색 최적화
