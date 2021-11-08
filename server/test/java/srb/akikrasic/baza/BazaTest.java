package srb.akikrasic.baza;

import org.junit.Before;
import org.junit.Test;
import srb.akikrasic.domen.*;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.loginkorisnika.RezultatLogovanja;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.pretragaproizvodazaizmenu.RezultatPretrageProizvoda;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Sifra;
import srb.akikrasic.token.Tokeni;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by aki on 7/29/17.
 */
public class BazaTest {
    private Baza b ;
    protected String email = "akikrasic@gmail.com";
    protected String email2="akikrasicDRUGIKORISNIK@gmail.com";
    protected String neispravanEmail = "email@koji je sigurno neispravan";
    protected byte[] sifra = "aA1!aa".getBytes();
    protected int proizvodId=36;
    @Before
    public void inic(){
        b = new Baza();
        b.setRadSaSifrom(new Sifra());
        b.setRadSaGenerisanjemTokena(new GenerisanjeTokena());
        b.setRadSaTokenima(new Tokeni());
        b.getRadSaTokenima().setRadSaGenerisanjemTokena(b.getRadSaGenerisanjemTokena());

    }

   // @Test
    public void snimiteNovogNepotvrdjenogKorisnika() throws Exception {
        b.snimiteNovogNepotvrdjenogKorisnika("Mika", "mika@pera.com", "sifra".getBytes(), "Branka Radicevica", "Pirot", "010341004", "062341094", "010373409", "token proba");
    }

    //@Test
    public void snimiteNovogKorisnika(){
        b.snimiteNovogNepotvrdjenogKorisnika("мика", "mika@gmail.com", "запорка".getBytes(), "Бранка Радичевића 16/7","Пирот","010341004", "062341094","010331004", "Хеширање да видимо да ли је у реду");
    }

    @Test
    public void daLiJeEmailSlobodanUObeTabele() throws Exception {
        assertTrue(b.daLiJeEmailSlobodanUObeTabele("ka163404m@student.etf.rs"));
    }
    @Test
    public void daLiEmailNijeSlobodanNepotvrdjeniKorisnik(){
        //  b.snimiteNovogKorisnika("", "test", "".getBytes(), "", "", "", "", "","");
        assertFalse(b.daLiJeEmailSlobodanUObeTabele("mika@pero.com"));//taj email ne treba da se brise iz tabele Nepotvrdjenikorisnik
    }
    @Test
    public void daLiEmailNijeSlobodanKorisnik(){
      //  b.snimiteNovogKorisnika("", "test", "".getBytes(), "", "", "", "", "","");
        assertFalse(b.daLiJeEmailSlobodanUObeTabele("akikrasic@gmail.com"));
    }

    @Test
    public void daLiJeMogucaPotvrdaKorisnika() throws Exception {
        String email = "email@testPotvrde";
        String token = "tokenZaPotvrduITest";
        //samo prvi put bez komentara
       // b.snimiteNovogNepotvrdjenogKorisnika("",email , "".getBytes(), "","", "", "", "", token);
        assertNotNull(b.daLiJeMogucaPotvrdaKorisnika(email, token));
    }
    @Test
    public void daLiJeMogucaPotvrdaKorisnikaGreskaMail() throws Exception {
        String email = "email@testPotvrdeGreska";
        String token = "tokenZaPotvrduITest";
        //samo prvi put bez komentara
        // b.snimiteNovogNepotvrdjenogKorisnika("",email , "".getBytes(), "","", "", "", "", token);
        assertNull(b.daLiJeMogucaPotvrdaKorisnika(email, token));
    }
    @Test
    public void daLiJeMogucaPotvrdaKorisnikaGreskaToken() throws Exception {
        String email = "email@testPotvrde";
        String token = "tokenZaPotvrduITestGreska";
        //samo prvi put bez komentara
        // b.snimiteNovogNepotvrdjenogKorisnika("",email , "".getBytes(), "","", "", "", "", token);
        assertNull(b.daLiJeMogucaPotvrdaKorisnika(email, token));
    }

   // @Test
    //komentar zato sto ce nekoliko puta u bazi korisnika da upise vrednost koju brise iz nepotvrdjenog korisnika
    public void potvrditeKorisnika() throws Exception {
        String email = "email@testPotvrdePotrda";
        String token = "tokenZaPotvrduPotvrdu";
        b.snimiteNovogNepotvrdjenogKorisnika("",email , "".getBytes(), "","", "", "", "", token);

        assertTrue(b.potvrditeKorisnika(b.daLiJeMogucaPotvrdaKorisnika(email, token)));
    }

