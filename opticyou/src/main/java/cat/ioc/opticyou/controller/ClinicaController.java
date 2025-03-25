package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.ClinicaDTO;
import cat.ioc.opticyou.service.ClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinica")
public class ClinicaController {

    @Autowired
    private ClinicaService clinicaService;

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

    @GetMapping
    public ResponseEntity<?> getAllClinicas(@RequestHeader("Authorization") String token) {
        try {
            List<ClinicaDTO> cliniques = clinicaService.getAllClinicas(token);
            return ResponseEntity.ok(cliniques);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

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
