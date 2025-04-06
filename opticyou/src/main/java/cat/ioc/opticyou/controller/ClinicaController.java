package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.ClinicaDTO;
import cat.ioc.opticyou.service.ClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de clients.
 * Gestiona les operacions relacionades amb els clients com crear, obtenir, actualitzar i eliminar.
 */
@RestController
@RequestMapping("/clinica")
public class ClinicaController {

    @Autowired
    private ClinicaService clinicaService;

    /**
     * Crea una nova clínica.
     * Aquest mètode rep les dades de la clínica, crea una nova clínica a la base de dades
     * i retorna una resposta segons el resultat.
     *
     * @param clinicaDTO  Les dades de la clínica a crear.
     * @param token       El token d'autenticació per verificar l'usuari.
     * @return            Resposta HTTP amb l'estat de la creació de la clínica.
     */
    @PostMapping
    public ResponseEntity<?> createClinica(
            @RequestBody ClinicaDTO clinicaDTO,
            @RequestHeader("Authorization") String token
    ) {
        try {
            int created = clinicaService.createClinica(clinicaDTO, token);
            if (created == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else if(created == 0){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("La clínica ja existeix");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obté la informació d'una clínica per ID.
     * Aquest mètode rep l'ID de la clínica i el token d'autenticació,
     * i retorna la informació de la clínica o un error si no es troba o no es té permís per accedir-hi.
     *
     * @param id     L'ID de la clínica a obtenir.
     * @param token  El token d'autenticació per verificar l'usuari.
     * @return       Resposta HTTP amb la informació de la clínica o un missatge d'error.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getClinicaById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try{
            ClinicaDTO clinica = clinicaService.getClinicaById(id, token);
            return ResponseEntity.ok(clinica);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * Obté la llista de totes les clíniques.
     * Aquest mètode retorna una llista de totes les clíniques disponibles
     * o un error si no es té permís per accedir-hi.
     *
     * @param token El token d'autenticació per verificar l'usuari.
     * @return      Resposta HTTP amb la llista de clíniques o un missatge d'error.
     */
    @GetMapping
    public ResponseEntity<?> getAllClinicas(@RequestHeader("Authorization") String token) {
        try {
            List<ClinicaDTO> cliniques = clinicaService.getAllClinicas(token);
            return ResponseEntity.ok(cliniques);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * Actualitza la informació d'una clínica existent.
     * Aquest mètode permet actualitzar les dades d'una clínica, excepte el seu ID.
     * Retorna un missatge d'èxit si s'actualitza correctament o un error si no es troba la clínica.
     *
     * @param clinicaDTO  Les dades actualitzades de la clínica.
     * @param token       El token d'autenticació per verificar l'usuari.
     * @return            Resposta HTTP amb l'estat de l'actualització de la clínica.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateClinica(
            @RequestBody ClinicaDTO clinicaDTO,
            @RequestHeader("Authorization") String token
    ) {
        try {
            boolean updated = clinicaService.updateClinica(clinicaDTO, token);
            if (!updated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat la clínica amb ID: " + clinicaDTO.getIdClinica());
            }
            return ResponseEntity.ok("Clínica actualitzada correctament");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * Elimina una clínica existent.
     * Aquest mètode elimina la clínica amb l'ID proporcionat. Si no es troba la clínica o no es té permís per eliminar-la, retorna un error.
     *
     * @param id     L'ID de la clínica a eliminar.
     * @param token  El token d'autenticació per verificar l'usuari.
     * @return       Resposta HTTP amb l'estat de l'eliminació de la clínica.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClinica(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            boolean deleted = clinicaService.deleteClinica(id, token);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat la clínica amb ID: " + id);
            }
            return ResponseEntity.ok("Clínica eliminada correctament");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
