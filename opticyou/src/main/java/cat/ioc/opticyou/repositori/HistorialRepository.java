package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.dto.HistorialDTO;
import cat.ioc.opticyou.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<Historial,Long> {
    @Query("SELECT c FROM Client c WHERE c.sexe = :sexe")// TODO: fer bé la query
    List<HistorialDTO> findClientsBySexe(@Param("id") Long clinicaId);
}
