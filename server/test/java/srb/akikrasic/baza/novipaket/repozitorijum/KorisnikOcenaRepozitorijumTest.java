package srb.akikrasic.baza.novipaket.repozitorijum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.domen.KorisnikOcena;

import static org.junit.Assert.*;

/**
 * Created by aki on 2/23/18.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class KorisnikOcenaRepozitorijumTest extends ZajednickiPodaciZaTest {

    @Autowired
    private KorisnikOcenaRepozitorijum korisnikOcenaRepozitorijum;

    @Test
    public void daLiKupacMozeDaOcenjujeProdavca(){
        assertTrue(korisnikOcenaRepozitorijum.daLiKupacMozeDaOcenjujeProdavca(email, email2));
    }
    @Test
    public void daLiKupacMozeDaOcenjujeProdavcaNetacan(){
        assertFalse(korisnikOcenaRepozitorijum.daLiKupacMozeDaOcenjujeProdavca(email, neispravanEmail));

    }
    @Test
    public void velikiTest(){
        int ocena =10;
        int id= korisnikOcenaRepozitorijum.dodajteOcenuKorisnika(ocena, email, email2);
        assertTrue(id>0);
        KorisnikOcena ko = korisnikOcenaRepozitorijum.vratiteKorisnikuOcenu(id);
        System.out.println(ko.getOcena());
        assertTrue(ko.getOcena()==ocena);
        assertTrue(ko.getKupac().getEmail().equals(email));
        assertTrue(ko.getProdavac().getEmail().equals(email2));
        System.out.println(" id "+id);
        ocena = 7;
        assertTrue(korisnikOcenaRepozitorijum.izmeniteOcenuKorisnika(id, ocena));
        ko = korisnikOcenaRepozitorijum.vratiteKorisnikuOcenu(id);
        assertTrue(ko.getOcena()==ocena);

        assertTrue(korisnikOcenaRepozitorijum.obrisiteOcenuKorisnika(id));

    }
}