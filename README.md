# ReservationWebProgram

Spring Framework, Spring MVC Pattern을 사용한 티켓 예약 서비스 프로그램

## API 스펙
[링크](http://49.236.147.192:9090/swagger-ui.html)

## ERD

<img src="https://cphinf.pstatic.net/mooc/20190108_224/1546930955463gOn4N_PNG/reservation_ERD_PJT6.png?type=w760"></img>

## 시연 영상

### 메인 화면

[![메인 화면](http://img.youtube.com/vi/FpUbz0anTXE/0.jpg)](https://youtu.be/FpUbz0anTXE)

### 상세 화면

[![상세 화면](http://img.youtube.com/vi/4Okcrajjaj4/0.jpg)](https://youtu.be/4Okcrajjaj4)

### 예약 기능

[![예약 기능](http://img.youtube.com/vi/b9ZixZzZDWg/0.jpg)](https://youtu.be/b9ZixZzZDWg)

### 예약 한 줄 평

[![예약 한 줄 평](http://img.youtube.com/vi/7b_jSn_3Vk4/0.jpg)](https://youtu.be/7b_jSn_3Vk4)

## Clone 후 실행 방법
### Visual Studio Code 사용 기준
1. [여기](https://sambalim.tistory.com/67) 참고하여 IDE 초기 설정 가능
2. MySql Sever 설치
3. [application.properties](./src/main/resources/application.properties) > spring.datasource.username, spring.datasource.password에 상응하는 MySql User 추가
4. MySql Character Set을 UTF-8로 설정. -> [linux 설정법](https://www.lesstif.com/dbms/mysql-rhel-centos-ubuntu-20775198.html) | [Windows 설정법](https://sina-bro.tistory.com/4)
5. 기본 DB 스키마, 데이터를 넣기 위해, [/sqls](./sqls) 폴더 안에있는 sql 파일을 실행할 필요가 있음.

```sql
mysql > source /path/to/schema.sql
mysql > source /path/to/data.sql
```

6. [filepath.properties](./src/main/resources/filepath.properties) > root.path 의 위치에 [/imgs](./imgs) 폴더 안에 있는 /img, /img_map 디렉토리 복사. ex) C:\img, C:\img_map
7. pom.xml > 마우스 오른쪽 클릭 > Update Project Configuration 클릭 (Maven Dependencies 다운로드 & 정리)
8. 아무 자바 파일에서 F5를 눌러서 서버 실행.
