package cat.ioc.opticyou.mapper;

import cat.ioc.opticyou.dto.ClientDTO;
import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Clinica;
import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.util.Rol;

/**
 * Classe per mapejar entre Client i ClientDTO
 */
public class ClientMapper {
    /**
     * Converteix un Client a ClientDTO
     * @param client El client a convertir
     * @return ClientDTO
     */
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

    /**
     * Converteix un ClientDTO a Client
     * @param clientDTO El ClientDTO a convertir
     * @param clinica   La clínica associada al client
     * @param historial El historial associat al client
     * @return Client
     */
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
