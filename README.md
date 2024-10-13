# HUSK-WAS

## 프로젝트 구조

본 프로젝트는 Domain-Driven Design (DDD) 원칙을 준수하고자 하며, 아래와 같은 5개의 주요 계층으로 구성되어 있다.

```asciidoc
+-------------------+
|    Presentation   |
+-------------------+
          |
          v
+-------------------+
|    Application    |
+-------------------+
          |
          v
+-------------------+
|      Domain       |
+-------------------+
    ^           ^
    |           |
+--------+ +----------------+
| Common | | Infrastructure |
+--------+ +----------------+
```

### 1. Domain

- 비즈니스 로직을 구현하고, 도메인 객체를 관리하는 계층
- 비즈니스 규칙과 로직을 순수하게 유지
- 핵심 비즈니스 로직, `Entity`, `VO(Value-Object)`, `Domain Service`, `Repository`를 포함

### 2. Application

- 도메인 계층의 서비스를 사용하여 비즈니스 로직을 처리하는 계층
- 도메인 계층과 외부 계층(Presentation, Infrastructure) 간의 중개자 역할
- 유즈케이스 구현, 도메인 서비스 호출, 트랜잭션 관리, 도메인 이벤트 처리 등을 담당

### 3. Infrastructure

- 외부와의 연동을 담당하는 계층
- 도메인과 애플리케이션 계층의 추상화를 실제 기술로 구현
- 데이터베이스 접근, 외부 서비스 호출, 메시징, 파일 I/O 등을 담당

### 4. Common

- 여러 계층에서 공통적으로 사용되는 코드를 관리하는 계층
- 공통 상수, 공통 예외, 공통 유틸리티 등을 포함
- 각 계층에서 공통으로 사용되는 코드(cross-cutting concerns)를 분리하여 중복을 방지하고, 유지보수성을 높임

### 5. Presentation

- 사용자와의 상호작용을 담당하는 계층
- 사용자 인터페이스, API 엔드포인트, 인증 및 권한 부여 등을 담당
- 사용자 요청을 처리하고, 응답을 생성하는 역할

위와 같은 계층 구조를 통해 각 계층의 역할을 명확히 구분하고, 각 계층 간의 의존성을 최소화하고자 한다.