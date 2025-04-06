package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.service.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador del historial.
 * Gestiona les operacions relacionades amb el historial dels clients, com obtenir-lo, actualitzar-lo o eliminar-lo.
 */
@RestController
@RequestMapping("/historial")
public class HistorialController {
    @Autowired
    private HistorialService historialService;

    /**
     * Actualitza un historial existent.
     * Aquest mètode rep les dades d'un historial i actualitza la informació relacionada amb ell a la base de dades.
     *
     * @param historial    Les dades actualitzades de l'historial.
     * @param token        El token d'autenticació per verificar l'usuari.
     * @return             Resposta HTTP amb l'estat de l'actualització del historial.
     * @throws SecurityException Si l'usuari no té permís per actualitzar l'historial.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateHistorial(@RequestBody Historial historial,
                                                @RequestHeader("Authorization") String token) {
        try {
            boolean updated = historialService.updateHistorial(historial, token);
            if (!updated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat l'historial amb ID: " + historial.getIdhistorial());
            }
            return ResponseEntity.ok("Historial actualitzat correctament");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Obté la informació d'un historial per ID.
     * Aquest mètode rep l'ID d'un historial i retorna la informació corresponent.
     * Requereix un token d'autenticació per verificar l'usuari.
     *
     * @param id        L'ID de l'historial a obtenir.
     * @param token     El token d'autenticació per verificar l'usuari.
     * @return          Resposta HTTP amb la informació de l'historial o un error si no es troba.
     * @throws SecurityException Si l'usuari no té permís per veure l'historial.
     * @throws NoSuchElementException Si no s'ha trobat l'historial amb l'ID proporcionat.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getHistorialById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Historial historial = historialService.getHistorialById(id, token);
            return ResponseEntity.ok(historial);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Obté tots els historials.
     * Aquest mètode retorna una llista de tots els historials existents.
     * Requereix un token d'autenticació per verificar l'usuari.
     *
     * @param token     El token d'autenticació per verificar l'usuari.
     * @return          Resposta HTTP amb la llista dels historials o un error si no es té permís per accedir-hi.
     * @throws SecurityException Si l'usuari no té permís per veure els historials.
     */
    @GetMapping
    public ResponseEntity<?> getAllHistorial(@RequestHeader("Authorization") String token) {
        try {
            List<Historial> historials = historialService.getAllHistorial(token);
            return ResponseEntity.ok(historials);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
