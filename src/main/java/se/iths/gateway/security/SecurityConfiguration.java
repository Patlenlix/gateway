package se.iths.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;
import se.iths.gateway.auth.AuthenticationManager;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public SecurityConfiguration(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .logout().disable()
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .exceptionHandling()
                .authenticationEntryPoint((swe, ex) -> Mono.fromRunnable(
                        () -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                .accessDeniedHandler((swe, ex) -> Mono.fromRunnable(
                        () -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
                .and()
                .authorizeExchange()
                .pathMatchers("/auth/**").permitAll()
                .pathMatchers("/images/**").authenticated()
                .pathMatchers("/short/**").authenticated()
                .anyExchange().hasRole("ADMIN")
                .and().build();
    }
}
