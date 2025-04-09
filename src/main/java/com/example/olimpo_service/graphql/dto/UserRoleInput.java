package com.example.olimpo_service.graphql.dto;

import lombok.Data;

@Data
public class UserRoleInput {
    private String microservice;
    private String role;
}
