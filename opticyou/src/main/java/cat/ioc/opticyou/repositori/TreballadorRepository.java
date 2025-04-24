package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Treballador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repositori per gestionar les operacions de base de dades relacionades amb l'entitat Treballador
 */
@Repository
public interface TreballadorRepository extends JpaRepository<Treballador,Long> {
}
