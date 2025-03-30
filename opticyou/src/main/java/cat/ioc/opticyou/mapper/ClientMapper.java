package cat.ioc.opticyou.mapper;

import cat.ioc.opticyou.dto.ClientDTO;
import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Clinica;
import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.util.Rol;


public class ClientMapper {
    public static ClientDTO toDto(Client client){
        ClientDTO clientDTO = new ClientDTO();

        clientDTO.setIdUsuari(client.getIdUsuari());
        clientDTO.setNom(client.getNom());
        clientDTO.setEmail(client.getEmail());
        clientDTO.setRol(Rol.CLIENT);
        clientDTO.setTelefon(client.getTelefon());
        clientDTO.setSexe(client.getSexe());
        clientDTO.setDataNaixament(client.getDataNaixament());
        clientDTO.setClinicaId(client.getClinica() != null ? client.getClinica().getIdClinica() : null);
        clientDTO.setHistorialId(client.getHistorial() != null ? client.getHistorial().getIdhistorial() : null);

        return clientDTO;
    }

    public static Client toEntity(ClientDTO clientDTO, Clinica clinica, Historial historial){
        Client client = new Client();

        client.setIdUsuari(clientDTO.getIdUsuari());
        client.setNom(clientDTO.getNom());
        client.setEmail(clientDTO.getEmail());
        client.setContrasenya(clientDTO.getContrasenya());
        client.setRol(Rol.CLIENT);
        client.setTelefon(clientDTO.getTelefon());
        client.setSexe(clientDTO.getSexe());
        client.setDataNaixament(clientDTO.getDataNaixament());
         client.setClinica(clinica);
         client.setHistorial(historial);

        return client;
    }
}
