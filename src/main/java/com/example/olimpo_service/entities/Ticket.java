package com.example.olimpo_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ticket {

    @Id
    private String ticket;

    private String username;

    private Instant issuedAt;
}
