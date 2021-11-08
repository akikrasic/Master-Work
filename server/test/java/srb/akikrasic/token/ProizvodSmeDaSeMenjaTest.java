package srb.akikrasic.token;

import org.junit.Before;
import org.junit.Test;
import srb.akikrasic.baza.Baza;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by aki on 9/13/17.
 */
public class ProizvodSmeDaSeMenjaTest {
    private ProizvodSmeDaSeMenja proizvodSmeDaSeMenja;

    @Before
    public void setUp() throws Exception {
        proizvodSmeDaSeMenja= new ProizvodSmeDaSeMenja();
    }

    @Test
    public void smeDaSeMenja() throws Exception {
        List<Integer> lista  = new ArrayList<>();
        lista.add(1);
        lista.add(2);
        lista.add(3);
        proizvodSmeDaSeMenja.inicijalizacija(lista);
        assertTrue(proizvodSmeDaSeMenja.smeDaSeMenja(4));
        assertTrue(proizvodSmeDaSeMenja.smeDaSeMenja(5));
        assertTrue(proizvodSmeDaSeMenja.smeDaSeMenja(6));
        assertFalse(proizvodSmeDaSeMenja.smeDaSeMenja(1));
        assertFalse(proizvodSmeDaSeMenja.smeDaSeMenja(2));
        assertFalse(proizvodSmeDaSeMenja.smeDaSeMenja(3));

    }

    @Test
    public void zajednosaBazom() throws Exception {
        Baza b = new Baza();
        b.setProizvodSmeDaSeMenja(proizvodSmeDaSeMenja);
        b.inic();
        assertTrue(proizvodSmeDaSeMenja.smeDaSeMenja(1));
        assertFalse(proizvodSmeDaSeMenja.smeDaSeMenja(36));//za taj 36 sam napravio test ako posle budu problemi ce se resi
    }
}