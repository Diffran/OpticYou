package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Diagnostic;
import cat.ioc.opticyou.model.Historial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class DiagnosticRepositoryTest {
    @Autowired
    private DiagnosticRepositori diagnosticRepositori;

    @Autowired
    private HistorialRepository historialRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Historial historial;

    @BeforeEach
    void setUp() {
        Client client = new Client();
        client.setEmail("test@cli.com");
        client.setContrasenya("pass");
        client.setNom("Client Test");


        historial = new Historial();
        historial.setPatologies("Cap");
        historial.setClient(client);
        client.setHistorial(historial);


        historialRepository.save(historial);
        clientRepository.save(client);


        Diagnostic d1 = new Diagnostic();
        d1.setHistorial(historial);
        d1.setDescripcio("Diag 1");
        d1.setDate(new Timestamp(System.currentTimeMillis()));
        diagnosticRepositori.save(d1);

        Diagnostic d2 = new Diagnostic();
        d2.setHistorial(historial);
        d2.setDescripcio("Diag 2");
        d2.setDate(new Timestamp(System.currentTimeMillis()));
        diagnosticRepositori.save(d2);
    }

    @Test
    void testFindByHistorialIdhistorial_ReturnsDiagnostics() {
        List<Diagnostic> list = diagnosticRepositori.findByHistorialIdhistorial(historial.getIdhistorial());
        assertThat(list).hasSize(2)
                .extracting(Diagnostic::getDescripcio)
                .containsExactlyInAnyOrder("Diag 1", "Diag 2");
    }

    @Test
    void testFindByHistorialIdhistorial_Empty() {
        List<Diagnostic> list = diagnosticRepositori.findByHistorialIdhistorial(999L);
        assertThat(list).isEmpty();
    }
}

