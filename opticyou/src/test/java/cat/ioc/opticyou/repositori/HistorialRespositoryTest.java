package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Historial;
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
public class HistorialRespositoryTest {

    @Autowired
    private HistorialRepository historialRepository;

    private Historial historial;

    @BeforeEach
    public void setUp() {
        historial = new Historial();
        historial.setPatologies("Historial de prova");
        historial = historialRepository.save(historial);
    }

    @Test
    public void testFindById_Exist() {
        Optional<Historial> histExist = historialRepository.findById(historial.getIdhistorial());
        assertTrue(histExist.isPresent());
        assertEquals(historial.getPatologies(), histExist.get().getPatologies());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<Historial> histExist = historialRepository.findById(999L);
        assertFalse(histExist.isPresent());
    }

    @Test
    public void testSaveHistorial() {
        Historial newHist = new Historial();
        newHist.setPatologies("Historial nou");
        Historial savedHist = historialRepository.save(newHist);

        assertNotNull(savedHist);
        assertNotNull(savedHist.getIdhistorial());
        assertEquals("Historial nou", savedHist.getPatologies());
    }

    @Test
    public void testDeleteHistorial() {
        historialRepository.delete(historial);

        Optional<Historial> histExist = historialRepository.findById(historial.getIdhistorial());
        assertFalse(histExist.isPresent());
    }

    @Test
    public void testFindAllHistorials() {
        Historial hist2 = new Historial();
        hist2.setPatologies("Historial secundari");
        historialRepository.save(hist2);

        List<Historial> historials = historialRepository.findAll();
        assertNotNull(historials);

        assertTrue(historials.size() > 0);
        assertTrue(historials.stream().anyMatch(h -> h.getPatologies().equals("Historial de prova")));
        assertTrue(historials.stream().anyMatch(h -> h.getPatologies().equals("Historial secundari")));
    }

    @Test
    public void testDeleteByIdHistorial() {
        historialRepository.deleteById(historial.getIdhistorial());

        Optional<Historial> histExist = historialRepository.findById(historial.getIdhistorial());
        assertFalse(histExist.isPresent());
    }
}
