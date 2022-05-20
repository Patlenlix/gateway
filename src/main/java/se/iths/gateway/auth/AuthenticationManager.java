package se.iths.gateway.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import se.iths.gateway.util.JWTUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;

    public AuthenticationManager(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        try {
            Jws<Claims> claims = jwtUtil.getAllClaimsFromToken(authToken);
            if (claims == null) {
                return Mono.empty();
            }

            Date expires = claims.getBody().getExpiration();
            if (expires.before(new Date(System.currentTimeMillis()))) {
                return Mono.empty();
            }

            List<String> roles = (ArrayList<String>) claims.getBody().get("roles");
            List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

            return Mono.just(new UsernamePasswordAuthenticationToken(claims.getBody().getSubject(), null, authorities));
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}