    @Test
    public void logovanjeUspesno(){
        RezultatLogovanja rl = b.logovanjeKorisnika("akikrasic@gmail.com", "aA1!aa".getBytes());
        assertTrue(rl.isUspesno());
    }
    @Test
    public void logovanjeNeuspesno(){
        RezultatLogovanja rl = b.logovanjeKorisnika("akikrasic@gmail.com", "aA1!aaAA".getBytes());
        assertFalse(rl.isUspesno());
    }
    //@Test 19.09.2017.
    public void posaljiteZahtevZaPromenuSifreIPromeniteSifru() throws Exception {
        //posle testa izbrisati unos rucno iz tabele promena sifre
        String email="akikrasic@gmail.com";
        byte[] sifra="aA1!aa".getBytes();
        String token = b.posaljiteZahtevZaPromenuSifre(email);
        assertTrue(b.promenaSifre(email, token, sifra));
        RezultatLogovanja rl = b.logovanjeKorisnika(email, sifra);
        assertTrue(rl.isUspesno());
   }


    @Test
    public void posaljiteZahtevZaPromenuSifreNesipravanMejl() throws Exception {
        assertNull(b.posaljiteZahtevZaPromenuSifre("neispravanMejl"));
    }

    @Test
    public void promenaSifreFalse() throws Exception {
        assertFalse(b.promenaSifre("neispravanMejl", "", "".getBytes()));
    }

    @Test
    public void pretragaKorisnikaPoEmailu() throws Exception {
       Korisnik k = b.pretragaKorisnikaPoEmailu(email);
        assertEquals(email,k.getEmail() );
    }

    @Test
    public void pretragaKorisnikaPoEmailuNeuspesna() throws Exception {
        Korisnik k = b.pretragaKorisnikaPoEmailu(neispravanEmail);
        assertNull(k);
    }

    @Test
    public void kategorijeJedanVelikiTest() throws Exception {

        String nazivNoveKategorije="Kategorija proba";
        String izmenjenNaziv ="Kategorija proba izmena";
        assertTrue(b.dodajteKategoriju(nazivNoveKategorije));
        List<Kategorija> kategorije = b.vratiteSveKategorije();
        assertFalse(kategorije.isEmpty());
        Kategorija k = kategorije.get(0);//ovo je latinicom a sve je cirilicom pa je sigurno na 0
        assertEquals(nazivNoveKategorije, k.getNaziv());

        assertTrue(b.izmenaKategorije(k.getId(), izmenjenNaziv));
        kategorije = b.vratiteSveKategorije();
        assertFalse(kategorije.isEmpty());
        k = kategorije.get(0);//isto kao i prethodno navedena situacija
        assertEquals(izmenjenNaziv, k.getNaziv());

        assertTrue(b.obrisiteKategoriju(k.getId()));


    }

    @Test
    public void izmenitePodatkeZaKorisnikaNeispravanMejl() throws Exception {
        assertFalse(b.izmenitePodatkeZaKorisnika("", neispravanEmail,"".getBytes(),"", "", "", "", ""));

    }

    @Test
    public void izmenitePodatkeZaKorisnika() throws Exception {
        String novoImeNaziv = "Aleksandar Krasic";
        String email = "akikrasic@gmail.com";
        byte[] novaSifra = "aaAA11!!".getBytes();
        String novaAdresa ="Branka Radicevica 16/7";
        String novoMesto="Pirot";
        String novTel1="0103410041";
        String novTel2 = "0623410941";
        String novTel3= "0103734091";

        Korisnik k = b.pretragaKorisnikaPoEmailu(email);
        String imeNaziv = k.getImeNaziv();
        String adresa = k.getAdresa();
        String mesto = k.getMesto();
        String tel1 =k.getTel1();
        String tel2= k.getTel2();
        String tel3 = k.getTel3();

       assertTrue( b.izmenitePodatkeZaKorisnika(novoImeNaziv, email, "".getBytes(), novaAdresa, novoMesto, novTel1, novTel2, novTel3));
        k = b.pretragaKorisnikaPoEmailu(email);
        assertEquals(novoImeNaziv, k.getImeNaziv());
        assertEquals(novaAdresa, k.getAdresa());
        assertEquals(novoMesto, k.getMesto());
        assertEquals(novTel1, k.getTel1());
        assertEquals(novTel2, k.getTel2());
        assertEquals(novTel3, k.getTel3());

        assertTrue(b.izmenitePodatkeZaKorisnika(imeNaziv, email, "".getBytes(), adresa, mesto, tel1, tel2, tel3));
        k = b.pretragaKorisnikaPoEmailu(email);
        assertEquals(imeNaziv, k.getImeNaziv());
        assertEquals(adresa, k.getAdresa());
        assertEquals(mesto, k.getMesto());
        assertEquals(tel1, k.getTel1());
        assertEquals(tel2, k.getTel2());
        assertEquals(tel3, k.getTel3());


    }

