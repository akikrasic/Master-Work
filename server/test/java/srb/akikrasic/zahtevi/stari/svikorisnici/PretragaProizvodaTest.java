package srb.akikrasic.zahtevi.stari.svikorisnici;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by aki on 9/7/17.
 */
public class PretragaProizvodaTest {
    private PretragaProizvoda pp;
    @Before
    public void inic(){
        pp = new PretragaProizvoda();
    }
    @Test
    public void razdvajanjeKljucnihReci() throws Exception {
        String kljucneReciString = "Mika Pera Laza Sofronije";
        List<String > listaZaProveru = new ArrayList<>();
        listaZaProveru.add("Mika");
        listaZaProveru.add("Pera");
        listaZaProveru.add("Laza");
        listaZaProveru.add("Sofronije");
        List<String> listaOdTamo = pp.razdvajanjeKljucnihReci(kljucneReciString);

        for(int i=0;i<listaZaProveru.size();i++){
            assertEquals(listaZaProveru.get(i), listaOdTamo.get(i));
        }

    }
    @Test
    public void razdvajanjeKljucnihReciGranicniSlucajevi() throws Exception {
        String kljucneReciString = "             Mika       Sofronije         ";
        List<String > listaZaProveru = new ArrayList<>();
        listaZaProveru.add("Mika");
        listaZaProveru.add("Sofronije");
        List<String> listaOdTamo = pp.razdvajanjeKljucnihReci(kljucneReciString);

        for(int i=0;i<listaZaProveru.size();i++){
            assertEquals(listaZaProveru.get(i), listaOdTamo.get(i));
        }
    }

    @Test
    public void doubleTest() throws Exception {
        String pocetak = "123.45";
        assertEquals(123.45, pp.konverzijaDoubleVrednosti(pocetak), 0);
        String drugaVr="";
        assertEquals(-1, pp.konverzijaDoubleVrednosti(drugaVr),0);
        String greska = "fdfhdhfd";
        assertNull(pp.konverzijaDoubleVrednosti(greska));
    }
    @Test
    public void integerTest() throws Exception {
        String pocetak = "123";
        assertEquals(123, pp.konverzijaIntegerVrednosti(pocetak),0);
        String drugaVr="";
        assertEquals(-1, pp.konverzijaIntegerVrednosti(drugaVr),0);
        String greska = "fdfhdhfd";
        assertNull(pp.konverzijaIntegerVrednosti(greska));
    }
}