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

🐳 Docker로 실행하기

1. PostgreSQL이 5432 포트에서 실행 중인지 확인하거나 `SPRING_DATASOURCE_URL`을 원하는 DB로 바꿉니다.
2. 백엔드 이미지를 빌드합니다.
   ```bash
   docker build -t jihunsns-backend .
   ```
3. 이미지 실행 (필요한 DB/앱 환경 변수를 함께 설정하고, 업로드 디렉터리는 호스트와 볼륨으로 연결):
   ```bash
   docker run --rm -p 8080:8080 \
     -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/jihunsns \
     -e SPRING_DATASOURCE_USERNAME=postgres \
     -e SPRING_DATASOURCE_PASSWORD=postgres \
     -e APP_JWT_SECRET=change-me-very-long-random-secret \
     -v "$(pwd)/uploads:/app/uploads" \
     jihunsns-backend
   ```
   - 컨테이너 기본 프로필은 `prod`이며 필요 시 `-e SPRING_PROFILES_ACTIVE=dev`와 같이 덮어쓸 수 있습니다.
   - 로컬 DB를 쓰지 않는다면 `SPRING_DATASOURCE_URL`을 컨테이너가 접근 가능한 호스트로 조정하세요.

Docker 환경에서 정적 파일은 `/app/uploads`에 저장되므로 호스트 디렉터리에 마운트하거나 외부 볼륨을 사용해 데이터를 유지하세요.

🧱 Docker Compose로 백엔드 + DB 함께 실행하기

1. `.env.docker.example`을 복사해 실제 환경 변수 파일을 만듭니다.
   ```bash
   cp .env.docker.example .env.docker
   # JWT 시크릿, DB 정보 등을 원하는 값으로 수정
   ```
2. Compose로 백엔드와 PostgreSQL을 동시에 띄웁니다.
   ```bash
   docker compose --env-file .env.docker up -d --build
   ```
   - `app` 컨테이너는 `APP_PORT`(기본 8080)로 노출되고, `uploads_data` 볼륨이 컨테이너의 `/app/uploads`와 연결되어 이미지 업로드가 유지됩니다.
   - PostgreSQL 데이터는 `postgres_data` 볼륨에 보관되며, 필요 시 `docker compose down -v`로 볼륨까지 제거할 수 있습니다.

☁️ Oracle Cloud(OCI) 배포

1. Oracle Cloud Container Registry(OCIR)에 로그인 후 이미지를 빌드/푸시합니다.
   ```bash
   docker build -t jihunsns-backend .
   docker tag jihunsns-backend phx.ocir.io/<테넌시이름>/jihunsns-backend:latest
   docker login phx.ocir.io
   docker push phx.ocir.io/<테넌시이름>/jihunsns-backend:latest
   ```
   - OCIR 레지온 코드는 계정에 맞게(`phx`, `icn`, `nrt` 등) 변경하세요.
   - 로그인 시 사용자명은 `<테넌시이름>/<사용자이름>` 형식, 비밀번호는 Auth Token입니다.
2. OCI Compute Instance에서 이 저장소를 가져온 뒤 Docker & Docker Compose를 설치합니다.
3. `.env.oci.example`를 복사해 배포용 환경 변수 파일을 만듭니다.
   ```bash
   cp .env.oci.example .env.oci
   # 필요 값 수정 (DB PW, 도메인, JWT 시크릿 등)
   ```
4. 제공된 Compose 파일로 애플리케이션과 PostgreSQL을 함께 실행합니다.
   ```bash
   docker compose --env-file .env.oci -f docker-compose.oci.yml up -d
   ```
   - `uploads_data` 볼륨이 `/data/uploads`에 마운트되어 이미지 업로드가 유지됩니다.
   - 이미지를 로컬에서 빌드해 사용하려면 `.env.oci`에서 `BACKEND_IMAGE`를 지우거나 `docker compose build app`으로 빌드하세요.
