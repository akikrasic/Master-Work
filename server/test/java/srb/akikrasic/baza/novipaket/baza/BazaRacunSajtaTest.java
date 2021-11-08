package srb.akikrasic.baza.novipaket.baza;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.domen.Racun;

import static org.junit.Assert.*;

/**
 * Created by aki on 12/8/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class BazaRacunSajtaTest {

    @Autowired
    private BazaRacunSajta b;

    @Test
    public void snimiteNaRacunSajtaIVratiteStanjeRacunaSajta() throws Exception {
        double stanje =b.vratiteStanjeRacunaSajta();
        double kolicinaZaDodavanje = 100;
        b.snimiteNaRacunSajta(stanje+kolicinaZaDodavanje);
        assertTrue((stanje+kolicinaZaDodavanje)==b.vratiteStanjeRacunaSajta());
        b.snimiteNaRacunSajta(stanje);
        assertTrue(stanje== b.vratiteStanjeRacunaSajta());

    }

    @Test
    public void snimiteNaRacunZaPrenosIVratiteStanjeRacunaZaPrenos() throws Exception {
        double stanje = b.vratiteStanjeRacunaZaPrenos();
        double kolicinaZaDodavanje = 100;
        b.snimiteNaRacunZaPrenos(stanje+kolicinaZaDodavanje);
        assertTrue((stanje+kolicinaZaDodavanje)==b.vratiteStanjeRacunaZaPrenos());
        b.snimiteNaRacunZaPrenos(stanje);
        assertTrue(stanje== b.vratiteStanjeRacunaZaPrenos());
    }

}