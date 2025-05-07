package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Diagnostic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosticRepositori extends JpaRepository<Diagnostic, Long> {
    List<Diagnostic> findByHistorialIdhistorial(Long idhistorial);
}
