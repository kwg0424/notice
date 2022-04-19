# 실행 방법 및 순서
1. 개발 환경 설정
2. db에서 notice 스키마 생성
4. 초기 설정시 src/main/resources/application.yml의 spring.jpa.hibernate.ddl-auto를 create로 지정하여 테이블 생성
5. com.example.notice.NoticeApplication.main(); 실행

## 개발 환경
```
OS : Windows 10 64-bit
Java : jdk1.8.0_91
DB : mariadb-10.6.7-winx64 (engine : InnoDB)
```

### mariadb의 my.ini 인코딩 정보
```
[mysqld]
collation-server = utf8mb4_unicode_ci
init-connect='SET NAMES utf8mb4'
character-set-server = utf8mb4
port=3306

[client]
port=3306
default-character-set=utf8mb4

[mysql]
default-character-set=utf8mb4
```
#테스트 
## 단위 테스트
**테스트에 앞서 기본 NoticeApplication 실행 필수**
```
- com.example.notice.NoticeApplicationTests.insertTest(); // 등록
- com.example.notice.NoticeApplicationTests.updateTest(); // 수정
- com.example.notice.NoticeApplicationTests.listTest(); // 목록
- com.example.notice.NoticeApplicationTests.infoTest(); // 상세
- com.example.notice.NoticeApplicationTests.deleteTest(); // 삭제
```

## 통합 테스트
**테스트에 앞서 기본 NoticeApplication 실행 필수**
```
 - com.example.notice.NoticeApplicationTests.integrateTest();
 - 혹은 클래스 실행(지정된 @Order의 순서대로 실행)
```

# 대용량 트래픽 해결책
1. 모든 log error/fatal/off로 전환
2. redis와 같은 독립적인 데이터 캐싱 서버 구축하여 개선
3. 램 부족일 경우
- 램 증설 -> -Xmx2048m와 같이 메모리 조정
2. 4번의 문제가 아닌 처리속도(CPU)의 한계일 경우
- docker swarm처럼 물리적 서버를 증설후, 각각의 서버에 서버 가동하여 분산처리함
- 혹은 nginx와 같은 웹서버로 docker swarm처럼 구현하여 분산처리함
- 물리적 서버가 늘어나면 ehcache와 같은 프로그램 상의 메모리 캐시는 제거