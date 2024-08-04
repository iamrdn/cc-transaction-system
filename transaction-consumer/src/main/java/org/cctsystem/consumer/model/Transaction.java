package org.cctsystem.consumer.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    @Column(name = "authorization_status")
    @Enumerated(EnumType.STRING)
    private AuthorizationStatus authorizationStatus;

    public enum AuthorizationStatus {
        PENDING, APPROVED, DECLINED
    }
}