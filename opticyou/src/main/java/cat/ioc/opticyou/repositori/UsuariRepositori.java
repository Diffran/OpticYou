package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositori per gestionar les operacions de base de dades relacionades amb l'entitat Usuari
 */
@Repository
public interface UsuariRepositori extends JpaRepository<Usuari, Long> {
    /**
     * Busca a un usuari pel seu email.
     *
     * @param email
     * @return que conté l'usuari si es troba, o buit si no existeix. Amb un Optional
     */
    Optional<Usuari> findByEmail(String email);
}
