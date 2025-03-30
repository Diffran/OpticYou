package cat.ioc.opticyou.controller;

import cat.ioc.opticyou.dto.ClientDTO;
import cat.ioc.opticyou.dto.ClinicaDTO;
import cat.ioc.opticyou.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    public ClientController(){}

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody ClientDTO clientDTO, @RequestHeader("Authorization") String token) {
       try{
           int result = clientService.createClient(clientDTO, token);
           if (result == 1) {
               return ResponseEntity.status(HttpStatus.CREATED).build();
           }
           return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
       }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientByID(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try{
            ClientDTO clientDTO = clientService.getClientById(id, token);
            return ResponseEntity.ok(clientDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllClients(@RequestHeader("Authorization") String token) {
        try {
            List<ClientDTO> clients = clientService.getAllClients(token);
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @Operation(
            summary = "No permet modificar ni el idUsuari ni la contrasenya!"
    )
    @PutMapping("/update")
    public ResponseEntity<?> updateClinica(
            @RequestBody ClientDTO clientDTO,
            @RequestHeader("Authorization") String token
    ) {
        try {
            boolean updated = clientService.updateClient(clientDTO, token);
            if (!updated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat el client amb ID: " + clientDTO.getClinicaId());
            }
            return ResponseEntity.ok("Client actualitzat correctament");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClinica(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            int status = clientService.deleteClient(id, token);
            if (status == -1) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat el client amb ID: " + id);
            }else if (status == 1) {
                return ResponseEntity.status(HttpStatus.OK).body("Client eliminat correctament");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("no s'ha pogut eliminar el client");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
