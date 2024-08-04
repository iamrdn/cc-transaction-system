package org.cctsystem.consumer.repository;

import org.cctsystem.consumer.model.CardLimit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardLimitRepository extends JpaRepository<CardLimit, String> {
}