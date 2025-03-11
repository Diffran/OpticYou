package cat.ioc.opticyou.service;

import cat.ioc.opticyou.dto.UsuariDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsuariService {
    void update(String email);
    void delete(String email);
    UsuariDTO getByEmail(String email);
    List<UsuariDTO> getAll();
    UserDetailsService userDetailsService();
}
