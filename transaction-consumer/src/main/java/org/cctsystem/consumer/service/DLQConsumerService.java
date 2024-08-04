package org.cctsystem.consumer.service;

import org.cctsystem.consumer.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DLQConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(DLQConsumerService.class);

    @Value("${spring.kafka.template.default-topic}")
    private String dlqTopic;

    @Qualifier(value = "producerKafkaTemplate")
    private KafkaTemplate<String, Transaction> kafkaTemplate;

    public DLQConsumerService(KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "transactions.DLT")
    public void listenDLQ(Transaction transaction) {
        logger.error("Received message in DLQ: {}", transaction);

        // Here we can implement special handling for failed messages
        kafkaTemplate.send(dlqTopic, transaction);
    }
}