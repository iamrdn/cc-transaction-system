package org.cctsystem.consumer.service;

import lombok.RequiredArgsConstructor;
import org.cctsystem.consumer.model.Transaction;
import org.cctsystem.consumer.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TransactionConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionConsumerService.class);

    private final TransactionRepository transactionRepository;
    private final AuthorizationService authorizationService;

    @KafkaListener(topics = "${spring.kafka.topic.transactions}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void consume(Transaction transaction) {
        logger.info("Received transaction: {}", transaction.getTransactionId());
        if (transaction.getAmount().intValue() > 800) {
            logger.error("Amount exceeds the limit 800 < : {}", transaction.getAmount());
            throw new IllegalArgumentException("Amount exceeds the limit 800 < : {}" + transaction.getAmount());
        }
        try {
            boolean isAuthorized = authorizationService.authorizeTransaction(transaction);

            transaction.setAuthorizationStatus(isAuthorized ?
                    Transaction.AuthorizationStatus.APPROVED :
                    Transaction.AuthorizationStatus.DECLINED);

            transactionRepository.save(transaction);

            logger.info("Processed transaction: {}, Status: {}",
                    transaction.getTransactionId(),
                    transaction.getAuthorizationStatus());
        } catch (Exception e) {
            logger.error("Failed to process transaction: {}", transaction.getTransactionId(), e);
            throw new RuntimeException("Failed to process transaction", e);
        }
    }

}