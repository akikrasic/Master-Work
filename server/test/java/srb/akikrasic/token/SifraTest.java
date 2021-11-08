package srb.akikrasic.token;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aki on 8/1/17.
 */
public class SifraTest {
    private Sifra sifra = new Sifra();
    @Test
    public void nadoveziteNiz() throws Exception {
        String zaTestiranje = "проба";
        String nadovezaniZaTestiranje = new StringBuilder().append(new String(sifra.getSaltPrednji())).append(zaTestiranje).append(new String(sifra.getSaltZadnji())).toString();

        String nadovezaniPrekoBajtova = new String(sifra.nadoveziteNiz(zaTestiranje.getBytes()));
        /*System.out.println(nadovezaniZaTestiranje);
        System.out.println(nadovezaniPrekoBajtova);*/
        assertTrue(nadovezaniZaTestiranje.equals(nadovezaniPrekoBajtova));
    }
    @Test
    public void proveriteSifruIspravna(){
        String sifraProvera  ="За проверу";
        assertTrue(sifra.proveriteSifru(sifraProvera.getBytes(),sifra.hesirajteSifru(sifraProvera.getBytes())));
    }
    @Test
    public void proveriteSifruNeispravna(){
        String sifraProvera  ="За проверу";
        assertFalse(sifra.proveriteSifru("Нека друга шифра".getBytes(),sifra.hesirajteSifru(sifraProvera.getBytes())));

    }
}