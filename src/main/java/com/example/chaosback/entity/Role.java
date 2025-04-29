package com.example.chaosback.entity;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
