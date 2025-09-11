package Ayurvedh.ayurvedh.Configurations;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret:ZmfrZ_default_secret_change_me=}")
    private String secretBase64;

    @Value("${security.jwt.expiration-ms:86400000}")
    private long expirationMs;

    @Value("${security.jwt.refresh-expiration-ms:2592000000}")
    private long refreshExpirationMs;

    // Clock skew tolerance in milliseconds (5 minutes)
    private static final long CLOCK_SKEW_TOLERANCE = 300000;

    private Key getSigningKey() {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secretBase64);
        } catch (IllegalArgumentException ex) {
            keyBytes = secretBase64.getBytes(StandardCharsets.UTF_8);
        }

        // Ensure key length >= 32 bytes (256 bits) as required by HMAC-SHA256
        if (keyBytes.length < 32) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                keyBytes = digest.digest(keyBytes);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("SHA-256 not available for JWT key derivation", e);
            }
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .setAllowedClockSkewSeconds(300) // 5 minutes tolerance
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    // public String generateToken(String username) {
    //     Date now = new Date();
    //     Date expiry = new Date(now.getTime() + expirationMs);
    //     return Jwts.builder()
    //         .setSubject(username)
    //         .setIssuedAt(now)
    //         .setExpiration(expiry)
    //         .setIssuer("ayurvedh")
    //         .setAudience("ayurvedh-users")
    //         .signWith(getSigningKey(), SignatureAlgorithm.HS256)
    //         .compact();
    // }


    public String generateToken(String username, String name, String role) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .claim("name", name)      // 👈 user ka actual name
        .claim("role", role)      // 👈 user ka role (ROLE_USER / ROLE_ADMIN)
        .setSubject(username)     // 👈 email ya username
        .setIssuedAt(now)
        .setExpiration(expiry)
        .setIssuer("ayurvedh")
        .setAudience("ayurvedh-users")
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
}


    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshExpirationMs);
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .setIssuer("ayurvedh")
            .setAudience("ayurvedh-refresh")
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public long getRefreshExpirationMs() {
        return refreshExpirationMs;
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        Date now = new Date();
        // Add clock skew tolerance
        return expiration.before(new Date(now.getTime() - CLOCK_SKEW_TOLERANCE));
    }

    public boolean validateToken(String token, String username) {
        try {
            final String extracted = extractUsername(token);
            return (extracted.equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return "ayurvedh-refresh".equals(claims.getAudience());
        } catch (Exception e) {
            return false;
        }
    }
}


