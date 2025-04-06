package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositori per gestionar les operacions relacionades amb els històrics dels clients.
 * Permet realitzar operacions bàsiques com guardar, actualitzar i eliminar històrics.
 */
@Repository
public interface HistorialRepository extends JpaRepository<Historial,Long> {
}
