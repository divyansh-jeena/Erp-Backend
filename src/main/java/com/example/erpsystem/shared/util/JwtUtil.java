package com.example.erpsystem.shared.util;

import com.example.erpsystem.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component

public class JwtUtil {

  @Value("${jwt.secret}")
  private String jwtSecretKey;

  @Value("${jwt.expiration}")
  private Long expirationTime;

  public SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  public String generateAccessToken(User user) {
    return Jwts.builder()
            .setSubject(String.valueOf(user.getId())) // 🔥 improved
            .claim("role", user.getRole().name())
            .claim("type", "ACCESS")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(getSecretKey())
            .compact();
  }

  public boolean validateToken(String token) {
    try {
      extractAllClaims(token);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public String getUsernameFromToken(String token) {
    return extractAllClaims(token).getSubject();
  }

  public String getClaimFromToken(String token, String claimKey) {
    return extractAllClaims(token).get(claimKey, String.class);
  }
}

