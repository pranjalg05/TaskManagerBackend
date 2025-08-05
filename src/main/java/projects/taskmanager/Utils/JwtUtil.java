package projects.taskmanager.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;


@Component
public class JwtUtil {

    private final String SECRET_KEY = "5ecc2dcba3931b72bb95c6c097080f8c";

    private SecretKey getSignKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public boolean isValidToken(String token){
        return !isExpired(token);
    }

    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1))
                .signWith(getSignKey())
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Date extractExpirationDate(String token){
        return extractAllClaims(token).getExpiration();
    }

    private boolean isExpired(String token){
        return extractExpirationDate(token).before(new Date());
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

}
