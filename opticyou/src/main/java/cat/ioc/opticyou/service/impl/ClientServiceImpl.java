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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

            clientRepository.save(client);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public ClientDTO getClientById(Long id, String token) {
        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) &&
                (Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token)))){

            Optional<Client> client = clientRepository.findById(id);

            if (client.isPresent()) {
                return ClientMapper.toDto(client.get());
            } else {
                throw new EntityNotFoundException("No hi ha cap clinica amb id: " + id);
            }
        } else {
            throw new SecurityException("Token expirat o no ADMIN");
        }
    }

    @Override
    public List<ClientDTO> getAllClients(String token) {
        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) &&
                Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))) {
            List<Client> clients = clientRepository.findAll();

            return clients.stream()
                    .map(ClientMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            throw new SecurityException("Token expirat o no ADMIN");
        }
    }

    @Override
    public boolean updateClient(ClientDTO clientdto, String token) {
        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) &&
                (Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token)))){
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

        } else {
            throw new SecurityException("Token expirat o no ADMIN");
        }
    }

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
        } else {
            throw new SecurityException("Token expirat o no ADMIN");
        }
    }
}
