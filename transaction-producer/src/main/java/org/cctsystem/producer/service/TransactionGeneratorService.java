package org.cctsystem.producer.service;

import lombok.RequiredArgsConstructor;
import org.cctsystem.producer.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TransactionGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionGeneratorService.class);
    private static final String TOPIC = "transactions";
    private static final String[] MERCHANT_IDS = {"AMZN", "APPL", "GOOG", "MSFT", "FB"};
    private static final Random random = new Random();

    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    private final List<String> cardNumbers = Arrays.asList(
            "1234567890123456",
            "2345678901234567",
            "3456789012345678"
    );

    @Scheduled(fixedRate = 1000)
    public void generateTransaction() {
        Transaction transaction = createRandomTransaction();
        CompletableFuture<SendResult<String, Transaction>> future = kafkaTemplate.send(TOPIC, transaction);

        future.whenComplete((result, err) -> {
            if (err != null) {
                // handle failure
                logger.error("Unable to send transaction: {} due to: {}",
                        transaction.getTransactionId(),
                        err.getMessage());
            } else {
                // handle success
                logger.info("Sent transaction: {} with offset: {}",
                        transaction.getTransactionId(),
                        result.getRecordMetadata().offset());
            }
        });
    }

    private Transaction createRandomTransaction() {
        Transaction transaction = new Transaction();
        transaction.setCardNumber(generateCardNumber());
        transaction.setAmount(generateRandomAmount());
        transaction.setMerchantId(MERCHANT_IDS[random.nextInt(MERCHANT_IDS.length)]);
        return transaction;
    }

    private String generateCardNumber() {
        return cardNumbers.get(random.nextInt(cardNumbers.size()));
    }

    private BigDecimal generateRandomAmount() {
        return BigDecimal.valueOf(random.nextDouble() * 1000)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
