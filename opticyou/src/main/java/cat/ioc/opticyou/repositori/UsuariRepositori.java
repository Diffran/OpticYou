package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuariRepositori extends JpaRepository<Usuari, Long> {
    Optional<Usuari> findByEmail(String email);
}
