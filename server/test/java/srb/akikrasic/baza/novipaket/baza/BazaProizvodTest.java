package srb.akikrasic.baza.novipaket.baza;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.domen.Kategorija;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.slike.Slike;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by aki on 12/8/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class BazaProizvodTest {

    @Autowired
    private BazaProizvod b;

    @Test
    public void napraviteProizvodSaPodacimaIzBaze() throws Exception {
        Map<String, Object > mapa = new LinkedHashMap<>();
        Baza b = spy(Baza.class);

        Korisnik k = new Korisnik();
        Kategorija kat = new Kategorija();
        kat.setNaziv("zmaj");
        k.setEmail("akikrasic@gmail.com");
        when(b.pretragaKorisnikaPoEmailu("akikrasic@gmail.com")).thenReturn(k);
        when(b.vratiteKategorijuPoId(1)).thenReturn(kat);
        String naziv = "Naziv proizvoda";
        String opis = "Opis proizvoda, ovo je opis proizvoda sdhlsfhkslkdjfhsjdkfhsldkjfhsdjkfhdskjfhsdkjf";
        int id = 1;
        double cena = 123.45;
        mapa.put("id",id );
        mapa.put("naziv",naziv);
        mapa.put("opis", opis);
        mapa.put("trenutnacena", cena);
        mapa.put("kategorija_id", 1);
        mapa.put("aktivan", true);
        mapa.put("korisnik_email", "akikrasic@gmail.com");
        mapa.put("prosecna_ocena", 0D);
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna


        Proizvod p = b.napraviteProizvodSaPodacimaIzBaze(mapa);
        assertEquals(p.getId(), id);
        assertEquals(p.getNaziv(), naziv);
        assertEquals(p.getOpis(), opis);
        assertTrue((double)p.getTrenutnaCena()== (double)cena);
        assertEquals(kat, p.getKategorija());
        assertEquals(k, p.getKorisnik());
    }



    @Test
    public void like() throws Exception {
        String proba = "Ovo je string za probu";

        assertEquals(new StringBuilder("%").append(proba).append("%").toString(), b.like(proba));
    }

    @Test
    public void likeMalaVelikaSlova() throws Exception {
        String proba = "VELIKA SLOVA";
        assertEquals(new StringBuilder("%").append("velika slova").append("%").toString(), b.like(proba.toLowerCase()));
    }
    @Test
    public void likeMalaVelikaSlovaЋирилица() throws Exception {
        String proba = "ВЕЛИКА СЛОВА";
        assertEquals(new StringBuilder("%").append("велика слова").append("%").toString(), b.like(proba.toLowerCase()));
    }

}