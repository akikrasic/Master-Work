package srb.akikrasic.baza.novipaket.repozitorijum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.domen.Korisnik;

import static org.junit.Assert.*;

/**
 * Created by aki on 11/25/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class NepotvrdjeniKorisniciRepozitorijumTest extends ZajednickiPodaciZaTest{
    @Autowired
    private NepotvrdjeniKorisniciRepozitorijum npkr;

    @Autowired
    private KorisniciRepozitorijum korR;
    @Test
    public void daLiSuUcitaniUMapu() throws Exception {
        assertTrue(npkr.getNepotvrdjeniKorisniciMapa().size()>0);
           npkr.getNepotvrdjeniKorisniciMapa().values().forEach(nk->{
            System.out.println(nk.getEmail());
        });

    }


    @Test
    public void snimiteIObrisiteINeregistrovanogIRegistrovanogKorisnika() throws Exception{
        String email = "mika1@pera.com";
        String token = "token proba";
        npkr.snimiteNovogNepotvrdjenogKorisnika("Mika1", email, "sifra".getBytes(), "Branka Radicevica", "Pirot", "010341004", "062341094", "010373409", token);
        assertFalse(npkr.daLiJeEmailSlobodanKodNepotvrdjenogKorisnika(email));
        assertFalse(npkr.daLiJeEmailSlobodanUObeTabele(email));
        Korisnik k = npkr.daLiJeMogucaPotvrdaKorisnika(email, token);
        System.out.println(k+" korisnik je tu da vidimo null li j e");
        assertTrue(npkr.potvrditeKorisnika(k));
        assertFalse(korR.daLiJeEmailSlobodanKodPotvrdjenogKorisnika(email));
        assertTrue(korR.obrisiteKorisnika(email));
        assertTrue(korR.daLiJeEmailSlobodanKodPotvrdjenogKorisnika(email));
        assertNull(korR.pretragaKorisnikaPoEmailu(email));

    }


    @Test
    public void daLiJeEmailSlobodanUObeTabele() throws Exception {
        assertTrue(npkr.daLiJeEmailSlobodanUObeTabele("ka163404m@student.etf.rs"));
    }
    @Test
    public void daLiEmailNijeSlobodanNepotvrdjeniKorisnik(){
        //  b.snimiteNovogKorisnika("", "test", "".getBytes(), "", "", "", "", "","");
        assertFalse(npkr.daLiJeEmailSlobodanUObeTabele("mika@pero.com"));//taj email ne treba da se brise iz tabele Nepotvrdjenikorisnik
    }
    @Test
    public void daLiEmailNijeSlobodanKorisnik(){
        //  b.snimiteNovogKorisnika("", "test", "".getBytes(), "", "", "", "", "","");
        assertFalse(npkr.daLiJeEmailSlobodanUObeTabele("akikrasic@gmail.com"));
    }

    @Test
    public void daLiJeMogucaPotvrdaKorisnika() throws Exception {
        String email = "email@testPotvrde";
        String token = "tokenZaPotvrduITest";
        //samo prvi put bez komentara
        // b.snimiteNovogNepotvrdjenogKorisnika("",email , "".getBytes(), "","", "", "", "", token);
        assertNotNull(npkr.daLiJeMogucaPotvrdaKorisnika(email, token));
    }
    @Test
    public void daLiJeMogucaPotvrdaKorisnikaGreskaMail() throws Exception {
        String email = "email@testPotvrdeGreska";
        String token = "tokenZaPotvrduITest";
        //samo prvi put bez komentara
        // b.snimiteNovogNepotvrdjenogKorisnika("",email , "".getBytes(), "","", "", "", "", token);
        assertNull(npkr.daLiJeMogucaPotvrdaKorisnika(email, token));
    }
    @Test
    public void daLiJeMogucaPotvrdaKorisnikaGreskaToken() throws Exception {
        String email = "email@testPotvrde";
        String token = "tokenZaPotvrduITestGreska";
        //samo prvi put bez komentara
        // b.snimiteNovogNepotvrdjenogKorisnika("",email , "".getBytes(), "","", "", "", "", token);
        assertNull(npkr.daLiJeMogucaPotvrdaKorisnika(email, token));
    }





}