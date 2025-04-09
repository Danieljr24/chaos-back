package com.example.olimpo_service.graphql.resolver;

import com.example.olimpo_service.service.AuthService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthQueryResolver implements GraphQLQueryResolver {

    private final AuthService authService;

    public boolean hasRole(String username, String microservice) {
        return authService.hasRoleInMicroservice(username, microservice);
    }
}
