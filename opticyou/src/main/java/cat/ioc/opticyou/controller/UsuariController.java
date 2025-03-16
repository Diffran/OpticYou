package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.UsuariDTO;
import cat.ioc.opticyou.service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador per gestionar les operacions relacionades amb els usuaris
 */
@RestController
@RequestMapping("/usuari")
public class UsuariController {
    @Autowired
    private UsuariService usuariService;

    /**
     * Retorna una llista de tots els usuaris.
     */
    @GetMapping("/all")
    public List<UsuariDTO> getAll(){
        return usuariService.getAll();
    }

    /**
     * Retorna un usuari a partir del seu correu electrònic. Si no es troba, retorna un error 404.
     */
    @GetMapping("/{email}")
    public ResponseEntity<UsuariDTO> getByEmail(@PathVariable String email) {
        try {
            UsuariDTO usuariDTO = usuariService.getByEmail(email);
            return ResponseEntity.ok(usuariDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
