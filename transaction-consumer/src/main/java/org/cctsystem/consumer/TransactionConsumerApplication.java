package org.cctsystem.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableJpaRepositories
@EnableKafka
public class TransactionConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionConsumerApplication.class, args);
	}

}
