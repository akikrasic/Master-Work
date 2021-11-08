package srb.akikrasic.baza.novipaket.baza;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * Created by aki on 12/8/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class BazaPrivremeniBrojTest {

    @Autowired
    private BazaPrivremeniBroj b;
    @Test
    public void privremeniProizvodVrednost() throws Exception {
        long vr = b.ucitajteVrednostBrojaPrivremenogProizvodaNaPocetku();
        assertTrue(vr>=0L);
        long vr1 = vr+1;
        b.sacuvajteVrednostBrojaPrivremenogPRoizvodaNaKraju(vr1);

        long vr2 = b.ucitajteVrednostBrojaPrivremenogProizvodaNaPocetku();
        assertEquals(vr1, vr2);
        b.sacuvajteVrednostBrojaPrivremenogPRoizvodaNaKraju(vr);
    }


}