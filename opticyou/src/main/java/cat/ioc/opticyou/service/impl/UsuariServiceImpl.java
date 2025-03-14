package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.dto.UsuariDTO;
import cat.ioc.opticyou.mapper.UsuariMapper;
import cat.ioc.opticyou.repositori.UsuariRepositori;
import cat.ioc.opticyou.service.UsuariService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuariServiceImpl implements UsuariService {
    @Autowired
    private UsuariRepositori usuariRepositori;


    @Override
    public boolean authenticate(String email, String contrasenya) {
        UsuariDTO usuariDTO = getByEmail(email);
        if(!usuariDTO.getContrasenya().equals(contrasenya)){
            return false;
        }
        return true;
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


    @Override
    public void update(String email) {
        //TODO: update data from user
    }

    @Override
    public void delete(String email) {
        //TODO: delete Usuari
    }

    //genera el JWT... canviar de lloc??
    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}
