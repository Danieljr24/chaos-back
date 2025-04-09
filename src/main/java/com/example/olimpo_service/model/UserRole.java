package com.example.olimpo_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String microservice;
    private String role;

    @ManyToOne
    @JoinColumn(name = "user_username")
    private User user;
}
