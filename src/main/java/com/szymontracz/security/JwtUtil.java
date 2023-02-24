package com.szymontracz.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    Environment environment;

    public JwtUtil(Environment environment) {
        this.environment = environment;
    }

    public String getUserId(String header) {
        String token = header.replace(environment.getProperty("authorization.token.header.prefix"), "");

        Claims claims = Jwts.parser().setSigningKey(environment.getProperty("token.secret"))
                .parseClaimsJws(token).getBody();

        if (claims == null) {
            return null;
        }

        return (String) claims.get("userId");
    }
}
