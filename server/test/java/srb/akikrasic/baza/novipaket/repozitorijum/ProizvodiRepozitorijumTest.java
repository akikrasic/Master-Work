package srb.akikrasic.baza.novipaket.repozitorijum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import srb.akikrasic.baza.novipaket.baza.BazaKljucneReci;
import srb.akikrasic.domen.KljucnaRec;
import srb.akikrasic.domen.Proizvod;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by aki on 12/2/17.
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@WebAppConfiguration
public class ProizvodiRepozitorijumTest extends ZajednickiPodaciZaTest {


    @Autowired
    private  ProizvodiRepozitorijum proizvodiRepozitorijum;

    @Autowired
    private BazaKljucneReci bazaKljucneReci;

    @Autowired
    private KategorijeRepozitorijum kategorijeRepozitorijum;

    @Test
    public void velikiTestUnosIzmenaBrisanjeProizvoda() throws Exception {
    /*
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna
        System.out.println(slike.vratiteSveSlikeProizvoda(1));
*/
        String email = this.email;
        String naziv = "Парадајз јабучар";
        String opis = "Стара српска аутохтона сорта, погодна за органски начин производње.";
        double cena=100;
        int kategorijaId=2;//to je povrce


        ArrayList<String> kljucneReci= new ArrayList<>();
        String[] nizKljucnihReci= {"парадајз", "органски", "јабучар", "стара сорта", "српски"};
        Arrays.stream(nizKljucnihReci).forEach(k->{
            kljucneReci.add(k);
        });


        int id = proizvodiRepozitorijum.dodajteNoviProizvod(email, naziv, opis, cena, kategorijaId);
        assertTrue(id>0);
        Proizvod p = proizvodiRepozitorijum.vratiteProizvodPoId(id);
        assertEquals(p.getId(), id);
        assertEquals(p.getNaziv(), naziv);
        assertEquals(p.getOpis(), opis);
        assertEquals(p.getTrenutnaCena(), cena, 0);
        assertEquals(p.getKategorija().getId(), kategorijaId);
        assertEquals(p.getKorisnik().getEmail(), email);

        bazaKljucneReci.unesiteKljucneReci(kljucneReci, p.getId());
        List<KljucnaRec> kljucneReciIzBaze= bazaKljucneReci.vratiteSveKljucnereciZaProizvod(p);
        Map<String, KljucnaRec> mapa = new LinkedHashMap<>();
        kljucneReciIzBaze.forEach(k->{
            mapa.put(k.getNaziv(), k);
        });
        kljucneReci.forEach(s->{
            assertNotNull(mapa.get(s));
        });

        String nazivIzmena = "Парадајз јабучар нови назив";
        String opisIzmena = "Стара српска аутохтона сорта, погодна за органски начин производње.Промењен опис.";
        double cenaIzmena=1000;
        int kategorijaIdIzmena=1;//voce
        p.setNaziv(nazivIzmena);
        p.setOpis(opisIzmena);
        p.setTrenutnaCena(cenaIzmena);
        p.setKategorija(kategorijeRepozitorijum.vratiteKategorijuPoId(kategorijaIdIzmena));


        assertTrue(proizvodiRepozitorijum.izmeniteProizvodPodaci(p));
        assertEquals(p.getId(), id);
        assertEquals(p.getNaziv(), nazivIzmena);
        assertEquals(p.getOpis(), opisIzmena);
        assertEquals(p.getTrenutnaCena(), cenaIzmena, 0);
        assertEquals(p.getKategorija().getId(), kategorijaIdIzmena);
        assertEquals(p.getKorisnik().getEmail(), email);

        cenaIzmena=10000.123;
        p.setTrenutnaCena(cenaIzmena);
        assertTrue( proizvodiRepozitorijum.izmeniteProizvodCenu(p));
        assertEquals(cenaIzmena, p.getTrenutnaCena(), 0);
        //izmena kljucnih reci
        String[] nizKljucnihReciIzmena= { "негмо", "јабучар", "стара сорта", "српски", "прави парадајз"};
        ArrayList<String> kljucneReciIzmena= new ArrayList<>();
        Arrays.stream(nizKljucnihReciIzmena).forEach(k->{
            kljucneReciIzmena.add(k);
        });

        bazaKljucneReci.izmeniteKljucneReci(kljucneReciIzmena, p.getId());
        kljucneReciIzBaze= bazaKljucneReci.vratiteSveKljucnereciZaProizvod(p);
        Map<String, KljucnaRec> mapaIzmena = new LinkedHashMap<>();
        kljucneReciIzBaze.forEach(k->{
            mapaIzmena.put(k.getNaziv(), k);
        });
        kljucneReciIzmena.forEach(s->{
            assertNotNull(mapaIzmena.get(s));
        });
        assertEquals(kljucneReciIzBaze.size(),5);


        bazaKljucneReci.obrisiteKljucneReci(mapaIzmena.values(), p.getId());
        assertTrue(proizvodiRepozitorijum.obrisiteProizvod(p.getId()));
    }

    @Test
    public void pretragaProizvodaNista() throws Exception {
        List<String> kljucneReci = new ArrayList<>();
        List<Proizvod> l =proizvodiRepozitorijum.pretragaProizvoda("", "","",-1, -1,kljucneReci,"","");
        assertTrue(l.isEmpty());
        assertEquals(0, l.size());
    }

