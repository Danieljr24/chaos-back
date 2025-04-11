package com.example.olimpo_service.repository;

import com.example.olimpo_service.entities.Ticket;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class TicketRepositoryImpl implements TicketRepository {

    private final TicketJpaRepository ticketJpaRepository;

    public TicketRepositoryImpl(TicketJpaRepository ticketJpaRepository) {
        this.ticketJpaRepository = ticketJpaRepository;
    }

    @Override
    public void saveTicket(String username, String ticket, Instant issuedAt) {
        Ticket t = new Ticket();
        t.setTicket(ticket);
        t.setUsername(username);
        t.setIssuedAt(issuedAt);
        ticketJpaRepository.save(t);
    }

    @Override
    public void invalidateTicket(String ticket) {
        ticketJpaRepository.deleteById(ticket);
    }
}
