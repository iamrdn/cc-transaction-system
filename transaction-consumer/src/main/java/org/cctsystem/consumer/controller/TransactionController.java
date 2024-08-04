package org.cctsystem.consumer.controller;

import lombok.RequiredArgsConstructor;
import org.cctsystem.consumer.model.Transaction;
import org.cctsystem.consumer.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TransactionController {

    private final TransactionRepository transactionRepository;

    @GetMapping
    public ResponseEntity<Page<Transaction>> getAllTransactions(Pageable pageable) {
        return ResponseEntity.ok(transactionRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        return transactionRepository.findByTransactionId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}