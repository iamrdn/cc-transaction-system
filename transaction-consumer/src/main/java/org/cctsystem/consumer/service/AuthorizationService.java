package org.cctsystem.consumer.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.cctsystem.consumer.model.AuthorizationLog;
import org.cctsystem.consumer.model.CardLimit;
import org.cctsystem.consumer.model.Transaction;
import org.cctsystem.consumer.repository.AuthorizationLogRepository;
import org.cctsystem.consumer.repository.CardLimitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthorizationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    private final CardLimitRepository cardLimitRepository;
    private final AuthorizationLogRepository authorizationLogRepository;

    @Transactional
    public boolean authorizeTransaction(Transaction transaction) {
        CardLimit cardLimit = cardLimitRepository.findById(transaction.getCardNumber())
                .orElseThrow(() -> new RuntimeException("Card not found: " + transaction.getCardNumber()));

        boolean isAuthorized = cardLimit.getAvailableCredit().compareTo(transaction.getAmount()) >= 0;

        if (isAuthorized) {
            cardLimit.setAvailableCredit(cardLimit.getAvailableCredit().subtract(transaction.getAmount()));
            cardLimitRepository.save(cardLimit);
        }

        logAuthorization(transaction, isAuthorized);

        return isAuthorized;
    }

    private void logAuthorization(Transaction transaction, boolean isAuthorized) {
        AuthorizationLog log = new AuthorizationLog();
        log.setTransactionId(transaction.getTransactionId());
        log.setAuthorized(isAuthorized);
        log.setReason(isAuthorized ? "Sufficient funds" : "Insufficient funds");
        log.setTimestamp(LocalDateTime.now());

        authorizationLogRepository.save(log);

        logger.info("Transaction {} {}", transaction.getTransactionId(), 
                    isAuthorized ? "authorized" : "declined");
    }

    @PostConstruct
    public void initializeCreditLimits() {
        List<CardLimit> cardLimits = Arrays.asList(
                new CardLimit("1234567890123456", new BigDecimal(10000), new BigDecimal(10000)),
                new CardLimit("2345678901234567", new BigDecimal(15000), new BigDecimal(15000)),
                new CardLimit("3456789012345678", new BigDecimal(30000), new BigDecimal(30000))
        );
        cardLimitRepository.saveAll(cardLimits);
    }
}