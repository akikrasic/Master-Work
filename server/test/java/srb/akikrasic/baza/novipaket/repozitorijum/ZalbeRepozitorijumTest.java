package srb.akikrasic.baza.novipaket.repozitorijum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.domen.*;
import srb.akikrasic.slike.Slike;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aki on 12/8/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class ZalbeRepozitorijumTest extends ZajednickiPodaciZaTest{

    @Autowired
    private ZalbeRepozitorijum zalbeRepozitorijum;

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @Autowired
    private NarudzbeniceRepozitorijum narudzbeniceRepozitorijum;

    @Autowired
    private NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;

    @Autowired
    private  ProizvodiRepozitorijum proizvodiRepozitorijum;


    @Test
    public void radSaZalbama() throws Exception{
      /*  Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
        */
        Narudzbenica n = new Narudzbenica();
        Korisnik k = korisniciRepozitorijum.pretragaKorisnikaPoEmailu(email);
        n.setKupac(k);
        n.setDatum(LocalDateTime.now());
        n.setZbir(0);
        n.setChargeId("");
        n.setDostava("");
        NaruceniProizvod np = new NaruceniProizvod();
        n.getNaruceniProizvodi().add(np);
        np.setNarudzbenica(n);
        String porukaKupac="asdjashdgsahd";
        String porukaProdavac="sdsaasdsadasd";
        Proizvod p = proizvodiRepozitorijum.vratiteProizvodPoId(proizvodId);
        np.setProizvod(p);
        np.setCenaPutaKolicina(0);
        np.setCena(0);
        narudzbeniceRepozitorijum.snimiteNarudzbenicu(n);
        n= narudzbeniceRepozitorijum.vratiteNarudzbenicuPoId(n.getId());
        np = n.getNaruceniProizvodi().get(0);
        assertTrue( zalbeRepozitorijum.korisnikSeZalio(np, porukaKupac));
        Zalba z = zalbeRepozitorijum.vratiteZalbuZaNaruceniProizvod(np);
        assertEquals(z.getTekstKupca(), porukaKupac);
        assertNotNull(z.getDatumKupca());
        assertNull(z.getDatumProdavca());
        assertNull(z.getTekstProdavca());

        z.setTekstProdavca(porukaProdavac);
        assertTrue( zalbeRepozitorijum.prodavacOdgovaraNaZalbu(z));

        z= zalbeRepozitorijum.vratiteZalbuZaNaruceniProizvod(np);

        assertEquals(z.getTekstKupca(), porukaKupac);
        assertEquals(z.getTekstProdavca(), porukaProdavac);
        assertNotNull(z.getDatumKupca());
        assertNotNull(z.getDatumProdavca());
        assertTrue(narudzbeniceRepozitorijum.obrisiteNarudzbenicu(n));
        assertTrue(zalbeRepozitorijum.obrisiteZalbu(z));

    }
}