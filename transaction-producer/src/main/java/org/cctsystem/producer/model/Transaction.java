package org.cctsystem.producer.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@ToString
public class Transaction {
    private String transactionId;
    private String cardNumber;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String merchantId;

    public Transaction() {
        this.transactionId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }
}