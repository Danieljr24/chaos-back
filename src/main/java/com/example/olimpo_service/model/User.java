package com.example.olimpo_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "roles")
@EqualsAndHashCode(exclude = "roles")
public class User {

    @Id
    private String username;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<UserRole> roles = new HashSet<>();

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
        for (UserRole role : roles) {
            role.setUser(this);
        }
    }
}
