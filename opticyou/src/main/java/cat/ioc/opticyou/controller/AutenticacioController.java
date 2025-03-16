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

/**
 * Controlador d'autenticació.
 * Gestiona el login, logout i el registre.
 */
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

    /**
     * és per fer proves amb el servidor, no l'utilitzen els clients
     * Inicia sessió d'usuari mitjançant el seu email i contrasenya.
     * Retorna unicament el token JWT per a l'usuari autenticat.
     */
    @Operation(
            summary = "Login per fer proves amb el servidor"
    )
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationDTO> login(@Valid @RequestBody LoginRequestDTO request){
        return ResponseEntity.ok(authenticationService.login(request));
    }

    /**
     * Inicia sessió d'usuari mitjançant el seu email i contrasenya.
     * Retorna boolean, token i rol del usuari autenticat
     */
    @Operation(
            summary = "LOGIN CLIENT"
    )
    @PostMapping("/login-user")
    public ResponseEntity<ClientLoginResponseDTO> loginUsuariDades(@Valid @RequestBody LoginRequestDTO request){
        JwtAuthenticationDTO token = authenticationService.login(request);
        if(blackListService.isTokenBlackListed(token.getToken())){
            return ResponseEntity.ok(new ClientLoginResponseDTO(false, "token is blacklisted", null));
        }
        blackListService.login(token.getToken());
        UsuariDTO usuari = usuariService.getByEmail(request.getEmail());

        return ResponseEntity.ok(new ClientLoginResponseDTO(true, token.getToken(), usuari.getRol().toString()));
    }

    /**
     * és per fer proves amb el servidor no l'utilitzen els clients
     * Tanca sessió d'usuari i retorna un boolean, comprova que no estigui a la blacklist.
     * Rep el JWT pel header
     */
    @Operation(
            summary = "Logout per fer proves amb el servidor",
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
    /**
     * Tanca sessió d'usuari i retorna un boolean, l'elimina de la blacklist
     * Rep el JWT com a string
     */
    @Operation(
            summary = "LOGOUT CLIENT"
    )
    @PostMapping("/logout-string")
    public ResponseEntity<Boolean> logoutString(@RequestBody String token) {
        if (token != null) {
            token = token.substring(1, token.length() - 1);//per eliminar les ""
            return ResponseEntity.ok(blackListService.logout(token));
        }
        return ResponseEntity.badRequest().body(false);
    }



//    @PostMapping("/register")
//    public ResponseEntity<JwtAuthenticationResponseDTO> register(@Valid @RequestBody UsuariDTO usuari){
//        return ResponseEntity.ok(authenticationService.register(usuari));
//    }
}
