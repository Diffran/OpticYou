package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.DiagnosticDTO;
import cat.ioc.opticyou.service.DiagnosticService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diagnostic")
public class DiagnosticController {
    @Autowired
    private DiagnosticService diagnosticService;

    public DiagnosticController(DiagnosticService diagnosticService) {
        this.diagnosticService = diagnosticService;
    }

    public DiagnosticController() {
    }

    /**
     * Crea un nou diagnòstic.
     * Aquest mètode rep les dades del diagnòstic, crea un diagnòstic a la base de dades i retorna una resposta segons el resultat.
     * Si el diagnòstic ja existeix, es retorna una resposta de conflicte.
     * Si l'usuari no té permisos, es retorna una resposta de denegació d'accés.
     *
     * @param diagnosticDTO Les dades del diagnòstic a crear.
     * @param token         El token d'autenticació per verificar l'usuari.
     * @return              Resposta HTTP amb l'estat de la creació del diagnòstic.
     * @throws Exception    Si hi ha algun error en la creació del diagnòstic.
     */
    @PostMapping
    public ResponseEntity<?> createDiagnostic(@RequestBody DiagnosticDTO diagnosticDTO, @RequestHeader("Authorization") String token) {
        try {
            int result = diagnosticService.createDiagnostic(diagnosticDTO, token);
            if (result == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else if (result == -1) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Ja existeix un diagnòstic amb aquest ID");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accés denegat");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obté tots els diagnòstics d'un historial específic.
     * Aquest mètode recupera la llista de diagnòstics associats a un historial donat.
     * Només es permet l'accés si l'usuari té el rol adequat i el token d'autenticació és vàlid.
     *
     * @param historialId L'ID de l'historial per al qual es volen obtenir els diagnòstics.
     * @param token       El token d'autenticació per verificar l'usuari.
     * @return            Resposta HTTP amb la llista de diagnòstics si es troben disponibles, o un error en cas contrari.
     * @throws Exception  Si hi ha algun error en la recuperació dels diagnòstics.
     */

    @GetMapping("/historial/{id}")
    public ResponseEntity<?> getAllDiagnosticsByHistorial(@PathVariable("id") Long historialId, @RequestHeader("Authorization") String token) {
        try {
            List<DiagnosticDTO> diagnostics = diagnosticService.getAllDiagnosticsByHistorial(historialId, token);
            return ResponseEntity.ok(diagnostics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * Actualitza un diagnòstic.
     * Aquest mètode permet actualitzar la descripció d'un diagnòstic existent, però no permet modificar ni el ID del diagnòstic ni altres camps.
     * El diagnòstic es busca per ID i es realitza la modificació de la descripció.
     *
     * @param diagnosticDTO El DTO que conté les dades actualitzades del diagnòstic.
     * @param token         El token d'autenticació per verificar l'usuari.
     * @return              Resposta HTTP amb un missatge de confirmació si l'actualització va ser exitosa o un error si no es va trobar el diagnòstic.
     * @throws Exception    Si hi ha algun error en l'actualització del diagnòstic.
     */
    @Operation(
            summary = "només permet modificar la descripció"
    )
    @PutMapping("/update")
    public ResponseEntity<?> updateDiagnostic(
            @RequestBody DiagnosticDTO diagnosticDTO,
            @RequestHeader("Authorization") String token
    ) {
        try {
            boolean updated = diagnosticService.updateDiagnostic(diagnosticDTO, token);
            if (!updated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No s'ha trobat el diagnòstic amb ID: " + diagnosticDTO.getIddiagnostic());
            }
            return ResponseEntity.ok("Diagnòstic actualitzat correctament");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * Elimina un diagnòstic per ID.
     * Aquest mètode elimina un diagnòstic del sistema segons el seu ID.
     * Si el diagnòstic no es troba, es retorna un error 404. Si es troba i s'eliminen les dades correctament, es retorna una resposta 200.
     * En cas de problemes amb l'eliminació, es retorna un error 500.
     *
     * @param id    L'ID del diagnòstic a eliminar.
     * @param token El token d'autenticació per verificar l'usuari.
     * @return      Resposta HTTP amb el missatge d'èxit o error corresponent.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiagnostic(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            boolean deleted = diagnosticService.deleteDiagnostic(id, token);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat el diagnòstic amb ID: " + id);
            }
            return ResponseEntity.ok("Diagnòstic eliminat correctament");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
