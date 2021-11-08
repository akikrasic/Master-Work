package srb.akikrasic.baza.novipaket.baza;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by aki on 12/8/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class BazaProizvodiKojiNeSmejuDaSeMenjajuTest {

    @Autowired
    private BazaProizvodiKojiNeSmejuDaSeMenjaju b;

    @Test
    public void proizvodiKojiNeSmejuDaSeMenjaju() throws Exception {
        List<Integer> lista = b.proizvodiKojiNeSmejuDaSeMenjaju();

        assertFalse(lista.isEmpty());

    }
}