package org.springframework.samples.petclinic.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@SpringBootTest
@EnableAutoConfiguration()
class GenerateTokenTest {

    // Token expires after 100 years
    private static final Date EXPIRY_DATE = Date.from(LocalDateTime.now().plusYears(100).toInstant(ZoneOffset.UTC));

    @Value("${petclinic.security.jwtSecret}")
    private String jwtSecret;

    @Test
    void generateOwnerAdminToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("groups", new String[]{Roles.OWNER_ADMIN});
        String jwt = Jwts.builder()
            .setClaims(claims)
            .setSubject("X00001")
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(new Date())
            .setExpiration(EXPIRY_DATE)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
        System.out.println(jwt);
    }

    @Test
    void generateVetAdminToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("groups", new String[]{Roles.VET_ADMIN});
        String jwt = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setSubject("X00002")
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(new Date())
            .setExpiration(EXPIRY_DATE)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
        System.out.println(jwt);
    }

    @Test
    void generateAdminToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("groups", new String[]{Roles.ADMIN});
        String jwt = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setSubject("X00003")
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(new Date())
            .setExpiration(EXPIRY_DATE)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
        System.out.println(jwt);
    }

    @Test
    void generateTokenWithAllRoles() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("groups", new String[]{Roles.OWNER_ADMIN, Roles.VET_ADMIN, Roles.ADMIN});
        String jwt = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setSubject("X00004")
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(new Date())
            .setExpiration(EXPIRY_DATE)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
        System.out.println(jwt);
    }
}
