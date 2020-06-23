package rw.tajyire.api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import rw.tajyire.api.model.Auth;

@Service
public class JwtUtil {
  private static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * 10;

  @Value("${jwt.secret}")
  private String secret;

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

  public Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  public boolean isTokenExpired(String token){
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(Auth userDetails){
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername(), userDetails.getRoles());
  }

  public String createToken(Map<String, Object> claims, String subject, List<String> roles){
    claims.put("roles", roles);
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)).signWith(
        SignatureAlgorithm.HS256, secret).compact();
  }

  public boolean isValidToken(String token, UserDetails userDetails){
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
