package cat.ioc.opticyou.service;

import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.repositori.HistorialRepository;
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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
public class HistorialServiceImplTest {
    @Mock
    private HistorialRepository historialRepository;

    @Mock
    private JwtServiceImpl jwtService;

    @InjectMocks
    private HistorialServiceImpl historialService;

    private Historial historial;
    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        token = "Bearer validToken";
        historial = new Historial();
        historial.setIdhistorial(1L);
    }

    @Test
    void testUpdateHistorial_HistorialExiste_ReturnsTrue() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(historialRepository.findById(1L)).thenReturn(Optional.of(historial));

        try (MockedStatic<Utils> utils = mockStatic(Utils.class)) {
            utils.when(() -> Utils.extractBearerToken(token)).thenReturn("validToken");

            boolean result = historialService.updateHistorial(historial, token);

            assertTrue(result);
            verify(historialRepository, times(1)).save(historial);
        }
    }

    @Test
    void testUpdateHistorial_HistorialNoExisteix_ReturnsFalse() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(historialRepository.findById(anyLong())).thenReturn(Optional.empty());

        Historial historial = new Historial();
        historial.setIdhistorial(1L);

        boolean result = historialService.updateHistorial(historial, "Bearer validToken");

        assertFalse(result);
        verify(historialRepository, never()).save(any(Historial.class));
    }

    @Test
    void testGetHistorialById_Success_ReturnsHistorial() {
        Long historialId = 1L;
        Historial historial = new Historial();
        historial.setIdhistorial(historialId);
        historial.setPatologies("PENDENT");

        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(historialRepository.findById(historialId)).thenReturn(Optional.of(historial));

        Historial result = historialService.getHistorialById(historialId, token);
        assertNotNull(result);
        assertEquals(historialId, result.getIdhistorial());
        assertEquals("PENDENT", result.getPatologies());
    }

    @Test
    void testGetHistorialById_NotFound_ThrowsEntityNotFoundException() {
        Long historialId = 1L;

        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(historialRepository.findById(historialId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> historialService.getHistorialById(historialId, token));
    }

    @Test
    void testGetAllHistorial_Success_ReturnsHistorials() {
        Historial historial1 = new Historial();
        historial1.setIdhistorial(1L);
        historial1.setPatologies("Historial 1");

        Historial historial2 = new Historial();
        historial2.setIdhistorial(2L);
        historial2.setPatologies("Historial 2");

        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(historialRepository.findAll()).thenReturn(Arrays.asList(historial1, historial2));

        List<Historial> result = historialService.getAllHistorial(token);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllHistorial_NoHistorials_ReturnsEmptyList() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(historialRepository.findAll()).thenReturn(Arrays.asList());

        List<Historial> result = historialService.getAllHistorial(token);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateHistorial_Success_Returns1() {
        Historial historial = new Historial();
        historial.setIdhistorial(1L);
        historial.setPatologies("Historial Test");

        when(historialRepository.save(any(Historial.class))).thenReturn(historial);

        int result = historialService.createHistorial(historial);

        assertEquals(1, result);
        verify(historialRepository).save(historial);
    }

    @Test
    void testGetHistorialById_Success_ReturnsHistorial2() {
        Long historialId = 1L;
        Historial historial = new Historial();
        historial.setIdhistorial(historialId);
        historial.setPatologies("Historial Test");

        when(historialRepository.findById(historialId)).thenReturn(Optional.of(historial));

        Historial result = historialService.getHistorialById(historialId);

        assertNotNull(result);
        assertEquals(historialId, result.getIdhistorial());
    }

    @Test
    void testGetHistorialById_NotFound_ThrowsEntityNotFoundException2() {
        Long historialId = 1L;

        when(historialRepository.findById(historialId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> historialService.getHistorialById(historialId));
    }

    @Test
    void testDeleteHistorial_Success_ReturnsTrue() {
        Long historialId = 1L;
        Historial historial = new Historial();
        historial.setIdhistorial(historialId);

        when(historialRepository.findById(historialId)).thenReturn(Optional.of(historial));
        doNothing().when(historialRepository).deleteById(historialId);

        boolean result = historialService.deleteHistorial(historialId);

        assertTrue(result);
        verify(historialRepository).deleteById(historialId);
    }

    @Test
    void testDeleteHistorial_NotFound_ReturnsFalse() {
        Long historialId = 1L;

        when(historialRepository.findById(historialId)).thenReturn(Optional.empty());

        boolean result = historialService.deleteHistorial(historialId);

        assertFalse(result);
        verify(historialRepository).deleteById(historialId);
    }
}
