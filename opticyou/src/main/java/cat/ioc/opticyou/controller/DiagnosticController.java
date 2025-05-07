package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.DiagnosticDTO;
import cat.ioc.opticyou.service.DiagnosticService;
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

    @GetMapping("/historial/{id}")
    public ResponseEntity<?> getAllDiagnosticsByHistorial(@PathVariable("id") Long historialId, @RequestHeader("Authorization") String token) {
        try {
            List<DiagnosticDTO> diagnostics = diagnosticService.getAllDiagnosticsByHistorial(historialId, token);
            return ResponseEntity.ok(diagnostics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

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
