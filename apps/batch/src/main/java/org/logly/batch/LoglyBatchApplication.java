package org.logly.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// @EnableBatchProcessing이 있으면 자동 설정이 비활성화 되어 JobLauncherApplicationRunner가 생성되지 않는다.
@EnableJpaRepositories(basePackages = "org.logly.infrastructure.database.repository")
@EntityScan(basePackages = "org.logly.infrastructure.database.entity")
@SpringBootApplication(scanBasePackages = "org.logly")
public class LoglyBatchApplication {

    public static void main(String[] args) {
        // 배치 작업의 성공/실패 상태를 exit code로 외부 시스템에 전달 하기 위함.
        System.exit(SpringApplication.exit(SpringApplication.run(LoglyBatchApplication.class, args)));
    }
}