    @Test
    public void pretragaImeNazivUneto() throws Exception {
        List<String> kljucneReci = new ArrayList<>();

/*
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna

*/

        List<Proizvod> l =proizvodiRepozitorijum.pretragaProizvoda("а","","",-1, -1,kljucneReci,"","");
        assertFalse(l.isEmpty());

        //assertTrue(r.getUkBrojProizvoda()>=l .size());
    }
    @Test
    public void pretragaEmailUnet() throws Exception {
        List<String> kljucneReci = new ArrayList<>();

/*
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/


        List<Proizvod> l =proizvodiRepozitorijum.pretragaProizvoda("","a","",-1, -1,kljucneReci,"","");
        assertFalse(l .isEmpty());
        //assertTrue(r.getUkBrojProizvoda()>=l .size());
    }
    @Test
    public void pretragaNazivProizvoda() throws Exception {
        List<String> kljucneReci = new ArrayList<>();

/*
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/


        List<Proizvod> l =proizvodiRepozitorijum.pretragaProizvoda("","","а",-1, -1,kljucneReci,"","");
        assertFalse(l .isEmpty());
      //  assertTrue(r.getUkBrojProizvoda()>=l .size());
    }
    @Test
    public void pretragaCenaOd() throws Exception {
        List<String> kljucneReci = new ArrayList<>();

/*
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/


        List<Proizvod> l =proizvodiRepozitorijum.pretragaProizvoda("","","",100, -1,kljucneReci,"","");
        assertFalse(l .isEmpty());
      //  assertTrue(r.getUkBrojProizvoda()>=l .size());
    }
    @Test
    public void pretragaCenaDo() throws Exception {
        List<String> kljucneReci = new ArrayList<>();

/*
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/


        List<Proizvod> l =proizvodiRepozitorijum.pretragaProizvoda("а","","",-1, 10000,kljucneReci,"","");
        assertFalse(l .isEmpty());

       // assertTrue(r.getUkBrojProizvoda()>=l .size());
    }
    @Test
    public void pretragaKljucneReci() throws Exception {
        List<String> kljucneReci = new ArrayList<>();
        kljucneReci.add("парадајз");
/*
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/


        List<Proizvod> l =proizvodiRepozitorijum.pretragaProizvoda("","","",-1, -1,kljucneReci,"","");
        assertFalse(l .isEmpty());
       // assertTrue(r.getUkBrojProizvoda()>=l .size());
    }
    @Test
    public void pretragaKategorijaNaziv() throws Exception {
        List<String> kljucneReci = new ArrayList<>();

/*
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna

*/

        List<Proizvod> l =proizvodiRepozitorijum.pretragaProizvoda("","","",-1, -1,kljucneReci,"Воћ","");
        assertFalse(l .isEmpty());
      //  assertTrue(r.getUkBrojProizvoda()>=l .size());
    }
    @Test
    public void pretragaOpis() throws Exception {
        List<String> kljucneReci = new ArrayList<>();

/*
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/


        List<Proizvod> l =proizvodiRepozitorijum.pretragaProizvoda("","","",-1, -1,kljucneReci,"","а");
        assertFalse(l .isEmpty());
       // assertTrue(r.getUkBrojProizvoda()>=l .size());
    }
    @Test
    public void pretragaviseParametra() throws Exception {
        List<String> kljucneReci = new ArrayList<>();
        kljucneReci.add("а");
/*
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/


        List<Proizvod> l =proizvodiRepozitorijum.pretragaProizvoda("а","a","г",100, 10000,kljucneReci,"Воћ","а");
        assertFalse(l .isEmpty());
      //  assertTrue(r.getUkBrojProizvoda()>=l .size());
    }




    @Test
    public void vratiteSveProizvode() throws Exception {
     /*   Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/
        System.out.println(proizvodiRepozitorijum.vratiteSveProizvode());
        assertTrue(proizvodiRepozitorijum.vratiteSveProizvode().size()>0);
    }

    @Test
    public void vratiteSveProizvodeKorisnika() throws Exception {
      /*  Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/
        List<Proizvod> proizvodiKorisnika = proizvodiRepozitorijum.vratiteSveProizvodeKorisnika(email);
        proizvodiKorisnika.forEach(p->{
            assertTrue(p.getKorisnik().getEmail().equals(email));
        });
    }

    @Test
    public void vratiteProizvodZaPrikazKorisnikuSaKomentarimaIOcenama() throws Exception {
     /*   Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/


        Proizvod p = proizvodiRepozitorijum.vratiteProizvodZaPrikazKorisnikuSaKomentarimaIOcenama(proizvodId);
        assertTrue(p.getId()==proizvodId);
        assertNotNull(p.getNaziv());
       // assertFalse(p.getKomentari().isEmpty()); stari api
       // assertFalse(p.getOcene().isEmpty()); stari api

        assertTrue(p.getProsecnaOcena()>0);
    }

    @Test
    public void vratiteStoPRoizvoda() throws Exception {
  /*      Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/

        List<Proizvod> proizvodi = proizvodiRepozitorijum.vratiteStoProizvoda();
        assertFalse(proizvodi.isEmpty());
        assertTrue(proizvodi.size()<=100);
        assertTrue(proizvodi.size()>0);
    }

    @Test
    public void vratiteSveProizvodeNekogKorisnikaPoEmailuDaIhOnMenja() throws Exception {
    /*    Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna
*/

        List<Proizvod> proizvodi = proizvodiRepozitorijum.vratiteSveProizvodeNekogKorisnikaPoEmailuDaIhOnMenja(email);
        proizvodi.forEach(p->{
            assertTrue(p.getKorisnik().getEmail().equals(email));
        });
    }
}