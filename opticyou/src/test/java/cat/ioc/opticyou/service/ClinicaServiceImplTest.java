package cat.ioc.opticyou.service;

import cat.ioc.opticyou.dto.ClinicaDTO;
import cat.ioc.opticyou.mapper.ClinicaMapper;
import cat.ioc.opticyou.model.Clinica;
import cat.ioc.opticyou.repositori.ClinicaRepository;
import cat.ioc.opticyou.service.impl.JwtServiceImpl;
import cat.ioc.opticyou.service.impl.ClinicaServiceImpl;
import cat.ioc.opticyou.util.Rol;
import cat.ioc.opticyou.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
public class ClinicaServiceImplTest {
    @Mock
    private ClinicaRepository clinicaRepository;

    @Mock
    private JwtServiceImpl jwtService;

    @InjectMocks
    private ClinicaServiceImpl clinicaService;

    private ClinicaDTO clinicaDTO;
    private String token;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        clinicaDTO = new ClinicaDTO();
        clinicaDTO.setIdClinica(1L);
        token = "Bearer validToken";
    }

    @Test
    public void testCreateClinica_Success() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clinicaRepository.existsById(anyLong())).thenReturn(false);

        int result = clinicaService.createClinica(clinicaDTO, token);

        assertEquals(1, result);
        verify(clinicaRepository, times(1)).save(any(Clinica.class));
    }

    @Test
    public void testCreateClinica_AlreadyExists() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clinicaRepository.existsById(anyLong())).thenReturn(true);

        int result = clinicaService.createClinica(clinicaDTO, token);

        assertEquals(-1, result);
        verify(clinicaRepository, times(0)).save(any(Clinica.class));
    }

    @Test
    public void testCreateClinica_InvalidToken() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(true);

        int result = clinicaService.createClinica(clinicaDTO, token);

        assertEquals(0, result);
        verify(clinicaRepository, times(0)).save(any(Clinica.class));
    }

    @Test
    public void testCreateClinica_InvalidRole() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.CLIENT);

        int result = clinicaService.createClinica(clinicaDTO, token);

        assertEquals(0, result);
        verify(clinicaRepository, times(0)).save(any(Clinica.class));
    }

    //getbyId
    @Test
    void testGetClinicaById_TokenExpired_ThrowsSecurityException() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.CLIENT);

        assertThrows(SecurityException.class, () -> clinicaService.getClinicaById(1L, token));
    }

    @Test
    void testGetClinicaById_ForbiddenAccess_ThrowsSecurityException() {
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.CLIENT);

        assertThrows(SecurityException.class, () -> clinicaService.getClinicaById(1L, token));
    }

    @Test
    void testGetClinicaById_NotFound_ThrowsEntityNotFoundException() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clinicaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clinicaService.getClinicaById(1L, token));
    }


    @Test
    void testGetClinicaById_Success_ReturnsClinicaDTO() {
        Long clinicaId = 1L;
        Clinica clinica = new Clinica(clinicaId, "Clinica Test", null,null,null, null,null);

        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clinicaRepository.findById(clinicaId)).thenReturn(Optional.of(clinica));

        ClinicaDTO result = clinicaService.getClinicaById(clinicaId, token);
        assertNotNull(result);
        assertEquals("Clinica Test",result.getNom());
    }
     //getAll
     @Test
     void testGetAllClinicas_Success_ReturnsClinicas() {
         Clinica clinica1 = new Clinica(1L, "Clinica 1", null, null, null, null, null);
         Clinica clinica2 = new Clinica(2L, "Clinica 2", null, null, null, null, null);

         when(jwtService.isTokenExpired(anyString())).thenReturn(false);
         when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
         when(clinicaRepository.findAll()).thenReturn(Arrays.asList(clinica1, clinica2));

         List<ClinicaDTO> result = clinicaService.getAllClinicas(token);

         assertNotNull(result);
         assertEquals(2, result.size());
     }

    @Test
    void testGetAllClinicas_NoClinicas_ReturnsEmptyList() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clinicaRepository.findAll()).thenReturn(Arrays.asList());

        List<ClinicaDTO> result = clinicaService.getAllClinicas(token);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //update
    @Test
    void testUpdateClinica_ClinicaNoExiste_ReturnsFalse() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clinicaRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = clinicaService.updateClinica(clinicaDTO, token);

        assertFalse(result);
        verify(clinicaRepository, never()).save(any(Clinica.class));
    }

    @Test
    void testUpdateClinica_ClinicaExiste_ReturnsTrue() {
        Clinica clinica = new Clinica();
        clinica.setIdClinica(1L);

        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clinicaRepository.findById(1L)).thenReturn(Optional.of(clinica));

        boolean result = clinicaService.updateClinica(clinicaDTO, token);

        assertTrue(result);
        verify(clinicaRepository, times(1)).save(any(Clinica.class));
    }

    @Test
    void testUpdateClinica_ModificaNom_ReturnsTrue() {
        Clinica clinicaOriginal = new Clinica();
        clinicaOriginal.setIdClinica(1L);
        clinicaOriginal.setNom("Original");

        clinicaDTO.setIdClinica(1L);
        clinicaDTO.setNom("Modificat");

        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clinicaRepository.findById(1L)).thenReturn(Optional.of(clinicaOriginal));

        Clinica clinicaModificada = new Clinica();
        clinicaModificada.setIdClinica(1L);
        clinicaModificada.setNom("Modificat");

        try (MockedStatic<ClinicaMapper> mockedMapper = mockStatic(ClinicaMapper.class)) {
            mockedMapper.when(() -> ClinicaMapper.toEntity(clinicaDTO)).thenReturn(clinicaModificada);

            boolean result = clinicaService.updateClinica(clinicaDTO, token);

            assertTrue(result);
            verify(clinicaRepository).save(clinicaModificada);
            assertEquals("Modificat", clinicaModificada.getNom());
        }
    }
    //delete

    @Test
    void testDeleteClinica_ClinicaExiste_ReturnsTrue() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clinicaRepository.existsById(1L)).thenReturn(true);

        boolean result = clinicaService.deleteClinica(1L, token);

        assertTrue(result);
        verify(clinicaRepository).deleteById(1L);
    }

    @Test
    void testDeleteClinica_ClinicaNoExisteix_ReturnsFalse() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(clinicaRepository.existsById(1L)).thenReturn(false);

        boolean result = clinicaService.deleteClinica(1L, token);

        assertFalse(result);
        verify(clinicaRepository, never()).deleteById(anyLong());
    }


}
