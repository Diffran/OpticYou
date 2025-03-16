package cat.ioc.opticyou.repositori;

import cat.ioc.opticyou.model.Usuari;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class UsurariRepositoriTest {

    @Autowired
    private UsuariRepositori usuariRepositori;

    @Test
    public void testFindByEmail_Exist() {

        Usuari usuari = new Usuari();
        usuari.setEmail("test@example.com");
        usuari.setNom("Usuari");
        usuari.setContrasenya("1234");
        usuariRepositori.save(usuari);


        Optional<Usuari> usuariExist = usuariRepositori.findByEmail("test@example.com");


        assertTrue(usuariExist.isPresent());
        assertTrue(usuariExist.get().getEmail().equals(usuari.getEmail()));
    }

    @Test
    public void testFindByEmail_NotFound() {
        Optional<Usuari> usuariExist = usuariRepositori.findByEmail("test@example.com");

        assertFalse(usuariExist.isPresent());//no existeix, torna false
    }

}
