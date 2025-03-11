package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.dto.UsuariDTO;
import cat.ioc.opticyou.mapper.UsuariMapper;
import cat.ioc.opticyou.repositori.UsuariRepositori;
import cat.ioc.opticyou.service.UsuariService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuariServiceImpl implements UsuariService {
    @Autowired
    private UsuariRepositori usuariRepositori;

    @Override
    public void update(String email) {
        //TODO: update data from user
    }

    @Override
    public void delete(String email) {
        //TODO: delete Usuari
    }

    @Override
    public UsuariDTO getByEmail(String email) {
        return usuariRepositori.findByEmail(email)
                .map(UsuariMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Usuari no trobat amb email: " + email));
    }

    @Override
    public List<UsuariDTO> getAll() {
        return usuariRepositori.findAll().stream()
                .map(UsuariMapper::toDto)
                .collect(Collectors.toList());
    }

    //seguretat
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return usuariRepositori.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
            }
        };
    }
}
