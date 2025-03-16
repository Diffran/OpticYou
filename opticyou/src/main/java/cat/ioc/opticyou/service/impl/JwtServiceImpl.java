package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.model.Usuari;
import cat.ioc.opticyou.service.JwtService;
import cat.ioc.opticyou.util.Rol;
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

/**
 * Gestiona la creació, extracció d'informació i validació de tokens JWT.
 */
@Service
public class JwtServiceImpl implements JwtService {
    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    public JwtServiceImpl(){

    }
    public JwtServiceImpl(String secretKey){
        this.secretKey = secretKey;
    }

    /**
     * Genera un token jwt del usuari autenticat
     *
     * @param userDetails
     * @return JWT token
     */
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

    /**
     * decodifica el token per extreure el email
     * @param token
     * @return email
     */
    @Override
    public String getEmailFromToken(String token) {
        return getClaim(token,Claims::getSubject);
    }

    /**
     * comprova si el token no esta expirat
     * @param token
     * @param userDetails
     * @return boolean
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token){
        return getClaim(token,Claims::getSubject);
    }

    /**
     * extreu els claims del token o sigui, la informació guardada
     * @param token
     * @return claims
     */
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

    /**
     * decodifica el rol del token
     * @param token
     * @return
     */
    public Rol getRolFromToken(String token) {
        String rolString = getClaim(token, claims -> claims.get("rol", String.class));
        return Rol.valueOf(rolString);
    }

    /**
     * decodifica la expiració del token
     * @param token
     * @return la data d'expiració
     */
    private Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * comprova si el token està expirat
     * @param token
     * @return boolena
     */
    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
}
