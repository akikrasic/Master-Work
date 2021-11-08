package srb.akikrasic.baza.novipaket.baza;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * Created by aki on 12/8/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class BazaSlikeTest {

    @Autowired
    private BazaSlike b;

    @Test
    public void slikaVrednost() throws Exception {
        long vr = b.ucitajteVrednostSlikeNaPocetku();
        assertTrue(vr>=0L);
        long vr1 = vr+1;
        b.sacuvajteVrednostSlikeNaKraju(vr1);
        long vr2 = b.ucitajteVrednostSlikeNaPocetku();
        assertEquals(vr1, vr2);
        b.sacuvajteVrednostSlikeNaKraju(vr);//da se vrati na prethodnu vrednost
    }

}