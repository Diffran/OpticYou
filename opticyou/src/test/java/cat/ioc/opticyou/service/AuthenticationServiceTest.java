package cat.ioc.opticyou.service;

import cat.ioc.opticyou.dto.loginLogout.JwtAuthenticationDTO;
import cat.ioc.opticyou.dto.loginLogout.LoginRequestDTO;
import cat.ioc.opticyou.model.Usuari;
import cat.ioc.opticyou.repositori.UsuariRepositori;
import cat.ioc.opticyou.service.impl.AuthenticationServiceImpl;
import cat.ioc.opticyou.service.impl.JwtServiceImpl;
import cat.ioc.opticyou.util.Rol;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthenticationServiceTest {
    @Mock
    private UsuariRepositori usuariRepositori;

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private Usuari usuari;

    @BeforeEach
    public void setUp() {
        usuari = new Usuari(1L, "Usuari1", "test@example.com", "1234", Rol.ADMIN);
    }

    //TODO: no em funciona....
    @Test
    public void testLogin_Success() {
        LoginRequestDTO request = new LoginRequestDTO("test@example.com", "1234");

        when(usuariRepositori.findByEmail("test@example.com")).thenReturn(Optional.of(usuari));
        when(jwtService.getToken(usuari)).thenReturn("token_del_login");

        JwtAuthenticationDTO response = authenticationService.login(request);

        assertNotNull(response);
        assertEquals("test@example.com", jwtService.getEmailFromToken(response.getToken()));
        assertEquals("token_del_login", response.getToken());
    }

    @Test
    public void testLogin_Fail_UserNotFound() {
        LoginRequestDTO request = new LoginRequestDTO("noexisteix@example.com", "1234");

        when(usuariRepositori.findByEmail("noexisteix@example.com")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> authenticationService.login(request));
    }
}
