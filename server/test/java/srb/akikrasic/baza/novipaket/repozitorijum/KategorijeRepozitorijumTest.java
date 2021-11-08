package srb.akikrasic.baza.novipaket.repozitorijum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.domen.Kategorija;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by aki on 11/30/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class KategorijeRepozitorijumTest extends ZajednickiPodaciZaTest {
    @Autowired
    private KategorijeRepozitorijum katR;

    @Test
    public void kategorijeJedanVelikiTest() throws Exception {

        String nazivNoveKategorije="Kategorija proba";
        String izmenjenNaziv ="Kategorija proba izmena";
        assertTrue(katR.dodajteKategoriju(nazivNoveKategorije));
        List<Kategorija> kategorije = katR.vratiteSveKategorije();
        assertFalse(kategorije.isEmpty());
        Kategorija k =katR.vratiteKategorijuPoNazivu(nazivNoveKategorije);
        assertEquals(nazivNoveKategorije, k.getNaziv());

        assertTrue(katR.izmenaKategorije(k.getId(), izmenjenNaziv));
        kategorije = katR.vratiteSveKategorije();
        assertFalse(kategorije.isEmpty());
        k = katR.vratiteKategorijuPoNazivu(izmenjenNaziv);
        assertEquals(izmenjenNaziv, k.getNaziv());

        assertTrue(katR.obrisiteKategoriju(k.getId()));
        assertFalse(katR.daLiSeNazivKategorijeNalaziUBazi(nazivNoveKategorije));
        assertFalse(katR.daLiSeNazivKategorijeNalaziUBazi(izmenjenNaziv));

    }


    @Test
    public void nazivKategorijeSeNalaziUBazi() throws Exception {
        assertTrue(katR.daLiSeNazivKategorijeNalaziUBazi("Воће"));
    }
    @Test
    public void nazivKategorijeSeNalaziUBaziGreska() throws Exception {
        assertFalse( katR.daLiSeNazivKategorijeNalaziUBazi("Voce"));
    }

    @Test
    public void daLiJeSortirano() throws Exception {
        List<Kategorija> l = katR.vratiteSveKategorije();
        for(int i=0;i<l.size()-1;i++){
            System.out.println(l.get(i).getNaziv());
            System.out.println(l.get(i+1).getNaziv());
            System.out.println(l.get(i).getNaziv().compareTo(l.get(i+1).getNaziv()));
            assertTrue(l.get(i).getNaziv().compareTo(l.get(i+1).getNaziv())<0);
        }
    }
}