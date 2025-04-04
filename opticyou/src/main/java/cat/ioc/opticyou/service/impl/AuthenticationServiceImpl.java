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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(UsuariRepositori usuariRepositori, JwtServiceImpl jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.usuariRepositori = usuariRepositori;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
       this.passwordEncoder = passwordEncoder;
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

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    //    @Override
//    public JwtAuthenticationResponse register(UsuariDTO usuariDTO) {
//        if (usuariDTO.getEmail().isEmpty() || usuariDTO.getPassword().isEmpty()) {
//            throw new IllegalArgumentException("Email and password: MUST NOT BE EMPTY");
//        }
//        usuariRepositori.findByEmail(request.getEmail())
//                .ifPresent(user -> {
//                    throw new EntityExistsException("Auth User failed: '"+user.getEmail()+"' -> ALREADY EXIST in DataBase" );
//                });
//
//        UsuariDTO userDTO = UsuariDTO()
//
//                .builder()
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .build();
//
//        User user = UserMapper.toEntity(userDTO);
//        if(userIRepository.findAll().isEmpty()){
//            user.setRole(Role.ADMIN);
//        }
//
//        userIRepository.save(user);
//        PlayerDTO defaultPlayer = new PlayerDTO(user.getId(),null,0);
//        playerService.create(defaultPlayer);
//
//        return JwtAuthenticationResponse.builder()
//                .token(jwtService.getToken(user))
//                .userId(user.getId())
//                .build();
//    }

}
