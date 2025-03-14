package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.model.Usuari;
import cat.ioc.opticyou.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.function.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    public JwtServiceImpl(){

    }
    public JwtServiceImpl(String secretKey){
        this.secretKey = secretKey;
    }

    @Override
    public String getToken(UserDetails userDetails) {
        Map<String, Object> userClaims = new HashMap<>();
        if (userDetails instanceof Usuari) {
            Usuari usuari = (Usuari) userDetails;
            userClaims.put("usuari_id", usuari.getIdUsuari());
            userClaims.put("rol", usuari.getRol());
        }
        return getToken(userClaims, userDetails);
    }

    private String getToken(Map<String,Object> extractClaims, UserDetails userDetails){

        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

    }


    private Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String getEmailFromToken(String token) {
        return getClaim(token,Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token){
        return getClaim(token,Claims::getSubject);
    }
    private Claims getAllClaims(String token){
        return
                Jwts.parser()
                        .setSigningKey(getKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }
    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
}
