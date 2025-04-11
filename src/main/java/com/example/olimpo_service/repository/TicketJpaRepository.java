package com.example.olimpo_service.repository;

import com.example.olimpo_service.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketJpaRepository extends JpaRepository<Ticket, String> {
}
