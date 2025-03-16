package cat.ioc.opticyou.service;

import cat.ioc.opticyou.dto.UsuariDTO;
import cat.ioc.opticyou.mapper.UsuariMapper;
import cat.ioc.opticyou.model.Usuari;
import cat.ioc.opticyou.repositori.UsuariRepositori;
import cat.ioc.opticyou.service.impl.UsuariServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
public class UsuariServiceTest {
    @Mock
    private UsuariRepositori usuariRepositori;

    @InjectMocks
    private UsuariServiceImpl usuariService;

    private List<Usuari> usuaris;

    @BeforeEach
    public void setUp() {
        usuaris = new ArrayList<>();

        Usuari usuari1 = new Usuari();
        usuari1.setEmail("test@example.com");
        usuari1.setNom("Usuari");
        usuari1.setContrasenya("1234");
        usuariRepositori.save(usuari1);
        usuaris.add(usuari1);

        Usuari usuari2 = new Usuari();
        usuari2.setEmail("test2@example.com");
        usuari2.setNom("Usuari2");
        usuari2.setContrasenya("4321");
        usuariRepositori.save(usuari2);
        usuaris.add(usuari2);

        Usuari usuari3 = new Usuari();
        usuari3.setEmail("test3@example.com");
        usuari3.setNom("Usuari3");
        usuari3.setContrasenya("5678");
        usuariRepositori.save(usuari3);
        usuaris.add(usuari3);
    }

    @Test
    public void testGetByEmail_Success() {
        when(usuariRepositori.findByEmail("test@example.com")).thenReturn(Optional.of(usuaris.get(0)));
        UsuariDTO usuariDTO = usuariService.getByEmail("test@example.com");

        assertNotNull(usuariDTO);
        assertEquals("test@example.com", usuariDTO.getEmail());
    }

    @Test
    public void testGetByEmail_Fail() {
        assertThrows(EntityNotFoundException.class,() -> usuariService.getByEmail("other@example.com"));
    }

    @Test
    public void testGetAll_Success(){
        when(usuariRepositori.findAll()).thenReturn(usuaris);

        List<UsuariDTO> resultatEsperat = usuaris.stream()
                .map(UsuariMapper::toDto)
                .collect(Collectors.toList());

        List<UsuariDTO> resultat = usuariService.getAll();

        assertNotNull(resultat);
        assertEquals(3, resultat.size());
        assertEquals(resultatEsperat, resultat);
    }

    @Test
    public void testLoadUserByUsername_Success() {
        when(usuariRepositori.findByEmail("test@example.com")).thenReturn(Optional.of(usuaris.get(0)));

        UserDetailsService userDetailsService = usuariService.userDetailsService();

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("1234", userDetails.getPassword());
    }

    @Test
    public void testLoadUserByUsername_Fail() {
        when(usuariRepositori.findByEmail("test@example.com")).thenReturn(Optional.empty());

        UserDetailsService userDetailsService = usuariService.userDetailsService();
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("test@example.com"));
    }



}
