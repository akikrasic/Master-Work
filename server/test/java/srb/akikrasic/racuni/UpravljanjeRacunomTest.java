package srb.akikrasic.racuni;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.domen.Racun;

import static org.junit.Assert.*;

/**
 * Created by aki on 10/10/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class UpravljanjeRacunomTest  {

    protected String email = "akikrasic@gmail.com";
    protected String email2="akikrasicDRUGIKORISNIK@gmail.com";
    protected String neispravanEmail = "email@koji je sigurno neispravan";
    protected byte[] sifra = "aA1!aa".getBytes();
    protected int proizvodId=36;

    @Autowired
    private UpravljanjeRacunom upravljanjeRacunom;

    @Test
    public void dodajteNaRacunSajtaIVratiteStanjeRacunaSajta() throws Exception {
        double stanje =upravljanjeRacunom.vratiteStanjeRacunaSajta();
        double kolicinaZaDodavanje = 100;
        upravljanjeRacunom.dodajteNaRacunSajta(kolicinaZaDodavanje);
        assertTrue((stanje+kolicinaZaDodavanje)==upravljanjeRacunom.vratiteStanjeRacunaSajta());
        kolicinaZaDodavanje=-kolicinaZaDodavanje;
        upravljanjeRacunom.dodajteNaRacunSajta(kolicinaZaDodavanje);
        assertTrue(stanje== upravljanjeRacunom.vratiteStanjeRacunaSajta());

    }

    @Test
    public void dodajteNaRacunZaPrenosIVratiteStanjeRacunaZaPrenos() throws Exception {
        double stanje = upravljanjeRacunom.vratiteStanjeRacunaZaPrenos();
        double kolicinaZaDodavanje = 100;
        upravljanjeRacunom.dodajteNaRacunZaPrenos(kolicinaZaDodavanje);
        assertTrue((stanje+kolicinaZaDodavanje)==upravljanjeRacunom.vratiteStanjeRacunaZaPrenos());
        upravljanjeRacunom.skiniteSaRacunaZaPrenos(kolicinaZaDodavanje);
        assertTrue(stanje== upravljanjeRacunom.vratiteStanjeRacunaZaPrenos());
    }



    @Test
    public void vratiteStanjeRacuna() throws Exception {
        Racun r = upravljanjeRacunom.vratiteStanjeRacuna();
        assertTrue(r.getRacunSajta()>0);
        assertTrue(r.getRacunZaPrenos()>0);
    }

    @Test
    public void snimiteStanjeNaRacunKorisnikaIVratiteStanjeRacunaKorisnika() throws Exception {
        double stanje = upravljanjeRacunom.vratiteStanjeRacunaKorisnika(email);
        double kolicinaZaDodavanje = 100;
        upravljanjeRacunom.dodajteNaRacunKorisnika(email,kolicinaZaDodavanje);
        assertTrue((stanje+kolicinaZaDodavanje)==upravljanjeRacunom.vratiteStanjeRacunaKorisnika(email));
        kolicinaZaDodavanje= -kolicinaZaDodavanje;
        upravljanjeRacunom.dodajteNaRacunKorisnika(email, kolicinaZaDodavanje);
        assertTrue(stanje== upravljanjeRacunom.vratiteStanjeRacunaKorisnika(email));
    }
    @Test
    public void daLiRadiRacunKorisnika(){
        assertTrue(upravljanjeRacunom.vratiteStanjeRacunaKorisnika(email)>0);
    }
    @Test
     public void prebaciteNaRacunKorisnika(){
        Double stanjeRacunaSajta = upravljanjeRacunom.vratiteStanjeRacunaSajta();
        Double stanjeRacunaZaPrenos= upravljanjeRacunom.vratiteStanjeRacunaZaPrenos();
        Double stanjeRacunaKorisnika = upravljanjeRacunom.vratiteStanjeRacunaKorisnika(email);
        //ovo je malo bez veze ali ce prodje
        double kolicinaZaPrebacivanjeKorisniku = 100;
        double sajtuIde=10;
        double korisnikuIde=90;
        upravljanjeRacunom.prebaciteNaRacunKorisnika(email, kolicinaZaPrebacivanjeKorisniku);
        Double stanjeRacunaSajtaPosle = upravljanjeRacunom.vratiteStanjeRacunaSajta();
        Double stanjeRacunaZaPrenosPosle= upravljanjeRacunom.vratiteStanjeRacunaZaPrenos();
        Double stanjeRacunaKorisnikaPosle = upravljanjeRacunom.vratiteStanjeRacunaKorisnika(email);

        assertTrue(stanjeRacunaSajta+sajtuIde==stanjeRacunaSajtaPosle);
        assertTrue(stanjeRacunaZaPrenos-kolicinaZaPrebacivanjeKorisniku==stanjeRacunaZaPrenosPosle);
        assertTrue (stanjeRacunaKorisnika+korisnikuIde==stanjeRacunaKorisnikaPosle);

        kolicinaZaPrebacivanjeKorisniku = -kolicinaZaPrebacivanjeKorisniku;
        sajtuIde=-sajtuIde;
        korisnikuIde=-korisnikuIde;
        upravljanjeRacunom.prebaciteNaRacunKorisnika(email, kolicinaZaPrebacivanjeKorisniku);
        stanjeRacunaSajtaPosle = upravljanjeRacunom.vratiteStanjeRacunaSajta();
        stanjeRacunaZaPrenosPosle= upravljanjeRacunom.vratiteStanjeRacunaZaPrenos();
        stanjeRacunaKorisnikaPosle = upravljanjeRacunom.vratiteStanjeRacunaKorisnika(email);
        assertEquals(stanjeRacunaSajta,stanjeRacunaSajtaPosle);
        assertEquals(stanjeRacunaZaPrenos,stanjeRacunaZaPrenosPosle);
        assertEquals(stanjeRacunaKorisnika,stanjeRacunaKorisnikaPosle);



    }
}