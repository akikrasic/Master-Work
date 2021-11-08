package srb.akikrasic.token;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aki on 8/13/17.
 */
public class GenerisanjeTokenaTest {
    private GenerisanjeTokena gt;
    @Before
    public void inic(){
        gt = new GenerisanjeTokena();
    }
    @Test
    public void generisiteTokenZaNeregistrovanogKorisnika() throws Exception {
    }

    @Test
       public void enkodDekodUrlFormata() throws Exception {
            String s = "fdfdsdsfffdssdfgsg";
            assertTrue(s.equals(gt.dekodujteURLFormat(gt.enkodujteUURLFormat(s))));
    }

}