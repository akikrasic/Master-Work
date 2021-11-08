package srb.akikrasic.zahtevi.stari.svikorisnici;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Created by aki on 8/2/17.
 */
public class RegistracijaTest {
    private Registracija r;

    @Before
    public void setUp() throws Exception {
        r = new Registracija();
    }

    @Test
    public void regexTest(){
        Pattern p = Pattern.compile("[a-z]");
        assertTrue("aбqwdsa!123dfgffdhg".matches("^.{0,}[a-zа-ш]{1,}.{0,}$"));
    }
   // @Test
    public void sigurnosnaProveraImeNaziv(){
        assertTrue(r.sigurnosnaProvera("mika", "akikrasic@gmail.comn","aA1!aa","Branka Radicevica 16/7","Pirot","010373409","062341094","010341004"));

    }
    @Test
    public void sigurnosnaProveraImeNazivNull(){
        assertFalse(r.sigurnosnaProvera(null, "","","","","","",""));

    }
    @Test
    public void sigurnosnaProveraImeNazivFalse(){
        assertFalse(r.sigurnosnaProvera("", "","","","","","",""));

    }
   // @Test
    public void sigurnosnaProveraMail(){
        assertFalse(r.sigurnosnaProvera("mika", "slobodanmejl@gmail.comnn","","","","","",""));

    }
   // @Test
    public void sigurnosnaProveraMailNeregistrovaniKorisnik(){
        assertFalse(r.sigurnosnaProvera("mika", "mika@pero.com","","","","","",""));

    }
   // @Test
    public void sigurnosnaProveraMailKorisnik(){
        assertFalse(r.sigurnosnaProvera("mika", "akikrasic@gmail.com","","","","","",""));

    }
}