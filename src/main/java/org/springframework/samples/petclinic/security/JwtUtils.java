package org.springframework.samples.petclinic.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${petclinic.security.jwtSecret}")
    private String jwtSecret;

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public User decodeToken(String token) {
        var claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        String username = claims.getSubject();

        @SuppressWarnings("unchecked")
        ArrayList<String> groups = (ArrayList<String>) claims.get("groups");
        Set<String> roles = new HashSet<>(groups);
        return new User(username, roles);
    }

    public boolean validateJwtToken(String authToken) {

        if (ObjectUtils.isEmpty(authToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is empty");
        }
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
