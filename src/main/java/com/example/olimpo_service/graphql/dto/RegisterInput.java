package com.example.olimpo_service.graphql.dto;

import com.example.olimpo_service.dto.RegisterRequest;
import com.example.olimpo_service.dto.RegisterRequest.MicroserviceRole;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegisterInput {
    private String username;
    private String password;
    private List<UserRoleInput> roles = new ArrayList<>();

    public RegisterRequest toRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(this.username);
        request.setPassword(this.password);

        List<MicroserviceRole> roleDTOs = new ArrayList<>();
        for (UserRoleInput input : roles) {
            MicroserviceRole role = new MicroserviceRole();
            role.setMicroservice(input.getMicroservice());
            role.setRole(input.getRole());
            roleDTOs.add(role);
        }
        request.setRoles(roleDTOs);

        return request;
    }
}
