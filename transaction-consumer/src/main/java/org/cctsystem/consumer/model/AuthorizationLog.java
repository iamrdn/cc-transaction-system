package org.cctsystem.consumer.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "authorization_logs")
@Data
@ToString
public class AuthorizationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private boolean authorized;

    private String reason;

    @Column(nullable = false)
    private LocalDateTime timestamp;

}