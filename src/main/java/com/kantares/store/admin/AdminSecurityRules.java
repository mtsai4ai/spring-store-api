package com.kantares.store.admin;

import com.kantares.store.common.SecurityRules;
import com.kantares.store.user.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

public class AdminSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.GET, "/admin/**").hasRole(Role.ADMIN.name());
    }
}
