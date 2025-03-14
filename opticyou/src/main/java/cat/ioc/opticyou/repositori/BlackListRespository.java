package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.JwtBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRespository extends JpaRepository<JwtBlackList, String> {
    boolean existsByToken(String token);
}
