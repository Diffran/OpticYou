package cat.ioc.opticyou.service;

import cat.ioc.opticyou.model.Usuari;
import cat.ioc.opticyou.service.impl.JwtServiceImpl;
import cat.ioc.opticyou.util.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
public class JwtServiceTest {
    @Autowired
    private JwtServiceImpl jwtService;
    private Usuari usuari;
    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    @BeforeEach
    public void setUp() {
        usuari = new Usuari();
        usuari.setEmail("test@example.com");
        usuari.setNom("Usuari");
        usuari.setContrasenya("1234");
        usuari.setRol(Rol.ADMIN);

        jwtService = new JwtServiceImpl(secretKey);
    }

    @Test
    public void testGetToken_Success() {
        String token = jwtService.getToken(usuari);

        assertNotNull(token);
        assertEquals("test@example.com", jwtService.getEmailFromToken(token));
        assertEquals(Rol.ADMIN, jwtService.getRolFromToken(token));
    }

    @Test
    public void testGetToken_Fail() {
        Usuari usuariIncorrecte = new Usuari();
        usuariIncorrecte.setEmail("incorrect@example.com");
        usuariIncorrecte.setNom("Usuari Incorrecte");
        usuariIncorrecte.setContrasenya("wrongpassword");
        usuariIncorrecte.setRol(Rol.CLIENT);

        String token = jwtService.getToken(usuariIncorrecte);

        assertNotNull(token);
        assertNotEquals("test@example.com", jwtService.getEmailFromToken(token));
        assertNotEquals(Rol.ADMIN, jwtService.getRolFromToken(token));
    }
}
