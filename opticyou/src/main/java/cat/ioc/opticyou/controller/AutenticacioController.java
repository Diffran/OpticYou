package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.LoginRequestDTO;
import cat.ioc.opticyou.dto.JwtAuthenticationResponseDTO;
import cat.ioc.opticyou.dto.TemporalJwtAuthResponseDTO;
import cat.ioc.opticyou.dto.UsuariDTO;
import cat.ioc.opticyou.model.Usuari;
import cat.ioc.opticyou.service.AuthenticationService;
import cat.ioc.opticyou.service.UsuariService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacioController {
    @Autowired
    private final UsuariService usuariService;
    @Autowired
    private final AuthenticationService authenticationService;
    public AutenticacioController(AuthenticationService authenticationService,UsuariService usuariService){
        this.authenticationService = authenticationService;
        this.usuariService = usuariService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponseDTO> login(@Valid @RequestBody LoginRequestDTO request){
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/login-user")
    public ResponseEntity<TemporalJwtAuthResponseDTO> loginUsuariDades(@Valid @RequestBody LoginRequestDTO request){
        JwtAuthenticationResponseDTO token = authenticationService.login(request);
        UsuariDTO usuari = usuariService.getByEmail(request.getEmail());

        return ResponseEntity.ok(new TemporalJwtAuthResponseDTO(true, token.getToken(), usuari.getRol().toString()));
    }



//    @PostMapping("/register")
//    public ResponseEntity<JwtAuthenticationResponseDTO> register(@Valid @RequestBody UsuariDTO usuari){
//        return ResponseEntity.ok(authenticationService.register(usuari));
//    }
}
