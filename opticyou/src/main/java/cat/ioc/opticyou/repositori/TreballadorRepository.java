package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Treballador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repositori per gestionar les operacions de base de dades relacionades amb l'entitat Treballador
 */
@Repository
public interface TreballadorRepository extends JpaRepository<Treballador,Long> {
    @Query("SELECT t FROM Treballador t WHERE t.clinica.id = :clinicaId")
    List<Treballador> findByClinicaId(@Param("clinicaId") Long clinicaId);
}
