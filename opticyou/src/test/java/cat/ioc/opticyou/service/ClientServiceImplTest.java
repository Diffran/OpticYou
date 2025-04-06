package cat.ioc.opticyou.service;

import cat.ioc.opticyou.dto.ClientDTO;
import cat.ioc.opticyou.mapper.ClientMapper;
import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Clinica;
import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.repositori.ClientRepository;
import cat.ioc.opticyou.repositori.UsuariRepositori;
import cat.ioc.opticyou.service.impl.ClientServiceImpl;
import cat.ioc.opticyou.service.impl.HistorialServiceImpl;
import cat.ioc.opticyou.service.impl.JwtServiceImpl;
import cat.ioc.opticyou.util.Rol;
import cat.ioc.opticyou.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
public class ClientServiceImplTest {
    @Mock
    private UsuariRepositori usuariRepositori;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private HistorialServiceImpl historialService;

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private ClinicaService clinicaService;

    @InjectMocks
    private ClientServiceImpl clientService;

    private ClientDTO clientDTO;
    private String token;
    private Clinica clinica;
    private Historial historial;
    private Client client;
    private Client client2;
    private List<Client> clients;

    @BeforeEach
    public void setUp() {
        token = "Bearer validToken";
        clientDTO = new ClientDTO();
        clientDTO.setClinicaId(1L);
        clientDTO.setNom("Updated Client");

        clinica = new Clinica();

        historial = new Historial();
        historial.setIdhistorial(99L);

        client = new Client();
        client.setIdUsuari(1L);
        client.setNom("Test Client");

        client2 = new Client();
        client2.setIdUsuari(2L);
        client2.setNom("Test Client 2");

        clients = new ArrayList<>();
        clients.add(client);
        clients.add(client2);
    }

    //create
    @Test
    void testCreateClient_WithValidTokenAndAdminRole_Returns1() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
        when(clinicaService.getClinicaById(1L)).thenReturn(clinica);
        when(historialService.createHistorial(any(Historial.class))).thenReturn(1);


        try (MockedStatic<Utils> utils = mockStatic(Utils.class)) {
            utils.when(() -> Utils.extractBearerToken(token)).thenReturn("validToken");
            try (MockedStatic<ClientMapper> clientMapper = mockStatic(ClientMapper.class)) {
                Client client = new Client();
                client.setIdUsuari(null);
                clientMapper.when(() -> ClientMapper.toEntity(any(ClientDTO.class), any(Clinica.class), any(Historial.class)))
                        .thenReturn(client);
                int result = clientService.createClient(clientDTO, token);

                assertEquals(1, result);
                verify(clientRepository).save(client);
            }
        }
    }

    @Test
    void testCreateClient_InvalidToken() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(true);
        int result = clientService.createClient(clientDTO, token);


        assertEquals(0, result);
        verify(clientRepository, times(0)).save(any(Client.class));
    }

    @Test
    void testCreateClient_InvalidRole() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.CLIENT);

        int result = clientService.createClient(clientDTO, token);

        assertEquals(0, result);
        verify(clientRepository, times(0)).save(any(Client.class));
    }

    //getbyid
    @Test
    void testGetClientById_Success() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientDTO result = clientService.getClientById(1L, token);

        assertEquals(client.getIdUsuari(), result.getIdUsuari());
        assertEquals(client.getNom(), result.getNom());
    }


    @Test
    void testGetClientById_EntityNotFoundException() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () -> {
            clientService.getClientById(1L, token);
        });
    }

    @Test
    void testGetClientById_SecurityException() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(true);

        assertThrows(SecurityException.class, () -> {
            clientService.getClientById(1L, token);
        });
    }

    @Test
    void testGetClientById_InvalidRole() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.CLIENT);

        assertThrows(SecurityException.class, () -> {
            clientService.getClientById(1L, token);
        });
    }

    //getAll
    @Test
    void testGetAllClients_Success() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
        when(clientRepository.findAll()).thenReturn(clients);

        List<ClientDTO> result = clientService.getAllClients(token);

        assertEquals(2, result.size());
        assertEquals(client.getIdUsuari(), result.get(0).getIdUsuari());
        assertEquals(client.getNom(), result.get(0).getNom());
        assertEquals(client2.getIdUsuari(), result.get(1).getIdUsuari());
        assertEquals(client2.getNom(), result.get(1).getNom());
    }
    @Test
    void testGetAllClients_EmptyList() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
        when(clientRepository.findAll()).thenReturn(Arrays.asList());

        List<ClientDTO> result = clientService.getAllClients(token);

        assertTrue(result.isEmpty());
    }
    //update
    @Test
    void testUpdateClient_ClientNotFound_ReturnsFalse() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
        when(clientRepository.findById(clientDTO.getIdUsuari())).thenReturn(Optional.empty());

        boolean result = clientService.updateClient(clientDTO, token);

        assertFalse(result);
        verify(clientRepository, times(0)).save(any(Client.class));
    }

    @Test
    void testUpdateClient_ModificaTelefon_ReturnsTrue() {
        Client clientOriginal = new Client();
        clientOriginal.setIdUsuari(1L);
        clientOriginal.setTelefon("123456789");
        clientOriginal.setContrasenya("existingPassword");


        clientDTO.setIdUsuari(1L);
        clientDTO.setTelefon("987654321");
        clientDTO.setHistorialId(1L);
        clientDTO.setClinicaId(1L);

        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(clientOriginal));

        Clinica dummyClinica = new Clinica();
        dummyClinica.setIdClinica(1L);
        Historial dummyHistorial = new Historial();
        dummyHistorial.setIdhistorial(1L);

        when(clinicaService.getClinicaById(anyLong())).thenReturn(dummyClinica);
        when(historialService.getHistorialById(anyLong())).thenReturn(dummyHistorial);


        Client clientModificat = new Client();
        clientModificat.setIdUsuari(1L);
        clientModificat.setTelefon("987654321");
        clientModificat.setContrasenya("existingPassword");

        try (MockedStatic<ClientMapper> mockedMapper = mockStatic(ClientMapper.class)) {
            mockedMapper.when(() -> ClientMapper.toEntity(eq(clientDTO), any(Clinica.class), any(Historial.class)))
                    .thenReturn(clientModificat);

            boolean result = clientService.updateClient(clientDTO, token);

            assertTrue(result);
            verify(clientRepository).save(clientModificat);
            assertEquals("987654321", clientModificat.getTelefon());
            assertEquals("existingPassword", clientModificat.getContrasenya());
        }
    }

    //delete
    @Test
    void testDeleteClient_ClientNotFound_ReturnsMinusOne() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        int result = clientService.deleteClient(1L, token);

        assertEquals(-1, result);
        verify(clientRepository, never()).eliminarClientPerId(anyLong());
        verify(historialService, never()).deleteHistorial(anyLong());
        verify(usuariRepositori, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteClient_SuccessfulDeletion_ReturnsOne() {
        Client client = new Client();
        client.setIdUsuari(1L);
        Historial historial = new Historial();
        historial.setIdhistorial(1L);
        client.setHistorial(historial);

        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clientRepository.findById(1L))
                .thenReturn(Optional.of(client))
                .thenReturn(Optional.empty());

        int result = clientService.deleteClient(1L, token);

        assertEquals(1, result);
        verify(clientRepository).eliminarClientPerId(1L);
        verify(historialService).deleteHistorial(1L);
        verify(usuariRepositori).deleteById(1L);
    }
}
