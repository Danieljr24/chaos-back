package com.example.olimpo_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private List<MicroserviceRole> roles;

    @Data
    public static class MicroserviceRole {
        private String microservice;
        private String role;
    }
}
