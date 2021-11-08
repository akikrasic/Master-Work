package srb.akikrasic.baza.novipaket.baza;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.domen.Proizvod;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by aki on 12/8/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class BazaKljucneReciTest {

    @Autowired
    private BazaKljucneReci b;

    @Test
    public void vratiteSveKljucnereciZaProizvodStringovi() throws Exception {
        Proizvod p = new Proizvod();
        p.setId(36);//taj ima sigurno kljucne reci
        List<String> l= b.vratiteSveKljucnereciZaProizvodStringovi(p);
        assertFalse(l.isEmpty());
        System.out.println(l);
    }
}