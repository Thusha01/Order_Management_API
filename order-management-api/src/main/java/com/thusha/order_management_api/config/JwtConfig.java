package com.thusha.order_management_api.config;

import com.thusha.order_management_api.model.Client;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtConfig {

    private final SecretKey SECRET_KEY;
    private final long EXPIRATION_TIME;

    // Initialize the secret key and expiration time from application.properties
    public JwtConfig(@Value("${jwt.secret}") String secret,
                     @Value("${jwt.expirationMs}") long expirationMs) {
        // Use the secret from application.properties instead of generating a new key
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes()); // Use the secret from properties
        this.EXPIRATION_TIME = expirationMs;
    }

    // Generate JWT token using email as subject (for login)
    public String generateToken(Client client) {
        return Jwts.builder()
                .setSubject(client.getUsername())  // Set the email as the token's subject
                .claim("role", client.getRole())
                .setIssuedAt(new Date())  // Set the issued time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Set expiration time
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)  // Sign with the secret key
                .compact();  // Build the token
    }

    public String extractSingleRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public String getUserCategoryFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public String getEmailFromToken(String token) {
        return extractUsername(token); // Reuses the extractUsername method
    }

    // Extract the username (email) from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a specific claim from the token using a claims resolver
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token against user details
    public boolean validateToken(String token, UserDetails userDetails) {
        String emailFromToken = extractUsername(token);
        return (emailFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Validate the token without user details (for general validation)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;  // Invalid token
        }
    }
}
