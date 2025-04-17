package cat.ioc.opticyou.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String getToken(UserDetails userDetails);
    String getEmailFromToken(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    Long getIdFromToken(String token);
}