    @Test
    public void izmenitePodatkeZakorisnikaSifra() throws Exception {
        byte[] novaSifra ="aaAA11!!".getBytes();
        byte[] staraSifra ="aA1!aa".getBytes();
        Korisnik k = b.pretragaKorisnikaPoEmailu(email);
        b.izmenitePodatkeZaKorisnika(k.getImeNaziv(),email, novaSifra, k.getAdresa(), k.getMesto(), k.getTel1(), k.getTel2(), k.getTel3());
        RezultatLogovanja rl = b.logovanjeKorisnika(email, novaSifra);
        assertTrue(rl.isUspesno());
        b.izmenitePodatkeZaKorisnika(k.getImeNaziv(),email, staraSifra, k.getAdresa(), k.getMesto(), k.getTel1(), k.getTel2(), k.getTel3());
        rl = b.logovanjeKorisnika(email, staraSifra);
        assertTrue(rl.isUspesno());

    }

    @Test
    public void nazivKategorijeSeNalaziUBazi() throws Exception {
        assertTrue(b.daLiSeNazivKategorijeNalaziUBazi("Воће"));
    }
    @Test
    public void nazivKategorijeSeNalaziUBaziGreska() throws Exception {
       assertFalse( b.daLiSeNazivKategorijeNalaziUBazi("Voce"));
    }

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

    @Test
    public void privremeniProizvodVrednost() throws Exception {
        long vr = b.ucitajteVrednostBrojaPrivremenogProizvodaNaPocetku();
        assertTrue(vr>=0L);
        long vr1 = vr+1;
        b.sacuvajteVrednostBrojaPrivremenogPRoizvodaNaKraju(vr1);

        long vr2 = b.ucitajteVrednostBrojaPrivremenogProizvodaNaPocetku();
        assertEquals(vr1, vr2);
        b.sacuvajteVrednostBrojaPrivremenogPRoizvodaNaKraju(vr);
    }

    @Test
    public void napraviteProizvodSaPodacimaIzBaze() throws Exception {
        Map<String, Object > mapa = new LinkedHashMap<>();
        Baza b = spy(Baza.class);

        Korisnik k = new Korisnik();
        Kategorija kat = new Kategorija();
        kat.setNaziv("zmaj");
        k.setEmail("akikrasic@gmail.com");
        when(b.pretragaKorisnikaPoEmailu("akikrasic@gmail.com")).thenReturn(k);
        when(b.vratiteKategorijuPoId(1)).thenReturn(kat);
        String naziv = "Naziv proizvoda";
        String opis = "Opis proizvoda, ovo je opis proizvoda sdhlsfhkslkdjfhsjdkfhsldkjfhsdjkfhdskjfhsdkjf";
        int id = 1;
        double cena = 123.45;
        mapa.put("id",id );
        mapa.put("naziv",naziv);
        mapa.put("opis", opis);
        mapa.put("trenutnacena", cena);
        mapa.put("kategorija_id", 1);
        mapa.put("aktivan", true);
        mapa.put("korisnik_email", "akikrasic@gmail.com");
        mapa.put("prosecna_ocena", 0D);
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna


        Proizvod p = b.napraviteProizvodSaPodacimaIzBaze(mapa);
        assertEquals(p.getId(), id);
        assertEquals(p.getNaziv(), naziv);
        assertEquals(p.getOpis(), opis);
        assertTrue((double)p.getTrenutnaCena()== (double)cena);
        assertEquals(kat, p.getKategorija());
        assertEquals(k, p.getKorisnik());
    }


    @Test
    public void velikiTestUnosIzmenaBrisanjeProizvoda() throws Exception {

        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna
        System.out.println(slike.vratiteSveSlikeProizvoda(1));

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


        int id = b.dodajteNoviProizvod(email, naziv, opis, cena, kategorijaId);
        assertTrue(id>0);
        Proizvod p = b.vratiteProizvodPoId(id);
        assertEquals(p.getId(), id);
        assertEquals(p.getNaziv(), naziv);
        assertEquals(p.getOpis(), opis);
        assertEquals(p.getTrenutnaCena(), cena, 0);
        assertEquals(p.getKategorija().getId(), kategorijaId);
        assertEquals(p.getKorisnik().getEmail(), email);

        b.unesiteKljucneReci(kljucneReci, p.getId());
        List<KljucnaRec> kljucneReciIzBaze= b.vratiteSveKljucnereciZaProizvod(p);
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
        p.setKategorija(b.vratiteKategorijuPoId(kategorijaIdIzmena));


        assertTrue(b.izmeniteProizvodPodaci(p));
        assertEquals(p.getId(), id);
        assertEquals(p.getNaziv(), nazivIzmena);
        assertEquals(p.getOpis(), opisIzmena);
        assertEquals(p.getTrenutnaCena(), cenaIzmena, 0);
        assertEquals(p.getKategorija().getId(), kategorijaIdIzmena);
        assertEquals(p.getKorisnik().getEmail(), email);

        cenaIzmena=10000.123;
        p.setTrenutnaCena(cenaIzmena);
       assertTrue( b.izmeniteProizvodCenu(p));
       assertEquals(cenaIzmena, p.getTrenutnaCena(), 0);
        //izmena kljucnih reci
        String[] nizKljucnihReciIzmena= { "негмо", "јабучар", "стара сорта", "српски", "прави парадајз"};
        ArrayList<String> kljucneReciIzmena= new ArrayList<>();
        Arrays.stream(nizKljucnihReciIzmena).forEach(k->{
            kljucneReciIzmena.add(k);
        });

        b.izmeniteKljucneReci(kljucneReciIzmena, p.getId());
        kljucneReciIzBaze= b.vratiteSveKljucnereciZaProizvod(p);
        Map<String, KljucnaRec> mapaIzmena = new LinkedHashMap<>();
        kljucneReciIzBaze.forEach(k->{
            mapaIzmena.put(k.getNaziv(), k);
        });
        kljucneReciIzmena.forEach(s->{
            assertNotNull(mapaIzmena.get(s));
        });
        assertEquals(kljucneReciIzBaze.size(),5);


        b.obrisiteKljucneReci(mapaIzmena.values(), p.getId());
        assertTrue(b.obrisiteProizvod(p.getId()));
    }

