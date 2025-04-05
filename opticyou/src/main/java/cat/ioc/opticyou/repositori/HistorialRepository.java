package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialRepository extends JpaRepository<Historial,Long> {
}
