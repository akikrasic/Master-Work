package srb.akikrasic.zahtevi.stari.registrovanikorisnik;

import org.junit.Before;
import org.junit.Test;
import srb.akikrasic.baza.Baza;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by aki on 8/23/17.
 */
public class RegistrovaniKorisnikPrijemZahtevaTest {
    private RegistrovaniKorisnikPrijemZahteva rkp;
    @Before
    public void setUp() throws Exception {
        rkp = new RegistrovaniKorisnikPrijemZahteva();
        rkp.setB(new Baza());
    }



    @Test
    public void proveraNazivaProizvoda() throws Exception {
        assertTrue(rkp.sigurnosnProveraNazivaProizvoda("Nije prazan string ni null"));
    }
    @Test
    public void proveraNazivaProizvodaNull() throws Exception {
        assertFalse(rkp.sigurnosnProveraNazivaProizvoda(null));
    }
    @Test
    public void proveraNazivaProizvodaPrazan() throws Exception {
        assertFalse(rkp.sigurnosnProveraNazivaProizvoda(""));
    }

    @Test
    public void proveraOpisaProizvoda() throws Exception {
        assertTrue(rkp.sigurnosnProveraOpisaProizvoda("opis proizvoda koji nije null ili prazan string"));
    }
    @Test
    public void proveraOpisaProizvodaNull() throws Exception {
        assertFalse(rkp.sigurnosnProveraOpisaProizvoda(null ));
    }
    @Test
    public void proveraOpisaProizvodaPrazan() throws Exception {
        assertFalse(rkp.sigurnosnProveraOpisaProizvoda(""));
    }

    @Test
    public void proveraKategorije() throws Exception {
        assertTrue(rkp.sigurnosnaProveraKategorije("1"));
    }

    @Test
    public void proveraKategorijeNull() throws Exception {
        assertFalse(rkp.sigurnosnaProveraKategorije(null));
    }
    @Test
    public void proveraKategorijePraqzan() throws Exception {
        assertFalse(rkp.sigurnosnaProveraKategorije(""));
    }

    @Test
    public void konverzijaKategorije() throws Exception {
        assertEquals(((Integer)1), rkp.konverzijaKategorije("1"));
    }

    @Test
    public void konverzijaKategorijeNull() throws Exception {
        assertNull(rkp.konverzijaKategorije("dasdsadasdadas"));
    }

    @Test
    public void proveraCene() throws Exception {
        assertTrue(rkp.sigurnosnaProveraCene("123.45"));
    }
    @Test
    public void proveraCeneNull() throws Exception {
        assertFalse(rkp.sigurnosnaProveraCene(null));
    }
    @Test
    public void proveraCenePrazan() throws Exception {
        assertFalse(rkp.sigurnosnaProveraCene(""));
    }

    @Test
    public void konverzijaCene() throws Exception {
        assertEquals(((Double)123.45), rkp.konverzijaCene("123.45"));
    }
    @Test
    public void konverzijaCeneGreskVracaNull() throws Exception {
        assertNull( rkp.konverzijaCene("Tekstualni format unet za cenu"));
    }

    @Test
    public void proveraListeKljucnihReci() throws Exception {
        List lista = new ArrayList<>();
        lista.add("Mika");
        lista.add("Pera");
        assertTrue(rkp.sigurnosnaProveraListeKljucnihReci(lista));
    }
    @Test
    public void proveraListeKljucnihReciPraznaLista() throws Exception {
        List lista = new ArrayList<>();

        assertFalse(rkp.sigurnosnaProveraListeKljucnihReci(lista));
    }
    @Test
    public void proveraListeKljucnihReciNull() throws Exception {
        List lista = new ArrayList<>();

        assertFalse(rkp.sigurnosnaProveraListeKljucnihReci(null));
    }
    @Test
    public void proveraListeKljucnihReciNullClanovi() throws Exception {
        List lista = new ArrayList<>();
        lista.add(null);
        lista.add("Mika");
        assertFalse(rkp.sigurnosnaProveraListeKljucnihReci(lista));
    }
    public void proveraListeKljucnihReciPrazniClanovi() throws Exception {
        List lista = new ArrayList<>();
        lista.add("Mika");
        lista.add("");
        assertFalse(rkp.sigurnosnaProveraListeKljucnihReci(lista));
    }
}