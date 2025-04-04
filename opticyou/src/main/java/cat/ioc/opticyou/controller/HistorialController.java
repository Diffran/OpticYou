package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.HistorialDTO;
import cat.ioc.opticyou.service.HistorialService;
import cat.ioc.opticyou.service.impl.HistorialServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historial")
public class HistorialController {

    private HistorialService historialService = new HistorialServiceImpl();

    @PostMapping
    public ResponseEntity<?> createHistorial(
            @RequestBody HistorialDTO historialDTO,
            @RequestHeader("Authorization") String token
    ) {
        try {
            int created = historialService.createHistorial(historialDTO, token);
            if (created == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else if(created == 0){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("L'historial ja existeix");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHistorialById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try{
            HistorialDTO historialDTO = historialService.getHistorialById(id, token);
            return ResponseEntity.ok(historialDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllClinicas(@RequestHeader("Authorization") String token) {
        try {
            List<HistorialDTO> historialDTOS = historialService.getAllHistorial(token);
            return ResponseEntity.ok(historialDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateHistorial(
            @RequestBody HistorialDTO historialDTO,
            @RequestHeader("Authorization") String token
    ) {
        try {
            boolean updated = historialService.updateHistorial(historialDTO, token);
            if (!updated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat l'historial amb ID: " + historialDTO.getIdhistorial());
            }
            return ResponseEntity.ok("Historial actualitzat correctament");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHistorial(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            boolean deleted = historialService.deleteHistorial(id, token);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat l'historial amb ID: " + id);
            }
            return ResponseEntity.ok("Historial eliminat correctament");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
