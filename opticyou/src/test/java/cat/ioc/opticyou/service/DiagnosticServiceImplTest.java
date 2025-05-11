package cat.ioc.opticyou.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import cat.ioc.opticyou.dto.DiagnosticDTO;
import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Diagnostic;
import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.repositori.DiagnosticRepositori;
import cat.ioc.opticyou.repositori.HistorialRepository;
import cat.ioc.opticyou.service.impl.DiagnosticServiceImpl;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;



import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Transactional
public class DiagnosticServiceImplTest {
    @Mock
    private DiagnosticRepositori diagnosticRepositori;

    @Mock
    private HistorialRepository historialRepository;

    @Mock
    private JwtServiceImpl jwtService;

    @InjectMocks
    private DiagnosticServiceImpl diagnosticService;

    private DiagnosticDTO diagnosticDTO;
    private String token;
    private Historial historial;
    private Diagnostic diagnostic;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        diagnosticDTO = new DiagnosticDTO();
        diagnosticDTO.setIddiagnostic(1L);
        diagnosticDTO.setDescripcio("Descripció de prova");
        diagnosticDTO.setHistorialId(1L);

        Client client = new Client();
        client.setIdUsuari(1L);
        client.setEmail("email@email.com");
        client.setContrasenya("1234");
        client.setNom("Client de Prova");


        historial = new Historial();
        historial.setIdhistorial(1L);
        historial.setClient(client);
        historial.setPatologies("Historial de prova");

        diagnostic = new Diagnostic();
        diagnostic.setHistorial(historial);
        diagnostic.setDescripcio("Diagnòstic de prova");

        token = "Bearer validToken";


        when(historialRepository.findById(anyLong())).thenReturn(Optional.of(historial));
    }

    @Test
    public void testCreateDiagnostic_Success() {
        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
        when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
        when(diagnosticRepositori.existsById(anyLong())).thenReturn(false);

        int result = diagnosticService.createDiagnostic(diagnosticDTO, token);

        assertEquals(1, result);
        verify(diagnosticRepositori, times(1)).save(any(Diagnostic.class));
    }

    @Test
    void testCreateDiagnosticAlreadyExists() {
        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.extractBearerToken(anyString())).thenReturn("validToken");

            when(jwtService.isTokenExpired(anyString())).thenReturn(false);
            when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);
            when(diagnosticRepositori.existsById(anyLong())).thenReturn(false);

            int result = diagnosticService.createDiagnostic(diagnosticDTO, token);

            assertEquals(1, result);
            verify(diagnosticRepositori, times(1)).save(any(Diagnostic.class));
        }
    }

    @Test
    void testCreateDiagnostic_HistorialNotFound() {
        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.extractBearerToken(anyString())).thenReturn("validToken");

            when(jwtService.isTokenExpired("validToken")).thenReturn(false);
            when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
            when(diagnosticRepositori.existsById(anyLong())).thenReturn(false);
            when(historialRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () ->
                    diagnosticService.createDiagnostic(diagnosticDTO, token)
            );
            verify(diagnosticRepositori, never()).save(any(Diagnostic.class));
        }
    }

    @Test
    void testGetAllDiagnosticsByHistorial() {
        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.extractBearerToken(anyString())).thenReturn("validToken");
            when(jwtService.isTokenExpired(anyString())).thenReturn(false);
            when(jwtService.getRolFromToken(anyString())).thenReturn(Rol.ADMIN);


            when(diagnosticRepositori.findByHistorialIdhistorial(historial.getIdhistorial()))
                    .thenReturn(Collections.singletonList(diagnostic));

            List<DiagnosticDTO> diagnostics = diagnosticService.getAllDiagnosticsByHistorial(historial.getIdhistorial(), token);

            assertNotNull(diagnostics);
            assertEquals(1, diagnostics.size());
            assertEquals("Diagnòstic de prova", diagnostics.get(0).getDescripcio());
        }
    }

    @Test
    void testUpdateDiagnostic() {
        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.extractBearerToken(anyString())).thenReturn("validToken");


            Long id = 1L;
            diagnosticDTO.setIddiagnostic(id);
            diagnosticDTO.setDescripcio("Descripció actualitzada");

            Diagnostic existing = new Diagnostic(
                    id,
                    "Descripció antiga",
                    new Timestamp(System.currentTimeMillis()),
                    historial
            );

            when(jwtService.isTokenExpired("validToken")).thenReturn(false);
            when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
            when(diagnosticRepositori.findById(id)).thenReturn(Optional.of(existing));

            boolean result = diagnosticService.updateDiagnostic(diagnosticDTO, "Bearer validToken");

            assertTrue(result);
            verify(diagnosticRepositori, times(1)).save(existing);
            assertEquals("Descripció actualitzada", existing.getDescripcio());
        }
    }

    @Test
    void testUpdateDiagnostic_NotFound() {
        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.extractBearerToken(anyString())).thenReturn("validToken");

            when(jwtService.isTokenExpired("validToken")).thenReturn(false);
            when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
            when(diagnosticRepositori.findById(anyLong())).thenReturn(Optional.empty());


            assertThrows(EntityNotFoundException.class, () ->
                    diagnosticService.updateDiagnostic(diagnosticDTO, token)
            );
            verify(diagnosticRepositori, never()).save(any(Diagnostic.class));
        }
    }

    @Test
    void testDeleteDiagnostic() {
        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.extractBearerToken(anyString())).thenReturn("validToken");

            when(jwtService.isTokenExpired(token)).thenReturn(false);
            when(jwtService.getRolFromToken(token)).thenReturn(Rol.ADMIN);
            when(diagnosticRepositori.existsById(diagnostic.getIddiagnostic())).thenReturn(true);

            boolean result = diagnosticService.deleteDiagnostic(diagnostic.getIddiagnostic(), token);

            assertTrue(result);
            verify(diagnosticRepositori).deleteById(diagnostic.getIddiagnostic());
        }
    }

    @Test
    void testDeleteDiagnosticNotFound() {
        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.extractBearerToken(anyString())).thenReturn("validToken");

            when(jwtService.isTokenExpired(token)).thenReturn(false);
            when(jwtService.getRolFromToken(token)).thenReturn(Rol.ADMIN);
            when(diagnosticRepositori.existsById(diagnostic.getIddiagnostic())).thenReturn(false);

            boolean result = diagnosticService.deleteDiagnostic(diagnostic.getIddiagnostic(), token);

            assertFalse(result);
            verify(diagnosticRepositori, never()).deleteById(diagnostic.getIddiagnostic());
        }
    }

    @Test
    void testDeleteDiagnostic_NotFound() {
        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.extractBearerToken(anyString())).thenReturn("validToken");


            when(jwtService.isTokenExpired("validToken")).thenReturn(false);
            when(jwtService.getRolFromToken("validToken")).thenReturn(Rol.ADMIN);
            when(diagnosticRepositori.existsById(anyLong())).thenReturn(false);


            boolean result = diagnosticService.deleteDiagnostic(42L, token);
            assertFalse(result);
            verify(diagnosticRepositori, never()).deleteById(anyLong());
        }
    }

}

