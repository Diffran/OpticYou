package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.loginLogout.LoginRequestDTO;
import cat.ioc.opticyou.dto.loginLogout.JwtAuthenticationDTO;
import cat.ioc.opticyou.dto.loginLogout.ClientLoginResponseDTO;
import cat.ioc.opticyou.dto.UsuariDTO;
import cat.ioc.opticyou.service.AuthenticationService;
import cat.ioc.opticyou.service.BlackListService;
import cat.ioc.opticyou.service.UsuariService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/auth")
public class AutenticacioController {
    @Autowired
    private final BlackListService blackListService;
    @Autowired
    private final UsuariService usuariService;
    @Autowired
    private final AuthenticationService authenticationService;
    public AutenticacioController(AuthenticationService authenticationService,UsuariService usuariService, BlackListService blackListService){
        this.authenticationService = authenticationService;
        this.usuariService = usuariService;
        this.blackListService = blackListService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationDTO> login(@Valid @RequestBody LoginRequestDTO request){
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/login-user")
    public ResponseEntity<ClientLoginResponseDTO> loginUsuariDades(@Valid @RequestBody LoginRequestDTO request){
        JwtAuthenticationDTO token = authenticationService.login(request);
        UsuariDTO usuari = usuariService.getByEmail(request.getEmail());

        return ResponseEntity.ok(new ClientLoginResponseDTO(true, token.getToken(), usuari.getRol().toString()));
    }

    @Operation(
            summary = "Logout header",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(@RequestHeader("Authorization") String header) {
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            return ResponseEntity.ok(blackListService.logout(token));
        }
        return ResponseEntity.badRequest().body(false);
    }

    @PostMapping("/logout-string")
    public ResponseEntity<Boolean> logoutString(@RequestBody String jwt) {
        if (jwt != null) {
            return ResponseEntity.ok(blackListService.logout(jwt));
        }
        return ResponseEntity.badRequest().body(false);
    }



//    @PostMapping("/register")
//    public ResponseEntity<JwtAuthenticationResponseDTO> register(@Valid @RequestBody UsuariDTO usuari){
//        return ResponseEntity.ok(authenticationService.register(usuari));
//    }
}
