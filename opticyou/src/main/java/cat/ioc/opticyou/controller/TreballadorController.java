package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.ClientDTO;
import cat.ioc.opticyou.dto.TreballadorDTO;
import cat.ioc.opticyou.service.TreballadorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treballador")
public class TreballadorController {
    @Autowired
    private TreballadorService treballadorService;

    public TreballadorController() {
    }

    public TreballadorController(TreballadorService treballadorService) {
        this.treballadorService = treballadorService;
    }

    /**
     * Crea un nou treballador.
     * Aquest mètode rep les dades del treballador, crea un treballador en la base de dades i retorna una resposta segons el resultat.
     *
     * @param treballador  Les dades del treballador a crear.
     * @param token      El token d'autenticació per verificar l'usuari.
     * @return           Resposta HTTP amb l'estat de la creació del treballador.
     */
    @PostMapping
    public ResponseEntity<?> createTreballador(@RequestBody TreballadorDTO treballador, @RequestHeader("Authorization") String token) {
        try{
            int result = treballadorService.createTreballador(treballador, token);
            if (result == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obté un treballador per la seva ID.
     * Aquest mètode recupera les dades d'un treballador mitjançant la seva ID i retorna les dades del trebvallador en format DTO.
     *
     * @param id     La ID del treballador a obtenir.
     * @param token  El token d'autenticació per verificar l'usuari.
     * @return       Resposta HTTP amb les dades del treballador si es troba, o un error en cas contrari.
     */
    @Operation(
            summary = "per admin i treballador, admin qualsevol i treballador només a si pertany a la mateixa clínica"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getTreballadorByID(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try{
            TreballadorDTO treballadorDTO = treballadorService.getTreballadorById(id, token);
            return ResponseEntity.ok(treballadorDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * Obté tots els treballadors.
     * Aquest mètode recupera la llista de tots els treballadors registrats i retorna la llista en format DTO.
     * Si l'usuari es TREBALLADOR podrà veure tots els treballadors de la seva clínica.
     * @param token  El token d'autenticació per verificar l'usuari.
     * @return       Resposta HTTP amb la llista de treballadors si es troben disponibles, o un error en cas contrari.
     */
    @Operation(
            summary = "ADMIN tots, TREBALLADOR només els de la seva clínica"
    )
    @GetMapping
    public ResponseEntity<?> getAllTreballadors(@RequestHeader("Authorization") String token) {
        try {
            List<TreballadorDTO> treballadors = treballadorService.getAllTreballadors(token);
            return ResponseEntity.ok(treballadors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * Actualitza un treballador.
     * Aquest mètode permet actualitzar les dades d'un treballador, però no permet modificar ni el ID del treballador ni la contrasenya.
     * El client es busca per ID i es realitza la modificació de les seves dades.
     *
     * @param treballadorDTO El DTO que conté les dades actualitzades del client.
     * @param token     El token d'autenticació per verificar l'usuari.
     * @return          Resposta HTTP amb un missatge de confirmació si l'actualització va ser exitosa o un error si no es va trobar el client.
     */
    @Operation(
            summary = "No permet modificar ni el idUsuari ni la contrasenya!"
    )
    @PutMapping("/update")
    public ResponseEntity<?> updateTreballador(
            @RequestBody TreballadorDTO treballadorDTO,
            @RequestHeader("Authorization") String token
    ) {
        try {
            boolean updated = treballadorService.updateTreballador(treballadorDTO, token);
            if (!updated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat el treballador amb ID: " + treballadorDTO.getClinicaId());
            }
            return ResponseEntity.ok("Treballador actualitzat correctament");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * Elimina un treballador per ID.
     * Aquest mètode elimina un treballador del sistema segons el seu ID.
     * Si el treballador no es troba, es retorna un error 404. Si es troba i s'eliminen les dades correctament, es retorna una resposta 200.
     * En cas de problemes amb l'eliminació, es retorna un error 500.
     *
     * @param id    L'ID del treballador a eliminar.
     * @param token El token d'autenticació per verificar l'usuari.
     * @return      Resposta HTTP amb el missatge d'èxit o error corresponent.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTreballador(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            int status = treballadorService.deleteTreballador(id, token);
            if (status == -1) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat el treballador amb ID: " + id);
            }else if (status == 1) {
                return ResponseEntity.status(HttpStatus.OK).body("Treballador eliminat correctament");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("no s'ha pogut eliminar el treballador");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
