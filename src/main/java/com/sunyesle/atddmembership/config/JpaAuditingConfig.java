package com.sunyesle.atddmembership.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "userAuditorAware")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> userAuditorAware() {
        return new UserAuditorAware();
    }

//    @Bean
//    public AuditorAware<Long> userAuditorAware() {
//        return () ->
//                Optional.ofNullable(SecurityContextHolder.getContext())
//                        .map(SecurityContext::getAuthentication)
//                        .filter(authentication -> !authentication.getPrincipal().equals("anonymousUser"))
//                        .map(Authentication::getPrincipal)
//                        .map(CustomUserDetails.class::cast)
//                        .map(CustomUserDetails::getId);
//    }
}
