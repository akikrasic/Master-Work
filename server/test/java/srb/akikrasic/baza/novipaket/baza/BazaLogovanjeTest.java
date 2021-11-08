package srb.akikrasic.baza.novipaket.baza;

import org.junit.Before;
import org.junit.Test;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaReferenca;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.loginkorisnika.RezultatLogovanja;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Sifra;
import srb.akikrasic.token.Tokeni;

import static org.junit.Assert.*;

/**
 * Created by aki on 11/25/17.
 */
public class BazaLogovanjeTest {
    private BazaLogovanje b ;
    @Before
    public void setUp() throws Exception {
        b = new BazaLogovanje();
        b.setB(new BazaReferenca());
        b.setRadSaSifrom(new Sifra());
        b.setRadSaTokenima(new Tokeni());
        b.getRadSaTokenima().setRadSaGenerisanjemTokena(new GenerisanjeTokena());
    }



        @Test
        public void logovanjeUspesno(){
            RezultatLogovanja rl = b.logovanjeKorisnika("akikrasic@gmail.com", "aA1!aa".getBytes());
            assertTrue(rl.isUspesno());
        }
        @Test
        public void logovanjeNeuspesno(){
            RezultatLogovanja rl = b.logovanjeKorisnika("akikrasic@gmail.com", "aA1!aaAA".getBytes());
            assertFalse(rl.isUspesno());
        }


}