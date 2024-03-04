# sprinb-batch-calculate
---

## 개요

- [배치 프로그램이란 무엇인가?](https://imwoo94.notion.site/Spring-Batch-f4d2dfe1397e4e9a8e54664aa52d2a06?pvs=4)
- Spring Batch
    - [Spring Batch 알아보기](https://github.com/IMWoo94/spring-batch-calculate/issues/9)
    - [Spring 성능 개선](https://github.com/IMWoo94/spring-batch-calculate/issues/12)
- Spring Batch 를 사용한 정산 시스템을 구현

---

### 개발 환경

- Java17
- Spring6.1.4
- SpringBoot 3.2.3
- Spring Batch
- Spring Data JPA
- H2 Database
- IntelliJ

---

### [원시적으로 배치 프로그램 구현하기](https://github.com/IMWoo94/spring-batch-calculate/issues/2)

- 365일이 지나도록 로그인 하지 않으면 휴면 계정으로 전환<br>
  <img width="450" alt="image" src="https://github.com/IMWoo94/spring-batch-calculate/assets/75981576/aca84b25-aff8-4f16-a891-5114ce996ea0">

---

### [Spring Batch 알아보기](https://github.com/IMWoo94/spring-batch-calculate/issues/9)

- Spring Batch 구조
    - Job, Step, JobRepository, ItemReader, ItemProcessor, ItemWriter
- Spring Batch Job
    - SimpleJob
    - FlowJob
- Spring Step
    - Tasklet
    - ItemReader, ItemWriter
        - File
            - FlatFile
            - Formatted
            - Json
        - Database
            - Jpa
            - JDBC
    - ItemProcessor
        - CustomItemProcessor
        - CompositeItemProcessor
- [Spring 성능 개선](https://github.com/IMWoo94/spring-batch-calculate/issues/12)
    - Multi-threaded Step
    - Parallel Steps
    - Partitioning

---

### 정산 시스템 구현
