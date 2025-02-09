## eventsourcing-patterns
CQRS와 이벤트 소싱을 활용하여 간단한 결제 시스템을 구현.
결제 프로세스는 다음과 같은 3단계로 이루어집니다:

1. 결제 요청 (Payment Requested)
2. 결제 정보 검증 (Payment Verified)
3. 결제 승인 (Payment Approved)

### Features
- 이벤트 소싱(Event Sourcing)
  - 결제 상태 변화를 PostgreSQL에 이벤트로 기록
  - 이벤트 재생(Replay)을 통해 현재 결제 상태를 재구성
- CQRS 분리
  - 결제를 요청/승인하는 커맨드(Command) 로직과 결제 상태를 조회하는 쿼리(Query) 로직을 분리하여 유지보수성 향상
- Docker & PostgreSQL
  - 로컬 개발 환경에서 쉽게 인프라를 구성하고 테스트 가능
- Axon Server 사용
  - Axon Server를 통해 이벤트 저장소 및 메시징 브로커 역할 수행
  - Axon의 CommandBus, EventStore를 활용하여 분산 시스템 구성 가능

### Requirements
- Java 21
- Axon Framework 4.x
- PostgreSQL
- Docker 및 Docker Compose
- Gradle (프로젝트 빌드용)