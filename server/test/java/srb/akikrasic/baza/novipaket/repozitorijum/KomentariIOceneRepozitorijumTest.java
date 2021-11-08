package srb.akikrasic.baza.novipaket.repozitorijum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.domen.KomentarIOcena;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.slike.Slike;

import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aki on 12/2/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class KomentariIOceneRepozitorijumTest extends ZajednickiPodaciZaTest {

    @Autowired
    private KomentariIOceneRepozitorijum komentariIOceneRepozitorijum;

    @Autowired
    private  ProizvodiRepozitorijum proizvodiRepozitorijum;

    @Test
    public void komentarNoviApi() throws Exception {
      /*  Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
        */
        String tekstKomentara = "Ово је коментар за испробавање и тестирање";
        assertTrue(komentariIOceneRepozitorijum.snimiteKomentarNovaMetoda(tekstKomentara, email, proizvodId));
        KomentarIOcena ko = komentariIOceneRepozitorijum.vratiteKomentarIOcenuZaKorisnikaIProizvodNovi(email, proizvodId);
        assertEquals(ko.getTekst(), tekstKomentara);
        assertNotNull(ko.getDatum());
        String tekstKomentaraNovi="Ово је текст коментара нови да се испроба измена коментара.";
        komentariIOceneRepozitorijum.snimiteKomentarNovaMetoda(tekstKomentaraNovi, email, proizvodId);
        ko = komentariIOceneRepozitorijum.vratiteKomentarIOcenuZaKorisnikaIProizvodNovi(email, proizvodId);
        assertEquals(ko.getTekst(), tekstKomentaraNovi);
        assertNotNull(ko.getDatum());
        Proizvod p = proizvodiRepozitorijum.vratiteProizvodPoId(proizvodId);
        double prosecnaOcena = p.getProsecnaOcena();
        Random r = new Random(47);

        int ocena =10;
        komentariIOceneRepozitorijum.snimiteOcenuNovaMetoda(ocena, email, proizvodId);
        ko = komentariIOceneRepozitorijum.vratiteKomentarIOcenuZaKorisnikaIProizvodNovi(email, proizvodId);
        assertTrue(ocena==ko.getOcena());
        p = proizvodiRepozitorijum.vratiteProizvodPoId(proizvodId);
        assertNotNull(p.getProsecnaOcena());
        //25.11.2017 zakomentarisano zbog potrebe testa inace ne treba da bude
        //assertTrue(!b.vratiteSveKomentareIOceneZaProizvodNovi(proizvodId).isEmpty());
    }
}