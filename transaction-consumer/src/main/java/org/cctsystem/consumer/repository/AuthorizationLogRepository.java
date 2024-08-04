package org.cctsystem.consumer.repository;

import org.cctsystem.consumer.model.AuthorizationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizationLogRepository extends JpaRepository<AuthorizationLog, Long> {
}