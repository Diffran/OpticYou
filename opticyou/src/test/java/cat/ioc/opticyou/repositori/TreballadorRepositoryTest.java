package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Clinica;
import cat.ioc.opticyou.model.Treballador;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class TreballadorRepositoryTest {
    @Autowired
    private TreballadorRepository treballadorRepository;

    @Autowired
    private EntityManager entityManager;

    private Treballador treballador;
    private Clinica clinica;

    @BeforeEach
    public void setUp() {
        clinica = new Clinica();
        clinica.setNom("Clinica Test");
        clinica.setEmail("clinica@email.com");
        entityManager.persist(clinica);


        treballador = new Treballador();
        treballador.setNom("Treballador Test");
        treballador.setEmail("email@email.com");
        treballador.setContrasenya("1234");
        treballador.setClinica(clinica);
        entityManager.persist(treballador);
    }

    @Test
    void testFindByClinicaId() {
        List<Treballador> result = treballadorRepository.findByClinicaId(clinica.getIdClinica());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(treballador.getNom(), result.get(0).getNom());
        assertEquals(clinica.getIdClinica(), result.get(0).getClinica().getIdClinica());
    }

    @Test
    void testFindByClinicaId_NoResult() {
        List<Treballador> result = treballadorRepository.findByClinicaId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
