package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.LoginRequestDTO;
import cat.ioc.opticyou.dto.JwtAuthenticationResponseDTO;
import cat.ioc.opticyou.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @Autowired
    public AutenticacioController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    //TODO: PROVAR QUE NOMES AMB CERTS TOKENS PUC ENTRAR ALS ENDPOINTS QUE TINC
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponseDTO> login(@Valid @RequestBody LoginRequestDTO request){
        return ResponseEntity.ok(authenticationService.login(request));
    }
    //TODO: logout
//    crear una black list
//    CREATE TABLE IF NOT EXISTS revoked_tokens (
//            token VARCHAR(500) PRIMARY KEY,
//    expiry_date TIMESTAMP NOT NULL
//);
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        String token = request.getHeader("Authorization").substring(7); // Eliminar "Bearer "
//        authenticationService.revokeToken(token); // Aquí pots desar-lo a una blacklist
//        return ResponseEntity.ok("Logged out successfully.");
//    }


    //TODO: SIGN IN
//    @PostMapping("/signup")
//    public ResponseEntity<JwtAuthenticationResponse> register(@Valid @RequestBody SignRequest request){
//        return ResponseEntity.ok(authenticationService.signUp(request));
//    }
}
