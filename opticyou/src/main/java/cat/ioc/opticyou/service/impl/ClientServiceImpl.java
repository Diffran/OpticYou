package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.dto.ClientDTO;
import cat.ioc.opticyou.mapper.ClientMapper;
import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.repositori.ClientRepository;
import cat.ioc.opticyou.repositori.UsuariRepositori;
import cat.ioc.opticyou.service.ClientService;
import cat.ioc.opticyou.service.ClinicaService;
import cat.ioc.opticyou.service.UsuariService;
import cat.ioc.opticyou.util.Rol;
import cat.ioc.opticyou.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Gestiona els processos relacionats amb els clients, com la creació, actualització i eliminació.
 * Utilitza els repositoris corresponents per interactuar amb la base de dades.
 */
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    UsuariRepositori usuariRepositori;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    HistorialServiceImpl historialService;
    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private ClinicaService clinicaService;
    @Autowired
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Crea un client i l'associa a un historial i a una clínica.
     * Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, es crea un client amb un historial bàsic.
     *
     * @param clientdto Les dades del client a crear.
     * @param token     El token JWT d'autenticació.
     * @return          1 si el client es crea correctament, 0 en cas contrari.
     */
    @Override
    public int createClient(ClientDTO clientdto, String token) {
        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))) {
            Historial historial = new Historial();
            historial.setPatologies("PENDENT");
            historialService.createHistorial(historial);

            //crear l'historial basic
            Client client = ClientMapper.toEntity(clientdto,
                    clinicaService.getClinicaById(clientdto.getClinicaId()),
                    historial);
            client.setHistorial(historial);
            //treure qualsevol id perque ho faci automaticament spring
            client.setIdUsuari(null);
            client.setContrasenya(passwordEncoder.encode(client.getPassword()));

            clientRepository.save(client);
            return 1;
        }
            return 0;
    }

    /**
     * Obté un client per ID. Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, es retorna el client amb l'ID indicat.
     * Si el client no es troba, es llença una excepció.
     *
     * Si és client només retornarà el client amb el id del token, ignorarà el param id
     *
     * @param id    L'ID del client a obtenir.
     * @param token El token JWT d'autenticació.
     * @return      El client convertit a un ClientDTO.
     * @throws SecurityException Si el token és expirat o l'usuari no té rol d'admin.
     * @throws EntityNotFoundException Si no es troba el client amb l'ID especificat.
     */
    @Override
    public ClientDTO getClientById(Long id, String token) {
        String tokenNoBearer = Utils.extractBearerToken(token);
        Rol rol = jwtService.getRolFromToken(tokenNoBearer);

        if (!jwtService.isTokenExpired(tokenNoBearer)){
            if (Rol.ADMIN == rol){
                Optional<Client> client = clientRepository.findById(id);
                if (client.isPresent()) {
                    return ClientMapper.toDto(client.get());
                }

            }
            if (Rol.CLIENT == rol){//nomes retornarà el seu client
                Optional<Client> client = clientRepository.findById(jwtService.getIdFromToken(tokenNoBearer));
                if (client.isPresent()) {
                    return ClientMapper.toDto(client.get());
                }
            }

            throw new EntityNotFoundException("No hi ha cap client amb el id entrat");
        }

        throw new SecurityException("Token expirat o no autoritzat");
    }

    /**
     * Obté una llista de tots els clients. Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, es retorna la llista de clients.
     * En cas contrari, es llença una excepció de seguretat.
     *
     * @param token El token JWT d'autenticació.
     * @return      Una llista de Clients convertits a ClientDTO.
     * @throws SecurityException Si el token és expirat o l'usuari no té rol d'admin.
     */
    @Override
    public List<ClientDTO> getAllClients(String token) {
        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) &&
                Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))) {
            List<Client> clients = clientRepository.findAll();

            return clients.stream()
                    .map(ClientMapper::toDto)
                    .collect(Collectors.toList());
        }
        throw new SecurityException("Token expirat o no ADMIN");
    }

    /**
     * Actualitza les dades d'un client existent. Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, es realitza l'actualització de les dades del client.
     * En cas contrari, es llença una excepció de seguretat.
     *
     * @param clientdto Els nous detalls del client a actualitzar.
     * @param token     El token JWT d'autenticació.
     * @return          Retorna un valor boolean que indica si l'actualització s'ha realitzat amb èxit.
     * @throws SecurityException Si el token és expirat o l'usuari no té rol d'admin.
     */
    @Override
    public boolean updateClient(ClientDTO clientdto, String token) {
        String tokenNoBearer = Utils.extractBearerToken(token);
        if (!jwtService.isTokenExpired(tokenNoBearer) &&
                (Rol.ADMIN == jwtService.getRolFromToken(tokenNoBearer))){
            Client client = clientRepository.findById(clientdto.getIdUsuari()).orElse(null);

            if (client == null) {
                return false;
            }

            Client c = ClientMapper.toEntity(clientdto,
                    clinicaService.getClinicaById(clientdto.getClinicaId()),
                    historialService.getHistorialById(clientdto.getHistorialId()));

            c.setContrasenya(client.getContrasenya());
            c.setIdUsuari(client.getIdUsuari());
            clientRepository.save(c);
            return true;

        }
        throw new SecurityException("Token expirat o no ADMIN");
    }

    /**
     * Elimina un client de la base de dades. Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, es procedeix a eliminar el client juntament amb el seu historial
     * i l'usuari associat. En cas contrari, es llença una excepció de seguretat.
     *
     * @param id     L'identificador del client a eliminar.
     * @param token  El token JWT d'autenticació.
     * @return       Retorna -1 si el client no existeix, 1 si l'eliminació és exitosa, i 0 si no s'ha pogut eliminar.
     * @throws SecurityException Si el token és expirat o l'usuari no té rol d'admin.
     */
    @Override
    public int deleteClient(Long id, String token) {
        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) &&
                (Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token)))) {
            Client client = clientRepository.findById(id).orElse(null);

            if (client == null) {
                return -1;
            }

            long idhistorial = client.getHistorial().getIdhistorial();
            clientRepository.eliminarClientPerId(id);
            historialService.deleteHistorial(idhistorial);
            usuariRepositori.deleteById(id);

            client = clientRepository.findById(id).orElse(null);
            if(client == null) {
                return 1;
            }
            return 0;
        }
        throw new SecurityException("Token expirat o no ADMIN");
    }

    // TEA 4
    /**
     * Actualitza les dades d’un client autenticat (rol CLIENT).
     * No es permet modificar el seu ID, la clínica ni l’historial associat.
     * Si el camp contrasenya és buit o nul, es manté la contrasenya actual.
     *
     * @param clientdto El DTO amb les dades actualitzades del client.
     * @param token     Token JWT amb la informació d’autenticació del client.
     * @return          true si l’actualització s’ha fet correctament, false si no s’ha trobat el client.
     * @throws SecurityException si el token ha expirat o no és d’un usuari amb rol CLIENT.
     */
    @Override
    public boolean updateClientClient(ClientDTO clientdto,String token){
        String tokenNoBearer = Utils.extractBearerToken(token);
        Long id = jwtService.getIdFromToken(tokenNoBearer);
        if (!jwtService.isTokenExpired(tokenNoBearer) &&
                (Rol.CLIENT == jwtService.getRolFromToken(tokenNoBearer))){

            Client client = clientRepository.findById(id).orElse(null);
            if (client == null) {
                return false;
            }

            //no podrà modificar ni la clinica ni el historial ni el seu id
            clientdto.setIdUsuari(id);
            clientdto.setClinicaId(client.getClinica().getIdClinica());
            clientdto.setHistorialId(client.getHistorial().getIdhistorial());


            Client updatedClient = ClientMapper.toEntity(clientdto,
                    client.getClinica(),
                    client.getHistorial());

            if (clientdto.getContrasenya() != null && !clientdto.getContrasenya().isEmpty()) {
                updatedClient.setContrasenya(passwordEncoder.encode(clientdto.getContrasenya()));
            } else {
                updatedClient.setContrasenya(client.getContrasenya());
            }

            clientRepository.save(updatedClient);
            return true;
        }
        throw new SecurityException("Token expirat o no CLIENT");
    }

    /**
     * Permet que un client es pugui eliminar el seu compte.
     * Aquest mètode utilitza el token d'autenticació per identificar el client i procedir a la seva eliminació,
     * incloent la seva informació en el sistema, com el seu historial i usuari associat.
     * Si el client no existeix, retorna un error. Si l'eliminació és exitosa, retorna 1, en cas contrari, retorna 0.
     *
     * @param token El token d'autenticació que permet identificar el client que vol eliminar el seu compte.
     * @return Un enter que indica l'estat de l'eliminació: -1 si no es troba el client, 1 si l'eliminació és exitosa, 0 si hi ha un error en l'eliminació.
     * @throws SecurityException Si el token és invàlid, caducat o el rol del client no és el correcte (ha de ser CLIENT).
     */
    @Override
    public int deleteClientClient(String token) {
        String tokenNoBearer = Utils.extractBearerToken(token);
        if (!jwtService.isTokenExpired(tokenNoBearer) &&
                jwtService.getRolFromToken(tokenNoBearer) == Rol.CLIENT) {

            Long id = jwtService.getIdFromToken(tokenNoBearer);
            Client client = clientRepository.findById(id).orElse(null);

            if (client == null) {
                return -1;
            }

            long idHistorial = client.getHistorial().getIdhistorial();
            clientRepository.eliminarClientPerId(id);
            historialService.deleteHistorial(idHistorial);
            usuariRepositori.deleteById(id);

            if (!clientRepository.existsById(id)) {
                return 1;
            }
            return 0;
        }
        throw new SecurityException("Token expirat o no CLIENT");
    }

    //TODO: CRUD client del treballador

}
