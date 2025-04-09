package com.example.olimpo_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String microservice;

    private String role;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;
    
}