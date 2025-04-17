package cat.ioc.opticyou.service;


import cat.ioc.opticyou.dto.ClientDTO;
import cat.ioc.opticyou.model.Client;

import java.util.List;

public interface ClientService {
    int createClient(ClientDTO clientdto,String token);
    ClientDTO getClientById(Long id, String token);

    List<ClientDTO> getAllClients(String token);
    boolean updateClient(ClientDTO clientDTO, String token);

    int deleteClient(Long id, String token);
    boolean updateClientClient(ClientDTO clientdto,String token);
    int deleteClientClient(String token);
}
