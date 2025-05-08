package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Diagnostic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositori per gestionar les operacions de base de dades relacionades amb l'entitat Treballador.
 * Permet realitzar operacions com la cerca de treballadors associats a una clínica específica.
 */
@Repository
public interface DiagnosticRepositori extends JpaRepository<Diagnostic, Long> {
    List<Diagnostic> findByHistorialIdhistorial(Long idhistorial);
}
