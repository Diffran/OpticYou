package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.JwtBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositori per gestionar la blacklist de tokens JWT.
 * Guarda els tokens del logout per evitar-ne l'ús posterior.
 */
@Repository
public interface BlackListRespository extends JpaRepository<JwtBlackList, String> {
    /**
     * Comprova si un token existeix a la blacklist.
     *
     * @param token El token.
     * @return true si el token està a la blacklist, false en cas contrari.
     */
    boolean existsByToken(String token);

}
