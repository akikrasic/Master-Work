package srb.akikrasic.baza.novipaket.baza;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaReferenca;

import static org.junit.Assert.*;

/**
 * Created by aki on 2/17/18.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class BazaNitiTest {
    @Autowired
    private BazaReferenca b;
    @Autowired
    private BazaNiti bn;

    @Test
    public void proveriteDaLiSuTokeniZaPromenuSifreValidni() throws Exception {

    }

    @Test
    public void proveriteDaLiJeProdavacOdgovorioNaZalbu() throws Exception {
    }

    @Test
    public void proveriteDaLiJeKupacOdgovorioNaPoslatProizvod() throws Exception {
    }

}