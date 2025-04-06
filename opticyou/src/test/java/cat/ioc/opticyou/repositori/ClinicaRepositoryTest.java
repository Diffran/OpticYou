package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Clinica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class ClinicaRepositoryTest {
    @Autowired
    private ClinicaRepository clinicaRepository;

    private Clinica clinica;

    @BeforeEach
    public void setUp() {
        clinica = new Clinica();
        clinica.setNom("Clinica Test");
        clinica.setEmail("test@example.com");
        clinica = clinicaRepository.save(clinica);
    }

    @Test
    public void testFindById_Exist() {
        Optional<Clinica> clinicaExist = clinicaRepository.findById(clinica.getIdClinica());

        assertTrue(clinicaExist.isPresent());
        assertEquals(clinica.getNom(), clinicaExist.get().getNom());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<Clinica> clinicaExist = clinicaRepository.findById(999L);

        assertFalse(clinicaExist.isPresent());
    }

    @Test
    public void testSaveClinica() {
        Clinica savedClinica = clinicaRepository.save(clinica);

        assertNotNull(savedClinica);
        assertNotNull(savedClinica.getIdClinica());
        assertEquals("Clinica Test", savedClinica.getNom());
        assertEquals("test@example.com", savedClinica.getEmail());
    }

    @Test
    public void testDeleteClinica() {
        // La clínica fue creada en setUp()
        clinicaRepository.delete(clinica);

        Optional<Clinica> clinicaExist = clinicaRepository.findById(clinica.getIdClinica());
        assertFalse(clinicaExist.isPresent());
    }

    @Test
    public void testFindAllClinicas() {
        Clinica clinica2 = new Clinica();
        clinica2.setNom("Clinica 2");
        clinica2.setEmail("clinica2@example.com");

        clinicaRepository.save(clinica);
        clinicaRepository.save(clinica2);

        List<Clinica> clinicas = clinicaRepository.findAll();

        assertNotNull(clinicas);
        assertTrue(clinicas.size() > 0);
        assertTrue(clinicas.stream().anyMatch(c -> c.getNom().equals("Clinica Test")));
        assertTrue(clinicas.stream().anyMatch(c -> c.getNom().equals("Clinica 2")));
    }

    @Test
    public void testDeleteByIdClinica() {
        clinicaRepository.save(clinica);

        clinicaRepository.deleteById(clinica.getIdClinica());

        Optional<Clinica> clinicaExist = clinicaRepository.findById(clinica.getIdClinica());
        assertFalse(clinicaExist.isPresent());
    }
}

