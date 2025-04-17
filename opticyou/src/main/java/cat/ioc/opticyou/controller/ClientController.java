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

/**
 * Controlador de clients.
 * Gestiona les operacions relacionades amb els clients com crear, obtenir, actualitzar i eliminar.
 */
@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    public ClientController(){}

    /**
     * Crea un nou client.
     * Aquest mètode rep les dades del client, crea un client en la base de dades i retorna una resposta segons el resultat.
     *
     * @param clientDTO  Les dades del client a crear.
     * @param token      El token d'autenticació per verificar l'usuari.
     * @return           Resposta HTTP amb l'estat de la creació del client.
     */
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

    /**
     * Obté un client per la seva ID.
     * Aquest mètode recupera les dades d'un client mitjançant la seva ID i retorna les dades del client en format DTO.
     *
     * @param id     La ID del client a obtenir.
     * @param token  El token d'autenticació per verificar l'usuari.
     * @return       Resposta HTTP amb les dades del client si es troba, o un error en cas contrari.
     */
    @Operation(
            summary = "per admin i client, admin qualsevol i client només a si mateix, ignora param id"
    )
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

    /**
     * Obté tots els clients.
     * Aquest mètode recupera la llista de tots els clients registrats i retorna la llista en format DTO.
     *
     * @param token  El token d'autenticació per verificar l'usuari.
     * @return       Resposta HTTP amb la llista de clients si es troben disponibles, o un error en cas contrari.
     */
    @GetMapping
    public ResponseEntity<?> getAllClients(@RequestHeader("Authorization") String token) {
        try {
            List<ClientDTO> clients = clientService.getAllClients(token);
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * Actualitza un client.
     * Aquest mètode permet actualitzar les dades d'un client, però no permet modificar ni el ID del client ni la contrasenya.
     * El client es busca per ID i es realitza la modificació de les seves dades.
     *
     * @param clientDTO El DTO que conté les dades actualitzades del client.
     * @param token     El token d'autenticació per verificar l'usuari.
     * @return          Resposta HTTP amb un missatge de confirmació si l'actualització va ser exitosa o un error si no es va trobar el client.
     */
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

    /**
     * Elimina un client per ID.
     * Aquest mètode elimina un client del sistema segons el seu ID.
     * Si el client no es troba, es retorna un error 404. Si es troba i s'eliminen les dades correctament, es retorna una resposta 200.
     * En cas de problemes amb l'eliminació, es retorna un error 500.
     *
     * @param id    L'ID del client a eliminar.
     * @param token El token d'autenticació per verificar l'usuari.
     * @return      Resposta HTTP amb el missatge d'èxit o error corresponent.
     */
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

    /**
     * Actualitza les dades d’un client autenticat amb rol CLIENT.
     *
     * Aquest mètode permet al client modificar les seves pròpies dades,
     * excepte el seu identificador (idUsuari), la clínica assignada i l’historial mèdic.
     * També pot modificar la contrasenya si s’indica una de nova.
     *
     * @param clientDTO DTO amb les dades actualitzades del client.
     * @param token     Token d’autenticació JWT del client.
     * @return          Resposta HTTP amb un missatge d’èxit si s’ha actualitzat correctament,
     *                  o amb un missatge d’error si no s’ha trobat el client o si el token no és vàlid.
     */
    @Operation(
            summary = "No permet modificar ni el idUsuari, historial o clinica"
    )
    @PutMapping("/update_client")
    public ResponseEntity<?> updateClient(
            @RequestBody ClientDTO clientDTO,
            @RequestHeader("Authorization") String token
    ) {
        try {
            boolean updated = clientService.updateClientClient(clientDTO, token);
            if (!updated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat el client amb ID: " + clientDTO.getIdUsuari());
            }
            return ResponseEntity.ok("Client actualitzat correctament");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * Permet que un client es pugui eliminar a si mateix.
     * Aquest mètode utilitza el token d'autenticació per identificar el client i procedir a la seva eliminació.
     * Si el client no es troba, retorna un error. Si l'eliminació és exitosa, retorna un missatge de confirmació.
     *
     * @param token El token d'autenticació que permet identificar el client que vol eliminar el seu compte.
     * @return Resposta HTTP amb un missatge de confirmació o error depenent de l'estat de l'eliminació.
     * @throws SecurityException Si el token és invàlid o caducat, o si el client no està autoritzat.
     */
    @Operation(
            summary = "Permet que un client es pugui eliminar a si mateix"
    )
    @DeleteMapping("/delete_client")
    public ResponseEntity<?> deleteClient(@RequestHeader("Authorization") String token) {
        try {
            int status = clientService.deleteClientClient(token);
            if (status == -1) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat el client autenticat.");
            } else if (status == 1) {
                return ResponseEntity.status(HttpStatus.OK).body("Client eliminat correctament.");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No s'ha pogut eliminar el client.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
