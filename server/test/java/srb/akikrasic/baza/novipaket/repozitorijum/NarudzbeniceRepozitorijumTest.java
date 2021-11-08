package srb.akikrasic.baza.novipaket.repozitorijum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.baza.novipaket.baza.BazaKljucneReci;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.domen.Narudzbenica;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.slike.Slike;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aki on 12/5/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class NarudzbeniceRepozitorijumTest extends ZajednickiPodaciZaTest{

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @Autowired
    private NarudzbeniceRepozitorijum narudzbeniceRepozitorijum;

    @Autowired
    private NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;

    @Autowired
    private  ProizvodiRepozitorijum proizvodiRepozitorijum;

    @Test
    public void narudzbeniceJedanVelikiTest() throws Exception {
     /*   Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/
        LocalDateTime datum = LocalDateTime.now();
        Narudzbenica n = new Narudzbenica();

        Korisnik kupac = korisniciRepozitorijum.pretragaKorisnikaPoEmailu("akikrasicDRUGIKORISNIK@gmail.com");
        Korisnik prodavac = korisniciRepozitorijum.pretragaKorisnikaPoEmailu(email);
        n.setKupac(kupac);
        n.setDatum(datum);
        n.setDostava("Курирска служба");
        n.setChargeId("ch_asasdsaddasd");
        n.setZbir(123);


        Proizvod p = proizvodiRepozitorijum.vratiteProizvodPoId(36);
        NaruceniProizvod np = new NaruceniProizvod();
        np.setProizvod(p);
        np.setNarudzbenica(n);
        np.setOtkazan(false);
        np.setKupacPotvrdio(false);
        np.setProdavacPotvrdio(false);
        np.setKolicina(10);
        np.setCena(30);
        np.setCenaPutaKolicina(300);
        n.getNaruceniProizvodi().add(np);

        narudzbeniceRepozitorijum.snimiteNarudzbenicu(n);
        assertTrue(n.getId()>0);
        Narudzbenica n1 = narudzbeniceRepozitorijum.vratiteNarudzbenicuPoId(n.getId());
        assertTrue(n.getId()==n1.getId());
        assertEquals(n.getChargeId(), n1.getChargeId());
        assertEquals(n.getDostava(), n1.getDostava());
        assertEquals(n.getDatum(), n1.getDatum());
        assertEquals(n.getKupac().getEmail(), n1.getKupac().getEmail());
        assertTrue(n.getZbir()==n1.getZbir());


        List<NaruceniProizvod> npLista= naruceniProizvodiRepozitorijum.vratiteNaruceneProizvodeZaNarudzbenicu(n1);
        NaruceniProizvod np1 = npLista.get(0);
        assertTrue(np.getProizvod().getId()==np1.getProizvod().getId());
        assertTrue(np.getNarudzbenica().getId()==np1.getNarudzbenica().getId());
        assertEquals(np.isOtkazan(), np1.isOtkazan());
        assertEquals(np.isKupacPotvrdio(), np1.isOtkazan());
        assertEquals(np.isProdavacPotvrdio(), np1.isProdavacPotvrdio());
        assertTrue(np.getKolicina()==np1.getKolicina());
        assertTrue(np.getCena()==np1.getCena());
        assertTrue(np.getCenaPutaKolicina()==np1.getCenaPutaKolicina());

        NaruceniProizvod np2 = n.getNaruceniProizvodi().get(0);

        assertTrue(np1.getProizvod().getId()==np2.getProizvod().getId());
        assertTrue(np1.getNarudzbenica().getId()==np2.getNarudzbenica().getId());
        assertEquals(np1.isOtkazan(), np2.isOtkazan());
        assertEquals(np1.isKupacPotvrdio(), np2.isOtkazan());
        assertEquals(np1.isProdavacPotvrdio(), np2.isProdavacPotvrdio());
        assertTrue(np1.getKolicina()==np2.getKolicina());
        assertTrue(np1.getCena()==np2.getCena());
        assertTrue(np1.getCenaPutaKolicina()==np2.getCenaPutaKolicina());

        List<Narudzbenica> narudzbeniceKupca = narudzbeniceRepozitorijum.vratiteNarudzbeniceKupcu(kupac.getEmail());
        boolean nadjenaNarudzbenica= false;
        for(Narudzbenica nar:narudzbeniceKupca){
            if(nar.getId()==n.getId()){
                nadjenaNarudzbenica=true;
                break;
            }
        }//mozda je malo bez veze nacin pretrage ali bice jos puno narudzbenica za tog korisnika
        assertTrue(nadjenaNarudzbenica);
        List<NaruceniProizvod> naruceniProizvodiProdavca = naruceniProizvodiRepozitorijum.vratiteNaruceneProizvodeProdavca(prodavac.getEmail());
        boolean nadjenNaruceniproizvod=false;
        for(NaruceniProizvod nprv:naruceniProizvodiProdavca){
            if(nprv.getNarudzbenica().getId()==n.getId()){
                nadjenNaruceniproizvod=true;
                break;
            }
        }
        assertTrue(nadjenNaruceniproizvod);
        naruceniProizvodiRepozitorijum.prodavacPotvrdjujeNaruceniProizvod(np1);
        naruceniProizvodiRepozitorijum.kupacPotvrdjujeNaruceniProizvod(np1);
        naruceniProizvodiRepozitorijum.otkazanNaruceniProizvod(np1);
        npLista= naruceniProizvodiRepozitorijum.vratiteNaruceneProizvodeZaNarudzbenicu(n);
        np1 = npLista.get(0);
        assertTrue(np1.isOtkazan());
        assertTrue(np1.isKupacPotvrdio());
        assertTrue(np1.isProdavacPotvrdio());
        narudzbeniceRepozitorijum.obrisiteNarudzbenicu(n);

    }


    @Test
    public void vratiteNarudzbeniceKupcuLimit() throws Exception {
     /*   Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/
        int pocetak=0;
        int kraj=10;
        List<Narudzbenica> l = narudzbeniceRepozitorijum.vratiteNarudzbeniceKupcuLimit(email,pocetak, kraj );

        assertTrue(l.size()<=kraj-pocetak);
        assertTrue(l.size()>0);
    }

}