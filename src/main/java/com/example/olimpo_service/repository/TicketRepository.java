package com.example.olimpo_service.repository;

import java.time.Instant;

public interface TicketRepository {
    void saveTicket(String username, String ticket, Instant issuedAt);
    void invalidateTicket(String ticket);
}
