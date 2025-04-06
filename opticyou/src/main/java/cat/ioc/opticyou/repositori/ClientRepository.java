package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repositori per gestionar les operacions relacionades amb els clients.
 * Permet realitzar operacions com eliminar un client per ID.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    /**
     * Elimina un client per ID.
     *
     * @param id L'ID del client a eliminar.
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM opticyou.client WHERE idclient = :id", nativeQuery = true)
    void eliminarClientPerId(@Param("id") Long id);
}
