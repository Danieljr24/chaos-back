package com.example.olimpo_service.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TicketService {

    // ticket -> expirationTime
    private final Map<String, Instant> ticketStore = new ConcurrentHashMap<>();

    // Ticket lifetime en segundos
    private static final long TICKET_EXPIRATION_SECONDS = 30 * 60; // 30 minutos

    public String generateTicket(String username) {
        String ticket = UUID.randomUUID().toString();
        Instant expiration = Instant.now().plusSeconds(TICKET_EXPIRATION_SECONDS);
        ticketStore.put(ticket, expiration);
        return ticket;
    }

    public boolean isTicketValid(String ticket) {
        Instant expiration = ticketStore.get(ticket);
        if (expiration == null) return false;
        if (Instant.now().isAfter(expiration)) {
            ticketStore.remove(ticket);
            return false;
        }
        return true;
    }

    public void invalidateTicket(String ticket) {
        ticketStore.remove(ticket);
    }

    public void renewTicket(String ticket) {
        if (ticketStore.containsKey(ticket)) {
            ticketStore.put(ticket, Instant.now().plusSeconds(TICKET_EXPIRATION_SECONDS));
        }
    }

    public long getRemainingTime(String ticket) {
        Instant expiration = ticketStore.get(ticket);
        if (expiration == null) return 0;
        return expiration.getEpochSecond() - Instant.now().getEpochSecond();
    }
}
