package cat.ioc.opticyou.service;


import cat.ioc.opticyou.dto.ClientDTO;

import java.util.List;

public interface ClientService {
    int createClient(ClientDTO clientdto,String token);
    ClientDTO getClientById(Long id, String token);

    List<ClientDTO> getAllClients(String token);
    boolean updateClient(ClientDTO clientDTO, String token);

    int deleteClient(Long id, String token);
}
