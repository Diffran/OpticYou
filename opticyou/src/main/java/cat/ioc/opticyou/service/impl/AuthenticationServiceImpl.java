package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.dto.loginLogout.JwtAuthenticationDTO;
import cat.ioc.opticyou.dto.loginLogout.LoginRequestDTO;
import cat.ioc.opticyou.model.Usuari;
import cat.ioc.opticyou.repositori.UsuariRepositori;
import cat.ioc.opticyou.service.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Gestiona el procés de login i genera el JWT
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UsuariRepositori usuariRepositori;
    private final JwtServiceImpl jwtService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(UsuariRepositori usuariRepositori, JwtServiceImpl jwtService, AuthenticationManager authenticationManager) {
        this.usuariRepositori = usuariRepositori;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Autentica un usuari i genera un token JWT si les credencials són vàlides.
     *
     * @param request Dades de l'usuari per a l'autenticació (email i contrasenya).
     * @return Un objecte el token JWT.
     * @throws EntityNotFoundException Si l'email proporcionat no existeix a la base de dades.
     */
    @Override
    public JwtAuthenticationDTO login(LoginRequestDTO request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Usuari usuari = usuariRepositori.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("NO EXISTEIX EL EMAIL"));

        return new JwtAuthenticationDTO(
                jwtService.getToken(usuari),
                usuari.getEmail()
        );
    }

    public UsuariRepositori getUsuariRepositori() {
        return usuariRepositori;
    }

    public JwtServiceImpl getJwtService() {
        return jwtService;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

}
