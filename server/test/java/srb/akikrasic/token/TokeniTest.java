package srb.akikrasic.token;

import org.apache.catalina.util.URLEncoder;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static org.junit.Assert.*;

/**
 * Created by aki on 7/24/17.
 */
public class TokeniTest {
    private Tokeni tokeni;
    @Before
    public void pocetak(){
        tokeni = new Tokeni();
    }
    @Test
    public void daLiJeUlogovanKorisnik() throws Exception {
        String tokenZaProveru="dfashfdsa`23213123asdgasdhf";
        //to treba da se prepravi
        //tokeni.korisnikUspesnoUlogovan("mika@pera.com",tokenZaProveru );
        assertTrue(tokeni.daLiJeUlogovanKorisnik("mika@pera.com", tokenZaProveru));
    }

    @Test
    public void daLiJeUlogovanNeispravno() throws Exception {
        String tokenZaProveru="dfashfdsa`23213123asdgasdhf";
        //to treba da se prepravi
       // tokeni.korisnikUspesnoUlogovan("mika@pera.com",tokenZaProveru );
        assertFalse(tokeni.daLiJeUlogovanKorisnik("mika@pera.com", tokenZaProveru+"13172372"));
    }

    @Test
    public void daLiJeUlogovanAdmin() throws Exception {
        String token = tokeni.adminUspesnoUlogovan();
        assertTrue(tokeni.daLiJeUlogovanAdmin(token));

    }

    @Test
    public void daLiJeUlogovanAdminNeispravno() throws Exception {
        String token = tokeni.adminUspesnoUlogovan();
        assertFalse(tokeni.daLiJeUlogovanAdmin(token+"123"));
    }
    @Test
    public void daLiJeUlogovanAdminNeispravnoPrazanString() throws Exception {
        assertFalse(tokeni.daLiJeUlogovanAdmin(""));
    }

   /* @Test
    public void testiranjeTokena() throws UnsupportedEncodingException {
        //String token = tokeni.generisiteTokenZaNeregistrovanogKorisnika("Mika", "mika@pera.com", "asdffhgfdshfgh 19","010341004" );
        String kodiraniToken64 = Base64.encodeBase64String(token.getBytes());
        String enkodovaNiToken = java.net.URLEncoder.encode(kodiraniToken64, "UTF-8");
        String dekodovaniToken64 = URLDecoder.decode(enkodovaNiToken, "UTF-8");
        String dobijeniToken = new String(Base64.decodeBase64(dekodovaniToken64.getBytes()));
        assertTrue(token.equals( dobijeniToken));

    }*/
}