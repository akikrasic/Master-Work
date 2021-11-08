package srb.akikrasic.baza.novipaket;

import org.junit.Before;
import org.junit.Test;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.domen.*;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Sifra;
import srb.akikrasic.token.Tokeni;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by aki on 7/29/17.
 */
public class BazaTestZaRasturanje {
    private Baza b ;
    private String email = "akikrasic@gmail.com";
    private String email2="akikrasicDRUGIKORISNIK@gmail.com";
    private String neispravanEmail = "email@koji je sigurno neispravan";
    private byte[] sifra = "aA1!aa".getBytes();
    private int proizvodId=36;
    @Before
    public void inic(){
        b = new Baza();
        b.setRadSaSifrom(new Sifra());
        b.setRadSaGenerisanjemTokena(new GenerisanjeTokena());
        b.setRadSaTokenima(new Tokeni());
        b.getRadSaTokenima().setRadSaGenerisanjemTokena(b.getRadSaGenerisanjemTokena());

    }








    @Test
    public void komentariBrisanjeiDodavanje() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        Proizvod p = b.vratiteProizvodPoId(36);
        Korisnik k = b.pretragaKorisnikaPoEmailu("akikrasic@gmail.com");
        Komentar kom = new Komentar();
        kom.setTekst("Tekst proba da vidimo da li radi");
        kom.setDatum(LocalDateTime.now());
        kom.setProizvod(p);
        kom.setKorisnik(k);
        b.snimiteKomentar(kom.getTekst(), kom.getDatum(), kom.getKorisnik().getEmail(), p.getId());
        Komentar kom1= b.vratiteKomentar(k.getEmail(),p.getId() );
        assertEquals(kom.getTekst(), kom1.getTekst());
        assertEquals(kom.getDatum(), kom1.getDatum());
        assertEquals(p,kom1.getProizvod());
        assertEquals(k, kom1.getKorisnik());
        //id ne uporedjujemo
        assertTrue(b.obrisiteKomentar(kom1.getId()));
        Komentar kom2 = b.vratiteKomentar(k.getEmail(), p.getId());
        assertNull(kom2);
    }
    @Test
    public void snimiteIzmeniteIzbrisiteOcenu(){
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        int proizvodId=36;
        int ocena = 8;
        assertTrue(b.daLiKorisnikSmeDaOceniProizvod(email, proizvodId));
        Proizvod p = b.vratiteProizvodPoId(proizvodId);
        double prosOcStara=p.getProsecnaOcena();

        b.snimiteOcenuSaRacunanjemProseka(ocena, email,proizvodId);
         p = b.vratiteProizvodPoId(proizvodId);
        assertEquals(9, p.getProsecnaOcena(),0);
        b.obrisiteOcenu(email, proizvodId);
        p = b.vratiteProizvodPoId(proizvodId);
        assertTrue(p.getProsecnaOcena()==prosOcStara);

    }

    @Test
    public void daLiKorisnikSmeDaOceniProizvod() throws Exception {
        assertTrue(b.daLiKorisnikSmeDaOceniProizvod(this.email, 36));
    }

    @Test
    public void daLiKorisnikSmeDaOceniProizvodPogresan() throws Exception {
        assertFalse(b.daLiKorisnikSmeDaOceniProizvod(this.neispravanEmail, 36));

    }

    @Test
    public void vratiteSveKomentareZaProizvod() throws Exception {
        List<Komentar> komentari = b.vratiteSveKomentareZaProizvod(proizvodId);
        System.out.println(komentari);
        assertFalse(komentari.isEmpty());
    }

    @Test
    public void vratiteSveOceneZaProizvod() throws Exception {
        List<Ocena> ocene = b.vratiteSveOceneZaProizvod(proizvodId);
        assertFalse(ocene.isEmpty());
    }











    @Test
    public void daLiKorisnikSmeDaKomentarise() throws Exception {
        assertTrue(b.daLiKorisnikSmeDaKomentariseProizvod(email, proizvodId));
    }
    @Test
    public void daLiKorisnikSmeDaKomentarisePogresan() throws Exception {
        assertFalse(b.daLiKorisnikSmeDaKomentariseProizvod(neispravanEmail, proizvodId));
    }

    @Test
    public void vratiteOcenu() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        Ocena o = b.vratiteOcenu("akikrasicDRUGIKORISNIK@gmail.com", proizvodId);
        assertNotNull(o);

    }
    @Test
    public void vratiteOcenuNeispravnu() throws Exception {
        Ocena o = b.vratiteOcenu(email, 37);
        assertNull(o);
    }

    @Test
    public void izmeniteKomentar() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        Komentar k = b.vratiteKomentar(email2, proizvodId);
        String tekstPrvi = k.getTekst();
        String tekstDrugi="asdsdasdasdas";
        assertTrue(b.izmeniteKomentar(tekstDrugi,LocalDateTime.now(),k.getKorisnik().getEmail(), k.getProizvod().getId()));
        k = b.vratiteKomentar(email2, proizvodId);
        assertEquals(tekstDrugi, k.getTekst());
        assertTrue(b.izmeniteKomentar(tekstPrvi,LocalDateTime.now(),k.getKorisnik().getEmail(), k.getProizvod().getId()));
        k = b.vratiteKomentar(email2, proizvodId);
        assertEquals(tekstPrvi, k.getTekst());
    }

    @Test
    public void izmeniteOcenu() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        Ocena o = b.vratiteOcenu(email2, proizvodId);
        int prva = o.getOcena();
        int druga = 4;//bas da ne bude 5 do 10;
        //i proveriProsecnuOcenu
        double prviProsek = o.getProizvod().getProsecnaOcena();
        assertTrue(b.izmeniteOcenuSaRacunanjemProseka(druga, email2, proizvodId));
        o = b.vratiteOcenu(email2, proizvodId);
        assertTrue(o.getOcena()==druga);
        assertFalse(prviProsek==o.getProizvod().getProsecnaOcena());
        assertTrue(b.izmeniteOcenuSaRacunanjemProseka(prva, email2, proizvodId));
        o = b.vratiteOcenu(email2, proizvodId);
        assertTrue(o.getOcena()==prva);
        assertTrue(o.getProizvod().getProsecnaOcena()==prviProsek);

    }





}