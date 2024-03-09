package com.mengen.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtProvider {

    private SecretKey key = Keys.hmacShaKeyFor(JwtConstants.SECRET_KEY.getBytes());

    public String generateToken(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);

        return Jwts.builder().setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + JwtConstants.EXPIRATION_TIME))
                .claim("email", auth.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.substring(JwtConstants.JWT_HEADER.length()));
        return claims.getBody().get("email", String.class);
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();

        for (GrantedAuthority auth : authorities) {
            auths.add(auth.getAuthority());
        }

        return String.join(",", auths);
    }

}
