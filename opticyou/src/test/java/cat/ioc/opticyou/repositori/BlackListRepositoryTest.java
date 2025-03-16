package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.JwtBlackList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class BlackListRepositoryTest {
    @Autowired
    private BlackListRespository blackListRespository;

    @Test
    public void testExistsByToken() {
        JwtBlackList jwtBlackList = new JwtBlackList("token_blacklisted");
        blackListRespository.save(jwtBlackList);

        boolean exists = blackListRespository.existsByToken("token_blacklisted");

        assertTrue(exists);
    }
    @Test
    public void testNotExistsByToken() {
        boolean exists = blackListRespository.existsByToken("token_per_guardar_a_la_black_list");
        assertFalse(exists);
    }
    @Test
    public void testSaveToken() {
        JwtBlackList jwtBlackList = new JwtBlackList("token_a_guardar");
        blackListRespository.save(jwtBlackList);

        boolean exists = blackListRespository.existsByToken("token_a_guardar");
        assertTrue(exists);
    }

    @Test
    public void testDeleteToken() {
        JwtBlackList jwtBlackList = new JwtBlackList("token_a_eliminar");
        blackListRespository.save(jwtBlackList);

        blackListRespository.delete(jwtBlackList);

        boolean exists = blackListRespository.existsByToken("token_a_eliminar");
        assertFalse(exists);
    }
}
