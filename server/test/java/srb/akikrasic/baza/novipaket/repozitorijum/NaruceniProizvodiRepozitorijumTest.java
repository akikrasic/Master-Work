package srb.akikrasic.baza.novipaket.repozitorijum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.baza.novipaket.baza.BazaNaruceniProizvod;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.domen.Narudzbenica;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by aki on 12/5/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class NaruceniProizvodiRepozitorijumTest extends ZajednickiPodaciZaTest {

    @Autowired
    private NarudzbeniceRepozitorijum narudzbeniceRepozitorijum;

    @Autowired
    private NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;


    @Autowired
    private BazaNaruceniProizvod bazaNaruceniProizvod;



    @Test
    public void napraviteNaruceniproizvodSaPodacimaIzBaze() throws Exception {
      /*  Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/
        Map<String, Object> mapa = new LinkedHashMap<>();
        int id=1;
        double kolicina = 100.5;
        double cena = 123.56;
        double cenaPutaKolicina = cena* kolicina;
        boolean prodavacPotvrdio = true;
        boolean kupacPotvrdio = true;
        boolean otkazan = true;
        mapa.put("id",id);
        mapa.put("kolicina",kolicina);
        mapa.put("cena",cena);
        mapa.put("cenaputakolicina", cenaPutaKolicina);
        mapa.put("prodavacpotvrdio",prodavacPotvrdio);
        mapa.put("kupacpotvrdio", kupacPotvrdio);
        mapa.put("otkazan", otkazan);
        mapa.put("proizvod_id", proizvodId);
        Narudzbenica n = new Narudzbenica();
        n.setId(id);
        NaruceniProizvod np = naruceniProizvodiRepozitorijum.napraviteNaruceniproizvodSaPodacimaIzBaze(mapa, n);

        assertTrue(np.getId()==id);
        assertTrue(np.getKolicina()==kolicina);
        assertTrue(np.getCena()==cena);
        assertTrue(np.getCenaPutaKolicina()== cenaPutaKolicina);
        assertTrue(np.getCenaPutaKolicina()==cena*kolicina);
        assertTrue(np.isProdavacPotvrdio()== prodavacPotvrdio);
        assertTrue(np.isKupacPotvrdio()==kupacPotvrdio);
        assertTrue(np.isOtkazan()==otkazan);
        assertTrue(np.getProizvod().getId()==proizvodId);
        assertTrue(np.getNarudzbenica().getId()==id);

    }

    @Test
    public void vratiteNaruceniProizvodPoId() throws Exception {
    /*    Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/
        List<Narudzbenica> l = narudzbeniceRepozitorijum.vratiteNarudzbeniceKupcu(email);
        NaruceniProizvod np = l.get(0).getNaruceniProizvodi().get(0);
        NaruceniProizvod np1 = naruceniProizvodiRepozitorijum.vratiteNaruceniProizvodPoId(np.getId());
        assertTrue(np.getId()==np1.getId());
        assertTrue(np.getProizvod().getId()==np1.getProizvod().getId());

    }

    @Test
    public void vratiteNarudzbenicuPoIdBezNarucenihProizvoda() throws Exception {
     /*   Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/
        List<Narudzbenica> l = narudzbeniceRepozitorijum.vratiteNarudzbeniceKupcu(email);
        Narudzbenica n = l.get(0);
        Narudzbenica n1 = narudzbeniceRepozitorijum.vratiteNarudzbenicuPoIdBezNarucenihProizvoda(n.getId());
        assertTrue(n1.getNaruceniProizvodi().isEmpty());
        assertTrue(n.getId()==n1.getId());

    }

    @Test
    public void vratiteNaruceneProizvodeZaNarudzbenicuBezPovratneReference() throws Exception {
     /*   Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/
        List<Narudzbenica> l = narudzbeniceRepozitorijum.vratiteNarudzbeniceKupcu(email);
        for(Narudzbenica n : l){
            List<NaruceniProizvod> lnp = naruceniProizvodiRepozitorijum.vratiteNaruceneProizvodeZaNarudzbenicuBezPovratneReference(n);
            lnp.forEach(np->{
                assertTrue(np.getNarudzbenica()==null);
            });
        }
    }


    @Test
    public void vratiteNaruceneProizvodeProdavcaLimit() throws Exception {
/*        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/
        int pocetak=0;
        int kraj=10;

        List<NaruceniProizvod> l = naruceniProizvodiRepozitorijum.vratiteNaruceneProizvodeProdavcaLimit(email, pocetak, kraj);
        assertTrue(l.size()<=kraj-pocetak);
        assertTrue(l.size()>0);
    }

    @Test
    public void izvestajZaAdmina() throws Exception {
/*
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/
        int limit=10;
        int offset = 0;
        List<NaruceniProizvod> lista = naruceniProizvodiRepozitorijum.izvestajZaAdmina(limit, offset);
        assertTrue(lista.size()==limit);
        for(int i=0;i<lista.size();i++){
            NaruceniProizvod np = lista.get(i);
            assertNotNull(np);
            assertNotNull(np.getProizvod());
            assertNotNull(np.getNarudzbenica());
            assertNotNull(np.getNarudzbenica().getKupac());
            assertNotNull(np.getProizvod().getKorisnik());
        }
    }

}