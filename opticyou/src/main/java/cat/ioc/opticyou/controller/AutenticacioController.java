package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.LoginRequestDTO;
import cat.ioc.opticyou.dto.JwtAuthenticationResponseDTO;
import cat.ioc.opticyou.dto.UsuariDTO;
import cat.ioc.opticyou.service.AuthenticationService;
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
    private final AuthenticationService authenticationService;
    public AutenticacioController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    //TODO: PROVAR QUE NOMES AMB CERTS TOKENS PUC ENTRAR ALS ENDPOINTS QUE TINC
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponseDTO> login(@Valid @RequestBody LoginRequestDTO request){
        return ResponseEntity.ok(authenticationService.login(request));
    }

//    @PostMapping("/register")
//    public ResponseEntity<JwtAuthenticationResponseDTO> register(@Valid @RequestBody UsuariDTO usuari){
//        return ResponseEntity.ok(authenticationService.register(usuari));
//    }
}
