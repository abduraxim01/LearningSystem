package uz.abduraxim.LearningSystem.service.jwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    final private long TOKEN_LIVE_TIME = 1000 * 60 * 60 * 24; // a day

    final private String SECRET_KEY = "LaudateomnesgenteslaudateMagnificatinseculaEtanimamealaudateMagnificatinseculaHappynationlivininahappynationAddthistothedependenciesblockinyourbuildLetmeknowifyouneedfurtherassistance";

    public String encode(String username, Collection<? extends GrantedAuthority> authorities) {
        authorities = authorities.stream().filter(role -> role.toString().startsWith("ROLE_")).toList();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("Created by: ", "https://abduraxim.uz");
        extraClaims.put("Role", authorities.toString().substring(6, authorities.toString().length() - 1));


        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_LIVE_TIME))
                .signWith(getSignInKey())
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractClaims(token).getSubject();
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }


    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
