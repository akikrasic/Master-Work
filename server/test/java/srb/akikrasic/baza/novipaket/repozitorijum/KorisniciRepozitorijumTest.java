package srb.akikrasic.baza.novipaket.repozitorijum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.baza.novipaket.baza.BazaLogovanje;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.loginkorisnika.RezultatLogovanja;

import static org.junit.Assert.*;

/**
 * Created by aki on 11/29/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class KorisniciRepozitorijumTest extends ZajednickiPodaciZaTest{

    @Autowired
    private KorisniciRepozitorijum korR;
    @Autowired
    private BazaLogovanje bazaLogovanje;

    @Test //19.09.2017.
    public void posaljiteZahtevZaPromenuSifreIPromeniteSifru() throws Exception {
        String email="akikrasic@gmail.com";
        byte[] staraSifra="aA1!aa".getBytes();
        byte[] sifra="aA1!aaNova sifra".getBytes();
        String token = korR.posaljiteZahtevZaPromenuSifre(email);
        assertTrue(korR.promenaSifre(email, token, sifra));
        RezultatLogovanja rl = bazaLogovanje.logovanjeKorisnika(email, sifra);
        assertTrue(rl.isUspesno());

        token = korR.posaljiteZahtevZaPromenuSifre(email);
        assertTrue(korR.promenaSifre(email, token, staraSifra));
        rl = bazaLogovanje.logovanjeKorisnika(email, staraSifra);
        assertTrue(rl.isUspesno());

    }


    @Test
    public void posaljiteZahtevZaPromenuSifreNeispravanMejl() throws Exception {
        assertNull(korR.posaljiteZahtevZaPromenuSifre("neispravanMejl"));
    }

    @Test
    public void promenaSifreFalse() throws Exception {
        assertFalse(korR.promenaSifre("neispravanMejl", "", "".getBytes()));
    }

    @Test
    public void pretragaKorisnikaPoEmailu() throws Exception {

        Korisnik k = korR.pretragaKorisnikaPoEmailu(email);
        assertEquals(email,k.getEmail() );
    }

    @Test
    public void pretragaKorisnikaPoEmailuNeuspesna() throws Exception {
        Korisnik k = korR.pretragaKorisnikaPoEmailu(neispravanEmail);
        assertNull(k);
    }

    @Test
    public void izmenitePodatkeZaKorisnikaNeispravanMejl() throws Exception {
        assertFalse(korR.izmenitePodatkeZaKorisnika("", neispravanEmail,"".getBytes(),"", "", "", "", ""));

    }

    @Test
    public void izmenitePodatkeZaKorisnika() throws Exception {
        String novoImeNaziv = "Aleksandar Krasic";
        String email = "akikrasic@gmail.com";
        byte[] novaSifra = "aaAA11!!".getBytes();
        String novaAdresa ="Branka Radicevica 16/7";
        String novoMesto="Pirot";
        String novTel1="0103410041";
        String novTel2 = "0623410941";
        String novTel3= "0103734091";

        Korisnik k = korR.pretragaKorisnikaPoEmailu(email);
        String imeNaziv = k.getImeNaziv();
        String adresa = k.getAdresa();
        String mesto = k.getMesto();
        String tel1 =k.getTel1();
        String tel2= k.getTel2();
        String tel3 = k.getTel3();

        assertTrue( korR.izmenitePodatkeZaKorisnika(novoImeNaziv, email, "".getBytes(), novaAdresa, novoMesto, novTel1, novTel2, novTel3));
        k = korR.pretragaKorisnikaPoEmailu(email);
        assertEquals(novoImeNaziv, k.getImeNaziv());
        assertEquals(novaAdresa, k.getAdresa());
        assertEquals(novoMesto, k.getMesto());
        assertEquals(novTel1, k.getTel1());
        assertEquals(novTel2, k.getTel2());
        assertEquals(novTel3, k.getTel3());

        assertTrue(korR.izmenitePodatkeZaKorisnika(imeNaziv, email, "".getBytes(), adresa, mesto, tel1, tel2, tel3));
        k = korR.pretragaKorisnikaPoEmailu(email);
        assertEquals(imeNaziv, k.getImeNaziv());
        assertEquals(adresa, k.getAdresa());
        assertEquals(mesto, k.getMesto());
        assertEquals(tel1, k.getTel1());
        assertEquals(tel2, k.getTel2());
        assertEquals(tel3, k.getTel3());


    }

    @Test
    public void izmenitePodatkeZakorisnikaSifra() throws Exception {
        byte[] novaSifra ="aaAA11!!".getBytes();
        byte[] staraSifra ="aA1!aa".getBytes();
        Korisnik k = korR.pretragaKorisnikaPoEmailu(email);
        korR.izmenitePodatkeZaKorisnika(k.getImeNaziv(),email, novaSifra, k.getAdresa(), k.getMesto(), k.getTel1(), k.getTel2(), k.getTel3());
        RezultatLogovanja rl = bazaLogovanje.logovanjeKorisnika(email, novaSifra);
        assertTrue(rl.isUspesno());
        korR.izmenitePodatkeZaKorisnika(k.getImeNaziv(),email, staraSifra, k.getAdresa(), k.getMesto(), k.getTel1(), k.getTel2(), k.getTel3());
        rl = bazaLogovanje.logovanjeKorisnika(email, staraSifra);
        assertTrue(rl.isUspesno());

    }





}