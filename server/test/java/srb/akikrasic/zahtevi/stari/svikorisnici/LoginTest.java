package srb.akikrasic.zahtevi.stari.svikorisnici;

import org.junit.Before;
import org.junit.Test;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.loginkorisnika.RezultatLogovanja;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Tokeni;

import static org.junit.Assert.*;

/**
 * Created by aki on 8/24/17.
 */
public class LoginTest {
    private Login l;
    @Before
    public void inic(){
        l = new Login();
        Tokeni t = new Tokeni();
        GenerisanjeTokena gt = new GenerisanjeTokena();
        t.setRadSaGenerisanjemTokena(gt);
        l.setRadSaTokenima(t);

    }
    @Test
    public void logovanjeAdmina() throws Exception {
        RezultatLogovanja r = l.logovanjeAdmina("zmaj", "fraSi");
        assertTrue(r.isUspesno());
    }
    @Test
    public void logovanjeAdminaNeuspesnoKor() throws Exception {
        RezultatLogovanja r = l.logovanjeAdmina("zmaj1", "fraSi");
        assertFalse(r.isUspesno());

    }
    @Test
    public void logovanjeAdminaNeuspesnoSifra() throws Exception {
        RezultatLogovanja r = l.logovanjeAdmina("zmaj1", "fraSi123");
        assertFalse(r.isUspesno());

    }

}