    @Test
    public void pretragaProizvodaNista() throws Exception {
        List<String> kljucneReci = new ArrayList<>();
        List<Proizvod> r =b.pretragaProizvoda("", "","",-1, -1,kljucneReci,"","");
        assertTrue(r.isEmpty());
        //assertEquals(0, r.getUkBrojProizvoda());
        assertEquals(0, r.size());
    }

    @Test
    public void pretragaImeNazivUneto() throws Exception {
        List<String> kljucneReci = new ArrayList<>();


        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna


        RezultatPretrageProizvoda r = new RezultatPretrageProizvoda();
         r.setLista(b.pretragaProizvoda("а","","",-1, -1,kljucneReci,"",""));
         r.setUkBrojProizvoda(r.getLista().size());
        assertFalse(r.getLista().isEmpty());
        System.out.println(r.getUkBrojProizvoda());
        System.out.println(r.getLista().size());
        System.out.println(r.getLista());
        assertTrue(r.getUkBrojProizvoda()>=r.getLista().size());
    }
    @Test
    public void pretragaEmailUnet() throws Exception {
        List<String> kljucneReci = new ArrayList<>();


        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna


        RezultatPretrageProizvoda r = new RezultatPretrageProizvoda();

        r.setLista(b.pretragaProizvoda("","a","",-1, -1,kljucneReci,"",""));
        r.setUkBrojProizvoda(r.getLista().size());

        assertFalse(r.getLista().isEmpty());
        assertTrue(r.getUkBrojProizvoda()>=r.getLista().size());
    }
    @Test
    public void pretragaNazivProizvoda() throws Exception {
        List<String> kljucneReci = new ArrayList<>();


        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna


        RezultatPretrageProizvoda r = new RezultatPretrageProizvoda();

        r.setLista(b.pretragaProizvoda("","","а",-1, -1,kljucneReci,"",""));
        r.setUkBrojProizvoda(r.getLista().size());
        assertFalse(r.getLista().isEmpty());
        assertTrue(r.getUkBrojProizvoda()>=r.getLista().size());
    }
    @Test
    public void pretragaCenaOd() throws Exception {
        List<String> kljucneReci = new ArrayList<>();


        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna


        RezultatPretrageProizvoda r = new RezultatPretrageProizvoda();

        r.setLista(b.pretragaProizvoda("","","",100, -1,kljucneReci,"",""));
        r.setUkBrojProizvoda(r.getLista().size());
        assertFalse(r.getLista().isEmpty());
        assertTrue(r.getUkBrojProizvoda()>=r.getLista().size());
    }
    @Test
    public void pretragaCenaDo() throws Exception {
        List<String> kljucneReci = new ArrayList<>();


        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna


        RezultatPretrageProizvoda r = new RezultatPretrageProizvoda();

        r.setLista(b.pretragaProizvoda("а","","",-1, 10000,kljucneReci,"",""));
        r.setUkBrojProizvoda(r.getLista().size());
        assertFalse(r.getLista().isEmpty());
        System.out.println(r.getUkBrojProizvoda());
        System.out.println(r.getLista().size());
        assertTrue(r.getUkBrojProizvoda()>=r.getLista().size());
    }
    @Test
    public void pretragaKljucneReci() throws Exception {
        List<String> kljucneReci = new ArrayList<>();
        kljucneReci.add("парадајз");

        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna


        RezultatPretrageProizvoda r = new RezultatPretrageProizvoda();

        r.setLista(b.pretragaProizvoda("","","",-1, -1,kljucneReci,"",""));
        r.setUkBrojProizvoda(r.getLista().size());
        assertFalse(r.getLista().isEmpty());
        assertTrue(r.getUkBrojProizvoda()>=r.getLista().size());
    }
    @Test
    public void pretragaKategorijaNaziv() throws Exception {
        List<String> kljucneReci = new ArrayList<>();


        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna


        RezultatPretrageProizvoda r = new RezultatPretrageProizvoda();

        r.setLista(b.pretragaProizvoda("","","",-1, -1,kljucneReci,"Воћ",""));
        r.setUkBrojProizvoda(r.getLista().size());
        assertFalse(r.getLista().isEmpty());
        assertTrue(r.getUkBrojProizvoda()>=r.getLista().size());
    }
    @Test
    public void pretragaOpis() throws Exception {
        List<String> kljucneReci = new ArrayList<>();


        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna


        RezultatPretrageProizvoda r = new RezultatPretrageProizvoda();

        r.setLista(b.pretragaProizvoda("","","",-1, -1,kljucneReci,"","а"));
        r.setUkBrojProizvoda(r.getLista().size());
        assertFalse(r.getLista().isEmpty());
        assertTrue(r.getUkBrojProizvoda()>=r.getLista().size());
    }
    @Test
    public void pretragaviseParametra() throws Exception {
        List<String> kljucneReci = new ArrayList<>();
        kljucneReci.add("а");

        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna


        RezultatPretrageProizvoda r = new RezultatPretrageProizvoda();

        r.setLista(b.pretragaProizvoda("а","a","г",100, 10000,kljucneReci,"Воћ","а"));
        r.setUkBrojProizvoda(r.getLista().size());
        assertFalse(r.getLista().isEmpty());
        assertTrue(r.getUkBrojProizvoda()>=r.getLista().size());
    }


