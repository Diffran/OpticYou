package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.Login;
import cat.ioc.opticyou.dto.UsuariDTO;
import cat.ioc.opticyou.service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuari")
public class UsuariController {
    @Autowired
    private UsuariService usuariService;

    @GetMapping("/all")
    public List<UsuariDTO> getAll(){
        return usuariService.getAll();
    }

    @GetMapping("/{email}")
    public ResponseEntity<UsuariDTO> getByEmail(@PathVariable String email) {
        try {
            UsuariDTO usuariDTO = usuariService.getByEmail(email);
            return ResponseEntity.ok(usuariDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //TODO: es temporal
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody Login login) {
        boolean valid = usuariService.authenticate(login.getEmail(), login.getPassword());
        return ResponseEntity.ok(valid);
    }

    @PostMapping("/login-string")
    public ResponseEntity<String> loginString(@RequestBody Login login) {
        boolean valid = usuariService.authenticate(login.getEmail(), login.getPassword());
        if(valid) {
            return ResponseEntity.ok("token");
        }
        return ResponseEntity.ok("contrasenya incorrecte");
    }
}
