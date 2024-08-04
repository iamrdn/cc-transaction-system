package org.cctsystem.consumer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "card_limits")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CardLimit {
    @Id
    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber;

    @Column(name = "credit_limit", nullable = false)
    private BigDecimal creditLimit;

    @Column(name = "available_credit", nullable = false)
    private BigDecimal availableCredit;

}
