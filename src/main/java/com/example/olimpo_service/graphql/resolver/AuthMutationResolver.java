package com.example.olimpo_service.graphql.resolver;

import com.example.olimpo_service.dto.RegisterRequest;
import com.example.olimpo_service.graphql.dto.RegisterInput;
import com.example.olimpo_service.service.AuthService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthMutationResolver implements GraphQLMutationResolver {

    private final AuthService authService;

    public String login(String username, String password) {
        return authService.authenticate(username, password);
    }

    public String register(RegisterInput input) {
        RegisterRequest request = input.toRegisterRequest();
        authService.register(request);
        return "Usuario registrado exitosamente";
    }
}
