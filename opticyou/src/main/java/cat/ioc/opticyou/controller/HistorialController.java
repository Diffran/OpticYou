package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.service.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/historial")
public class HistorialController {
    @Autowired
    private HistorialService historialService;

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
