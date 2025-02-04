package com.eventsourcing.payment.repository;

import com.eventsourcing.payment.query.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, String> {
}