    @Test
    public void like() throws Exception {
        String proba = "Ovo je string za probu";

        assertEquals(new StringBuilder("%").append(proba).append("%").toString(), b.like(proba));
    }

    @Test
    public void likeMalaVelikaSlova() throws Exception {
        String proba = "VELIKA SLOVA";
        assertEquals(new StringBuilder("%").append("velika slova").append("%").toString(), b.like(proba.toLowerCase()));
    }
    @Test
    public void likeMalaVelikaSlovaЋирилица() throws Exception {
        String proba = "ВЕЛИКА СЛОВА";
        assertEquals(new StringBuilder("%").append("велика слова").append("%").toString(), b.like(proba.toLowerCase()));
    }
    @Test
    public void vratiteSveProizvode() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna

        System.out.println(b.vratiteSveProizvode());
    }

    @Test
    public void vratiteSveProizvodeKorisnika() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(1)).thenReturn(null);;//ne smeta null ovde je baza vazna

        List<Proizvod> proizvodiKorisnika = b.vratiteSveProizvodeKorisnika(email);
        proizvodiKorisnika.forEach(p->{
            assertTrue(p.getKorisnik().getEmail().equals(email));
        });
    }

    @Test
    public void vratiteSveKljucnereciZaProizvodStringovi() throws Exception {
       Proizvod p = new Proizvod();
       p.setId(36);//taj ima sigurno kljucne reci
        List<String> l= b.vratiteSveKljucnereciZaProizvodStringovi(p);
        assertFalse(l.isEmpty());
        System.out.println(l);
    }

    @Test
    public void proizvodiKojiNeSmejuDaSeMenjaju() throws Exception {
        List<Integer> lista = b.proizvodiKojiNeSmejuDaSeMenjaju();

        assertFalse(lista.isEmpty());

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
    public void vratiteProizvodZaPrikazKorisnikuSaKomentarimaIOcenama() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna



        Proizvod p = b.vratiteProizvodZaPrikazKorisnikuSaKomentarimaIOcenama(proizvodId);
        assertTrue(p.getId()==proizvodId);
        assertNotNull(p.getNaziv());
        assertFalse(p.getKomentari().isEmpty());
        assertFalse(p.getOcene().isEmpty());

        assertTrue(p.getProsecnaOcena()>0);
    }

    @Test
    public void vratiteStoPRoizvoda() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna


        List<Proizvod> proizvodi = b.vratiteStoProizvoda();
        assertFalse(proizvodi.isEmpty());
        assertTrue(proizvodi.size()<=100);
    }

    @Test
    public void vratiteSveProizvodeNekogKorisnikaPoEmailuDaIhOnMenja() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna


        List<Proizvod> proizvodi = b.vratiteSveProizvodeNekogKorisnikaPoEmailuDaIhOnMenja(email);
        proizvodi.forEach(p->{
            assertTrue(p.getKorisnik().getEmail().equals(email));
        });
    }


    @Test
    public void izvrsiteQuery() throws Exception {
        b.izvrsiteQuery("SELECT * from proizvod WHERE id=?", proizvodId).forEach(m->{

            assertTrue(((Integer)m.get("id"))== proizvodId);
        });
    }




    @Test
    public void napraviteNaruceniproizvodSaPodacimaIzBaze() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        Map<String, Object> mapa = new LinkedHashMap<>();
        int id=1;
        double kolicina = 100.5;
        double cena = 123.56;
        double cenaPutaKolicina = cena* kolicina;
        boolean prodavacPotvrdio = true;
        boolean kupacPotvrdio = true;
        boolean otkazan = true;
        mapa.put("id",id);
        mapa.put("kolicina",kolicina);
        mapa.put("cena",cena);
        mapa.put("cenaputakolicina", cenaPutaKolicina);
        mapa.put("prodavacpotvrdio",prodavacPotvrdio);
        mapa.put("kupacpotvrdio", kupacPotvrdio);
        mapa.put("otkazan", otkazan);
        mapa.put("proizvod_id", proizvodId);
        Narudzbenica n = new Narudzbenica();
        n.setId(id);
        NaruceniProizvod np = b.napraviteNaruceniproizvodSaPodacimaIzBaze(mapa, n);

        assertTrue(np.getId()==id);
        assertTrue(np.getKolicina()==kolicina);
        assertTrue(np.getCena()==cena);
        assertTrue(np.getCenaPutaKolicina()== cenaPutaKolicina);
        assertTrue(np.getCenaPutaKolicina()==cena*kolicina);
        assertTrue(np.isProdavacPotvrdio()== prodavacPotvrdio);
        assertTrue(np.isKupacPotvrdio()==kupacPotvrdio);
        assertTrue(np.isOtkazan()==otkazan);
        assertTrue(np.getProizvod().getId()==proizvodId);
        assertTrue(np.getNarudzbenica().getId()==id);

    }


    @Test
    public void narudzbeniceJedanVelikiTest() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        LocalDateTime datum = LocalDateTime.now();
        Narudzbenica n = new Narudzbenica();

        Korisnik kupac = b.pretragaKorisnikaPoEmailu("akikrasicDRUGIKORISNIK@gmail.com");
        Korisnik prodavac = b.pretragaKorisnikaPoEmailu(email);
        n.setKupac(kupac);
        n.setDatum(datum);
        n.setDostava("Курирска служба");
        n.setChargeId("ch_asasdsaddasd");
        n.setZbir(123);


        Proizvod p = b.vratiteProizvodPoId(36);
        NaruceniProizvod np = new NaruceniProizvod();
        np.setProizvod(p);
        np.setNarudzbenica(n);
        np.setOtkazan(false);
        np.setKupacPotvrdio(false);
        np.setProdavacPotvrdio(false);
        np.setKolicina(10);
        np.setCena(30);
        np.setCenaPutaKolicina(300);
        n.getNaruceniProizvodi().add(np);

        b.snimiteNarudzbenicu(n);
        assertTrue(n.getId()>0);
        Narudzbenica n1 = b.vratiteNarudzbenicuPoId(n.getId());
        assertTrue(n.getId()==n1.getId());
        assertEquals(n.getChargeId(), n1.getChargeId());
        assertEquals(n.getDostava(), n1.getDostava());
        assertEquals(n.getDatum(), n1.getDatum());
        assertEquals(n.getKupac().getEmail(), n1.getKupac().getEmail());
        assertTrue(n.getZbir()==n1.getZbir());


        List<NaruceniProizvod> npLista= b.vratiteNaruceneProizvodeZaNarudzbenicu(n1);
        NaruceniProizvod np1 = npLista.get(0);
        assertTrue(np.getProizvod().getId()==np1.getProizvod().getId());
        assertTrue(np.getNarudzbenica().getId()==np1.getNarudzbenica().getId());
        assertEquals(np.isOtkazan(), np1.isOtkazan());
        assertEquals(np.isKupacPotvrdio(), np1.isOtkazan());
        assertEquals(np.isProdavacPotvrdio(), np1.isProdavacPotvrdio());
        assertTrue(np.getKolicina()==np1.getKolicina());
        assertTrue(np.getCena()==np1.getCena());
        assertTrue(np.getCenaPutaKolicina()==np1.getCenaPutaKolicina());

        NaruceniProizvod np2 = n.getNaruceniProizvodi().get(0);

        assertTrue(np1.getProizvod().getId()==np2.getProizvod().getId());
        assertTrue(np1.getNarudzbenica().getId()==np2.getNarudzbenica().getId());
        assertEquals(np1.isOtkazan(), np2.isOtkazan());
        assertEquals(np1.isKupacPotvrdio(), np2.isOtkazan());
        assertEquals(np1.isProdavacPotvrdio(), np2.isProdavacPotvrdio());
        assertTrue(np1.getKolicina()==np2.getKolicina());
        assertTrue(np1.getCena()==np2.getCena());
        assertTrue(np1.getCenaPutaKolicina()==np2.getCenaPutaKolicina());

        List<Narudzbenica> narudzbeniceKupca = b.vratiteNarudzbeniceKupcu(kupac.getEmail());
        boolean nadjenaNarudzbenica= false;
        for(Narudzbenica nar:narudzbeniceKupca){
            if(nar.getId()==n.getId()){
                nadjenaNarudzbenica=true;
            }
        }//mozda je malo bez veze nacin pretrage ali bice jos puno narudzbenica za tog korisnika
        assertTrue(nadjenaNarudzbenica);
        List<NaruceniProizvod> naruceniProizvodiProdavca = b.vratiteNaruceneProizvodeProdavca(prodavac.getEmail());
        boolean nadjenNaruceniproizvod=false;
        for(NaruceniProizvod nprv:naruceniProizvodiProdavca){
            if(nprv.getNarudzbenica().getId()==n.getId()){
                nadjenNaruceniproizvod=true;
            }
        }
        assertTrue(nadjenNaruceniproizvod);
        b.prodavacPotvrdjujeNaruceniProizvod(np1);
        b.kupacPotvrdjujeNaruceniProizvod(np1);
        b.otkazanNaruceniProizvod(np1);
        npLista= b.vratiteNaruceneProizvodeZaNarudzbenicu(n);
         np1 = npLista.get(0);
        assertTrue(np1.isOtkazan());
        assertTrue(np1.isKupacPotvrdio());
        assertTrue(np1.isProdavacPotvrdio());
        b.obrisiteNarudzbenicu(n);

    }

    @Test
    public void snimiteNaRacunSajtaIVratiteStanjeRacunaSajta() throws Exception {
        double stanje = b.vratiteStanjeRacunaSajta();
        double kolicinaZaDodavanje = 100;
        b.snimiteNaRacunSajta(stanje+kolicinaZaDodavanje);
        assertTrue((stanje+kolicinaZaDodavanje)==b.vratiteStanjeRacunaSajta());
        b.snimiteNaRacunSajta(stanje);
        assertTrue(stanje== b.vratiteStanjeRacunaSajta());

    }

    @Test
    public void snimiteNaRacunZaPrenosIVratiteStanjeRacunaZaPrenos() throws Exception {
        double stanje = b.vratiteStanjeRacunaZaPrenos();
        double kolicinaZaDodavanje = 100;
        b.snimiteNaRacunZaPrenos(stanje+kolicinaZaDodavanje);
        assertTrue((stanje+kolicinaZaDodavanje)==b.vratiteStanjeRacunaZaPrenos());
        b.snimiteNaRacunZaPrenos(stanje);
        assertTrue(stanje== b.vratiteStanjeRacunaZaPrenos());
    }

    @Test
    public void snimiteStanjeNaRacunKorisnikaIVratiteStanjeRacunaKorisnika() throws Exception {
        double stanje = b.vratiteStanjeRacunaKorisnika(email);
        double kolicinaZaDodavanje = 100;
        b.snimiteStanjeNaRacunKorisnika(email,stanje+kolicinaZaDodavanje);
        assertTrue((stanje+kolicinaZaDodavanje)==b.vratiteStanjeRacunaKorisnika(email));
        b.snimiteStanjeNaRacunKorisnika(email, stanje);
        assertTrue(stanje== b.vratiteStanjeRacunaKorisnika(email));
    }
    @Test
    public void radSaZalbama() throws Exception{
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        Narudzbenica n = new Narudzbenica();
        Korisnik k = b.pretragaKorisnikaPoEmailu(email);
        n.setKupac(k);
        n.setDatum(LocalDateTime.now());
        n.setZbir(0);
        n.setChargeId("");
        n.setDostava("");
        NaruceniProizvod np = new NaruceniProizvod();
        n.getNaruceniProizvodi().add(np);
        np.setNarudzbenica(n);
        String porukaKupac="asdjashdgsahd";
        String porukaProdavac="sdsaasdsadasd";
        Proizvod p = b.vratiteProizvodPoId(proizvodId);
        np.setProizvod(p);
        np.setCenaPutaKolicina(0);
        np.setCena(0);
        b.snimiteNarudzbenicu(n);
        n= b.vratiteNarudzbenicuPoId(n.getId());
        np = n.getNaruceniProizvodi().get(0);
       assertTrue( b.korisnikSeZalio(np, porukaKupac));
        Zalba z = b.vratiteZalbuZaNaruceniProizvod(np);
        assertEquals(z.getTekstKupca(), porukaKupac);
        assertNotNull(z.getDatumKupca());
        assertNull(z.getDatumProdavca());
        assertNull(z.getTekstProdavca());

        z.setTekstProdavca(porukaProdavac);
       assertTrue( b.prodavacOdgovaraNaZalbu(z));

       z= b.vratiteZalbuZaNaruceniProizvod(np);

        assertEquals(z.getTekstKupca(), porukaKupac);
        assertEquals(z.getTekstProdavca(), porukaProdavac);
        assertNotNull(z.getDatumKupca());
        assertNotNull(z.getDatumProdavca());
        assertTrue(b.obrisiteNarudzbenicu(n));
        assertTrue(b.obrisiteZalbu(z));

    }

    @Test
    public void vratiteNaruceniProizvodPoId() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        List<Narudzbenica> l = b.vratiteNarudzbeniceKupcu(email);
        NaruceniProizvod np = l.get(0).getNaruceniProizvodi().get(0);
        NaruceniProizvod np1 = b.vratiteNaruceniProizvodPoId(np.getId());
        assertTrue(np.getId()==np1.getId());
        assertTrue(np.getProizvod().getId()==np1.getProizvod().getId());

    }

    @Test
    public void vratiteNarudzbenicuPoIdBezNarucenihProizvoda() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        List<Narudzbenica> l = b.vratiteNarudzbeniceKupcu(email);
        Narudzbenica n = l.get(0);
        Narudzbenica n1 = b.vratiteNarudzbenicuPoIdBezNarucenihProizvoda(n.getId());
        assertTrue(n1.getNaruceniProizvodi().isEmpty());
        assertTrue(n.getId()==n1.getId());

    }

    @Test
    public void vratiteNaruceneProizvodeZaNarudzbenicuBezPovratneReference() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        List<Narudzbenica> l = b.vratiteNarudzbeniceKupcu(email);
        for(Narudzbenica n : l){
            List<NaruceniProizvod> lnp = b.vratiteNaruceneProizvodeZaNarudzbenicuBezPovratneReference(n);
            lnp.forEach(np->{
                assertTrue(np.getNarudzbenica()==null);
            });
        }
    }

    @Test
    public void vratiteNarudzbeniceKupcuLimit() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        int pocetak=0;
        int kraj=10;
        List<Narudzbenica> l = b.vratiteNarudzbeniceKupcuLimit(email,pocetak, kraj );

        assertTrue(l.size()<=kraj-pocetak);
        assertTrue(l.size()>0);
    }

    @Test
    public void vratiteNaruceneProizvodeProdavcaLimit() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        int pocetak=0;
        int kraj=10;

        List<NaruceniProizvod> l = b.vratiteNaruceneProizvodeProdavcaLimit(email, pocetak, kraj);
        assertTrue(l.size()<=kraj-pocetak);
        assertTrue(l.size()>0);
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

    @Test
    public void komentarNoviApi() throws Exception {
        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        String tekstKomentara = "Ово је коментар за испробавање и тестирање";
        assertTrue(b.snimiteKomentarNovaMetoda(tekstKomentara, email, proizvodId));
        KomentarIOcena ko = b.vratiteKomentarIOcenuZaKorisnikaIProizvodNovi(email, proizvodId);
        assertEquals(ko.getTekst(), tekstKomentara);
        assertNotNull(ko.getDatum());
        String tekstKomentaraNovi="Ово је текст коментара нови да се испроба измена коментара.";
        b.snimiteKomentarNovaMetoda(tekstKomentaraNovi, email, proizvodId);
        ko = b.vratiteKomentarIOcenuZaKorisnikaIProizvodNovi(email, proizvodId);
        assertEquals(ko.getTekst(), tekstKomentaraNovi);
        assertNotNull(ko.getDatum());
        Proizvod p = b.vratiteProizvodPoId(proizvodId);
        double prosecnaOcena = p.getProsecnaOcena();
        Random r = new Random(47);

        int ocena =10;
        b.snimiteOcenuNovaMetoda(ocena, email, proizvodId);
        ko = b.vratiteKomentarIOcenuZaKorisnikaIProizvodNovi(email, proizvodId);
        assertTrue(ocena==ko.getOcena());
        p = b.vratiteProizvodPoId(proizvodId);
        assertNotNull(p.getProsecnaOcena());
        assertTrue(!b.vratiteSveKomentareIOceneZaProizvodNovi(proizvodId).isEmpty());
    }

    @Test
    public void vratiteStanjeRacuna() throws Exception {
        Racun r = b.vratiteStanjeRacuna();
        assertTrue(r.getRacunSajta()>0);
        assertTrue(r.getRacunZaPrenos()>0);
    }

    @Test
    public void vratiteSagovornikeZaKorisnika() throws Exception {
        List<Korisnik> l = b.vratiteSagovornikeZaKorisnika(email);
        assertFalse(l.isEmpty());
    }

    @Test
    public void vratitePoruke() throws Exception {
        int limit=20;
        List<Poruka> poruke = b.vratitePoruke(email, email2,limit, 0 );
        assertTrue(poruke.size()==limit);
        for(int i=0;i<limit;i++) {
            Poruka p = poruke.get(i);
            assertNotNull(p.getTekst());
            assertNotNull(p.getDatum());
        }
    }

    @Test
    public void izvestajZaAdmina() throws Exception {

        Slike slike = mock(Slike.class);
        b.setSlike(slike);
        when(slike.vratiteSveSlikeProizvoda(36)).thenReturn(null);;//ne smeta null ovde je baza vazna

        int limit=10;
        int offset = 0;
        List<NaruceniProizvod> lista = b.izvestajZaAdmina(limit, offset);
        assertTrue(lista.size()==limit);
        for(int i=0;i<lista.size();i++){
            NaruceniProizvod np = lista.get(i);
            assertNotNull(np);
            assertNotNull(np.getProizvod());
            assertNotNull(np.getNarudzbenica());
            assertNotNull(np.getNarudzbenica().getKupac());
            assertNotNull(np.getProizvod().getKorisnik());
        }
    }
}