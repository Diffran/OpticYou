package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositori per gestionar les operacions relacionades amb les clíniques.
 * Permet realitzar operacions bàsiques com guardar, actualitzar i eliminar clíniques.
 */
@Repository
public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
}
