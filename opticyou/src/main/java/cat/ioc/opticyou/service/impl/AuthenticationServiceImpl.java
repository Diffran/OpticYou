package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.dto.JwtAuthenticationResponseDTO;
import cat.ioc.opticyou.dto.LoginRequestDTO;
import cat.ioc.opticyou.model.Usuari;
import cat.ioc.opticyou.repositori.UsuariRepositori;
import cat.ioc.opticyou.service.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UsuariRepositori usuariRepositori;
    private final JwtServiceImpl jwtService;
    private AuthenticationManager authenticationManager;
    private final PasswordEncoderFactories passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(UsuariRepositori usuariRepositori, JwtServiceImpl jwtService, AuthenticationManager authenticationManager, PasswordEncoderFactories passwordEncoder) {
        this.usuariRepositori = usuariRepositori;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public JwtAuthenticationResponseDTO login(LoginRequestDTO request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Usuari usuari = usuariRepositori.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("NO EXISTEIX EL EMAIL"));

        return new JwtAuthenticationResponseDTO(
                jwtService.getToken(usuari),
                usuari.getEmail()
        );
    }

}
