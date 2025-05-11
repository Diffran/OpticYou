package cat.ioc.opticyou.service;

import cat.ioc.opticyou.dto.TreballadorDTO;
import cat.ioc.opticyou.mapper.TreballadorMapper;
import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Clinica;
import cat.ioc.opticyou.model.Treballador;
import cat.ioc.opticyou.repositori.TreballadorRepository;
import cat.ioc.opticyou.repositori.UsuariRepositori;
import cat.ioc.opticyou.service.impl.JwtServiceImpl;
import cat.ioc.opticyou.service.impl.TreballadorServiceImpl;
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
import cat.ioc.opticyou.repositori.ClientRepository;
import cat.ioc.opticyou.service.impl.ClientServiceImpl;
import cat.ioc.opticyou.service.impl.HistorialServiceImpl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
public class TreballadorServiceImplTest {

    @Mock
    private UsuariRepositori usuariRepositori;

    @Mock
    private TreballadorRepository treballadorRepository;

    @Mock
    private JwtServiceImpl jwtService;
    @Mock
    private ClinicaService clinicaService;

    @InjectMocks
    private TreballadorServiceImpl treballadorService;

    private TreballadorDTO treballadorDTO;
    private Treballador treballador, treballador2;

    private List<Treballador> treballadors;
    private String token;
    private Clinica clinica;

    @BeforeEach
    public void setUp() {
        token = "Bearer validToken";
        treballadorDTO = new TreballadorDTO();
        treballadorDTO.setClinicaId(1L);
        treballadorDTO.setContrasenya("1234");
        treballadorDTO.setNom("Treballador Prova");

        treballador = new Treballador();
        treballador.setIdUsuari(1L);
        treballador.setContrasenya("1234");
        treballador.setNom("Treballador Prova");

        treballador2 = new Treballador();
        treballador2.setIdUsuari(2L);
        treballador2.setContrasenya("5678");
        treballador2.setNom("Treballador Prova 2");

        treballadors = new ArrayList<>();
        treballadors.add(treballador);
        treballadors.add(treballador2);

        clinica = new Clinica();
    }

    @Test
    void testCreateTreballador_WithValidTokenAndAdminRole_Returns1() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
        when(clinicaService.getClinicaById(1L)).thenReturn(clinica);

        try (MockedStatic<Utils> utils = mockStatic(Utils.class)) {
            utils.when(() -> Utils.extractBearerToken(token)).thenReturn("validToken");
            try (MockedStatic<TreballadorMapper> mapper = mockStatic(TreballadorMapper.class)) {
                mapper.when(() -> TreballadorMapper.toEntity(any(TreballadorDTO.class), any(Clinica.class)))
                        .thenReturn(treballador);

                int result = treballadorService.createTreballador(treballadorDTO, token);

                assertEquals(1, result);
                verify(treballadorRepository).save(treballador);
            }
        }
    }

    @Test
    void testCreateTreballador_InvalidToken() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(true);

        int result = treballadorService.createTreballador(treballadorDTO, token);

        assertEquals(0, result);
        verify(treballadorRepository, times(0)).save(any(Treballador.class));
    }

    @Test
    void testCreateTreballador_InvalidRole() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.CLIENT); // no ADMIN

        int result = treballadorService.createTreballador(treballadorDTO, token);

        assertEquals(0, result);
        verify(treballadorRepository, times(0)).save(any(Treballador.class));
    }

    @Test
    void testGetTreballadorById_Success() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
        when(treballadorRepository.findById(1L)).thenReturn(Optional.of(treballador));

        TreballadorDTO result = treballadorService.getTreballadorById(1L, token);

        assertEquals(treballador.getIdUsuari(), result.getIdUsuari());
        assertEquals(treballador.getNom(), result.getNom());
    }

    @Test
    void testGetTreballadorById_EntityNotFoundException() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
        when(treballadorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            treballadorService.getTreballadorById(1L, token);
        });
    }

    @Test
    void testGetAllTreballadors_Success() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
        when(treballadorRepository.findAll()).thenReturn(treballadors);

        List<TreballadorDTO> result = treballadorService.getAllTreballadors(token);

        assertEquals(2, result.size());
        assertEquals(treballador.getIdUsuari(), result.get(0).getIdUsuari());
        assertEquals(treballador.getNom(), result.get(0).getNom());
        assertEquals(treballador2.getIdUsuari(), result.get(1).getIdUsuari());
        assertEquals(treballador2.getNom(), result.get(1).getNom());
    }

    @Test
    void testGetAllTreballadors_EmptyList() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
        when(treballadorRepository.findAll()).thenReturn(Arrays.asList());

        List<TreballadorDTO> result = treballadorService.getAllTreballadors(token);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateTreballador_TreballadorNotFound_ReturnsFalse() {
        when(jwtService.isTokenExpired("validToken")).thenReturn(false);
        when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
        when(treballadorRepository.findById(treballadorDTO.getIdUsuari())).thenReturn(Optional.empty());

        boolean result = treballadorService.updateTreballador(treballadorDTO, token);

        assertFalse(result);
        verify(treballadorRepository, times(0)).save(any(Treballador.class));
    }

    @Test
    void testUpdateTreballador_ModificaTelefon_ReturnsTrue() {
        Treballador treballadorOriginal = new Treballador();
        treballadorOriginal.setIdUsuari(1L);
        treballadorOriginal.setEspecialitat("Oftalmoleg");
        treballadorOriginal.setContrasenya("existingPassword");

        treballadorDTO.setIdUsuari(1L);
        treballadorDTO.setEspecialitat("Oftalmoleg");
        treballadorDTO.setClinicaId(1L);

        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(treballadorRepository.findById(1L)).thenReturn(Optional.of(treballadorOriginal));

        Clinica clinica = new Clinica();
        clinica.setIdClinica(1L);

        when(clinicaService.getClinicaById(anyLong())).thenReturn(clinica);

        Treballador treballadorModificat = new Treballador();
        treballadorModificat.setIdUsuari(1L);
        treballadorModificat.setEspecialitat("una altre especialitat");
        treballadorModificat.setContrasenya("existingPassword");

        try (MockedStatic<TreballadorMapper> mockedMapper = mockStatic(TreballadorMapper.class)) {
            mockedMapper.when(() -> TreballadorMapper.toEntity(eq(treballadorDTO), any(Clinica.class)))
                    .thenReturn(treballadorModificat);

            boolean result = treballadorService.updateTreballador(treballadorDTO, token);

            assertTrue(result);
            verify(treballadorRepository).save(treballadorModificat);
            assertEquals("una altre especialitat", treballadorModificat.getEspecialitat());
            assertEquals("existingPassword", treballadorModificat.getContrasenya());
        }
    }

    @Test
    void testDeleteTreballador_TreballadorNotFound_ReturnsMinusOne() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(treballadorRepository.findById(1L)).thenReturn(Optional.empty());

        int result = treballadorService.deleteTreballador(1L, token);

        assertEquals(-1, result);
        verify(treballadorRepository, never()).deleteById(anyLong());
        verify(usuariRepositori, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteTreballador_SuccessfulDeletion_ReturnsOne() {
        Treballador treballador = new Treballador();
        treballador.setIdUsuari(1L);

        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(treballadorRepository.findById(1L))
                .thenReturn(Optional.of(treballador))
                .thenReturn(Optional.empty());

        int result = treballadorService.deleteTreballador(1L, token);

        assertEquals(1, result);
        verify(treballadorRepository).deleteById(1L);
        verify(usuariRepositori).deleteById(1L);
    }

}
