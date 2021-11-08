package srb.akikrasic.baza.novipaket.baza;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.Poruka;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by aki on 12/8/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class BazaPorukaTest {
    private String email = "akikrasic@gmail.com";
    private String email2="akikrasicDRUGIKORISNIK@gmail.com";

    @Autowired
    private BazaPoruka b;
    @Test
    public void vratiteSagovornikeZaKorisnika() throws Exception {
        List<Korisnik> l = b.vratiteSagovornikeZaKorisnika(email);
        assertFalse(l.isEmpty());
    }

    @Test
    public void vratitePoruke() throws Exception {
        int limit=20;
        List<Poruka> poruke = b.vratitePoruke(email, email2,limit, 0 );
        assertTrue(poruke.size()==limit);
        for(int i=0;i<limit;i++) {
            Poruka p = poruke.get(i);
            assertNotNull(p.getTekst());
            assertNotNull(p.getDatum());
        }
    }
}