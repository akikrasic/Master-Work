package slike;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import srb.akikrasic.slike.Slike;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by aki on 8/26/17.
 */

public class SlikeTest {
    private Slike slike;

    @Before
    public void setUp() throws Exception {
        slike = new Slike();
    }

    @Test
    public void getPutanjaZaSlikeKorisnika() throws Exception {
        assertEquals("/var/www/htdocs/masterRadKA163404143095/slike/korisnik/", slike.getPutanjaZaCuvanjeSlikeKorisnika());
    }

    @Test
    public void uzmiteSledeci() throws Exception {
        long prvi = slike.uzmiteBrojSledeceSlike();
        long drugi = slike.uzmiteBrojSledeceSlike();
        assertEquals(1, drugi - prvi);
    }

    @Test
    public void napraviteDirektorijumZaSmestajSlika() throws Exception {
        String glavniDirektorijum = slike.getPutanjaZaCuvanjeSlikeKorisnika();
        String email = "akikrasic@gmail.com/test";
        assertTrue(slike.napraviteDirektorijumZaSmestajSlikaKorisnika(email));
        File f = new File(glavniDirektorijum + "/" + email);
        assertTrue(f.exists());
        f.delete();//da se taj direktorijum posle pravljenaj lepo pocisti
    }

    @Test
    public void napraviteDirektorijumZaPrivremeniSmestajSlikaProizvoda() throws Exception {
        String privremeniDirektorijumPutanja = slike.getPutanjaZacuvanjePrivremenihSlika();
        String email = "akikrasic@gmail.com/test";
        slike.napraviteDirektorijumZaSmestajPrivremenihSlika(email);

        File f = new File(privremeniDirektorijumPutanja + email);
        assertTrue(f.exists());
        f.delete();
    }

    @Test
    public void obrisiteSlikuAkoPostoji() throws Exception {
        String direktorijumZaPretraguIBrisanje = "/var/www/htdocs/masterRadKA163404143095/slike/korisnik/test";
        File direktorijum = new File(direktorijumZaPretraguIBrisanje);
        File[] fajlovi = direktorijum.listFiles();
        assertTrue(fajlovi.length > 0);
        assertEquals(fajlovi[0].getName(), "slikaProfila.png");
        File slikaProfila = new File(direktorijumZaPretraguIBrisanje + "/slikaProfila.png");
        assertEquals(slikaProfila.getName(), "slikaProfila.png");
        Path staza = Paths.get("/var/www/htdocs/masterRadKA163404143095/slike/korisnik/test/" + "slikaProfila.png");
        byte[] slika = Files.readAllBytes(staza);
        assertTrue(slika.length > 0);
        slike.obrisiteSlikuAkoJeUneta(direktorijumZaPretraguIBrisanje);
        direktorijum = new File(direktorijumZaPretraguIBrisanje);
        fajlovi = direktorijum.listFiles();
        assertTrue(fajlovi.length == 0);
        //znaci nema je
        Path write = Files.write(staza, slika);

        //provera da li je ima
        direktorijum = new File(direktorijumZaPretraguIBrisanje);
        fajlovi = direktorijum.listFiles();
        assertTrue(fajlovi.length > 0);


    }

    @Test
    public void vratiteEkstenzijuSlikeProfila() throws Exception {
        assertEquals("png", slike.vratiteEkstenzijuSlikeProfila("test"));
    }

    @Test
    public void uzmiteSledeciDirektorijum() throws Exception {
        long prvi = slike.uzmiteBrojSledecegDirektorijuma();
        long drugi = slike.uzmiteBrojSledecegDirektorijuma();
        assertEquals(1, drugi - prvi);
    }

    @Test
    public void napraviteDirektorijumZaKorisnikaKojiUnosiNoviProizvod() throws Exception {
        String email = "akikrasic@gmail.com/test";
        long id = 1;
        assertTrue(slike.napraviteDirektorijumZaKorisnikaKojiUnosiNoviProizvod(email, id));
        File f = new File(slike.getPutanjaZacuvanjePrivremenihSlika() + email + "/" + 1);
        assertTrue(f.exists());
        f.delete();
    }

    @Test
    public void vratiteIDPrivremenogFolderaIKreirajteGa() throws Exception {
        Slike slikeMock = spy(Slike.class);
        when(slikeMock.uzmiteBrojSledecegDirektorijuma()).thenReturn(1L);
        when(slikeMock.napraviteDirektorijumZaKorisnikaKojiUnosiNoviProizvod("akikrasic@gmail.com", 1L)).thenReturn(true);
        String email = "akikrasic@gmail.com";
        assertEquals(slikeMock.uzmiteBrojSledecegDirektorijuma(), 1L);
        assertEquals(1L, slikeMock.vratiteIDPrivremenogFolderaIKreirajteGa(email));
        assertTrue(slikeMock.daLIKorisnikImaPravaDaSnimaUFolder(email, 1L));

    }

    @Test
    public void napravitePutanjuFolderaPrivremenogProizvoda() throws Exception {
        assertEquals("/var/www/htdocs/masterRadKA163404143095/slike/privr/akikrasic@gmail.com/1/", slike.napravitePutanjuFolderaPrivremenogProizvoda("akikrasic@gmail.com", 1L));
    }

    @Test
    public void vratiteSveSlikeIZPrivremenogFolderaProizvoda() throws Exception {
        assertTrue(slike.vratiteSveSlikeIZPrivremenogFolderaProizvoda("test", 1L).size() == 3);
    }

    @Test
    public void putanjeNoveSlikeSaNovimImenom() throws Exception {
        AtomicLong al = new AtomicLong(1);
        this.slike.setBrojSlike(al);
        assertEquals(
                "/var/www/htdocs/masterRadKA163404143095/slike/privr/akikrasic@gmail.com/1/1.png",
                slike.putanjeNoveSlikeSaNovimImenom("akikrasic@gmail.com", 1, "png")

        );
    }

    @Test
    public void obrisiteDirektorijum() throws Exception {
        String putanja = "/var/www/htdocs/masterRadKA163404143095/slike/privr/test/abcd";
        File f = new File(putanja);
        assertTrue(f.mkdirs());
        assertTrue(slike.obrisiteDirektorijum(putanja));
    }

    @Test
    public void obrisitePrivremeniDirektorijumZaKorisnika() throws Exception {
        String putanja = "/var/www/htdocs/masterRadKA163404143095/slike/privr/test/123";
        File f = new File(putanja);
        assertTrue(f.mkdirs());
        Slike slike1 = new Slike() {
            public boolean daLIKorisnikImaPravaDaSnimaUFolder(String email, long id) {
                return true;
            }

            public void izbaciteDirektorijumIKorisnikaIZMape() {

            }
        };
        assertTrue(slike1.obrisitePrivremeniDirektorijumZaKorisnika("test", 123L));
    }

    //@Test
    public void proveraDaLiLepoRadi() throws Exception {
        //nism bio siguran da li treba / ili \\/
        String s = "/var/www/htdocs/masterRad163404143095/slike/privr/akikrasic@gmail.com/1/1.jpg";
        String niz[] = s.split("/");
        for(int i=0;i<niz.length;i++){
            System.out.println(niz[i]);
        }
        System.out.println(niz[niz.length-2]);
        System.out.println(niz[niz.length-3]);
    }

    @Test
    public void putanjaDoSlikeZaBrisanje() throws Exception {
        String stringZaProveru = "/var/www/htdocs/masterRadKA163404143095/slike/privr/akikrasic@gmail.com/1/1.png";
        assertEquals(stringZaProveru, slike.putanjaDoSlikeZaBrisanje("akikrasic@gmail.com", 1, "1.png"));
    }


    @Test
    public void putanjaSlikeUPrivremenomFolderu() throws Exception {
        assertEquals("/var/www/htdocs/masterRadKA163404143095/slike/privr/akikrasic@gmail.com/1/1.png", slike.putanjaSlikeUPrivremenomFolderu("akikrasic@gmail.com", 1L, "1.png"));
    }
    @Test
    public void putanjaSlikeUPrivremenomFolderuGreska() throws Exception {
        assertNotEquals("/var/www/htdocs/masterRadKA163404143095/slike/privr/akikrasic@gmail.com/1/1.png",slike.putanjaSlikeUPrivremenomFolderu("akikrasic@gmail.com", 1L, "/1.png"));

    }

    @Test
    public void sigurnosnaProveraSlikaNull() throws Exception {
        List<String > l = new ArrayList<>();
        l.add(null);

       assertFalse( slike.sigurnosnaProveraSlika(l));
    }
    @Test
    public void sigurnosnaProveraSlikaPrazanString() throws Exception {
        List<String > l = new ArrayList<>();
        l.add("");

        assertFalse( slike.sigurnosnaProveraSlika(l));
    }
    @Test
    public void sigurnosnaProveraSlikaJpg() throws Exception {

        assertTrue(slike.sigurnosnaProveraEkstenzije("http:localhost/nesto/1.jpg"));
    }
    @Test
    public void sigurnosnaProveraSlikaJpeg() throws Exception {

        assertTrue(slike.sigurnosnaProveraEkstenzije("http:localhost/nesto/1.jpeg"));
    }
    @Test
    public void sigurnosnaProveraSlikaPng() throws Exception {

        assertTrue(slike.sigurnosnaProveraEkstenzije("http:localhost/nesto/1.png"));    }
    @Test
    public void sigurnosnaProveraSlikaNijednaEkstenzija() throws Exception {
        List<String > l = new ArrayList<>();
        l.add("http:localhost/nesto/1.pngj");
        assertFalse(slike.sigurnosnaProveraSlika(l));
    }
    @Test
    public void sigurnosnaProveraSlika() throws Exception {
        List<String > l = new ArrayList<>();
        l.add("http:localhost/masterRadKA163404143095/slike/privr/test/1/1.png");
        l.add("http:localhost/masterRadKA163404143095/slike/privr/test/1/2.jpg");
        l.add("http:localhost/masterRadKA163404143095/slike/privr/test/1/3.png");
        assertTrue(slike.sigurnosnaProveraSlika(l));
    }
    @Test
    public void sigurnosnaProveraSlikaGreska() throws Exception {
        List<String > l = new ArrayList<>();
        l.add("http:localhost/masterRadKA163404143095/slike/privr/test/1/1.png");
        l.add("http:localhost/masterRadKA163404143095/slike/privr/test/1/2.jpg");
        l.add("http:localhost/masterRadKA163404143095/slike/privr/test/1/3.jpg");//treba png
        assertFalse(slike.sigurnosnaProveraSlika(l));
    }

    @Test
    public void vratitePutanjuDoSlikaProizvoda() throws Exception {
        assertEquals("/var/www/htdocs/masterRadKA163404143095/slike/proizvod/-1/",slike.vratitePutanjuDoSlikaProizvoda(-1));
    }

    @Test
    public void prebaciteSlikeIzPrivremenogProizvodaUTrajni() throws Exception {
        String putanjaPocetna = slike.napravitePutanjuFolderaPrivremenogProizvoda("test", 1);
        List<String> sveSlike = slike.vratiteSveSlikeIzFoldera(putanjaPocetna);
        String putanjaZaTestiranjePrivr = slike.napravitePutanjuFolderaPrivremenogProizvoda("test", -1);
        assertTrue(new File(putanjaZaTestiranjePrivr).mkdirs());
        sveSlike.forEach(s->{
            File f = new File(putanjaPocetna+s);
            File destinacija = new File(putanjaZaTestiranjePrivr+s);
            try {
                FileInputStream in = new FileInputStream(f);
                byte[] bajtovi =new byte[(int)f.length()];
                in.read(bajtovi);
                in.close();
                assertNotNull(bajtovi);

               // destinacija.mkdirs();
                destinacija.createNewFile();
                FileOutputStream out = new FileOutputStream(destinacija);
                out.write(bajtovi);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        List<String> slikeIzTogNovogFoldera = slike.vratiteSveSlikeIzFoldera(putanjaZaTestiranjePrivr);
        assertEquals(slikeIzTogNovogFoldera.size(),3);
        String konacnaPutanja = slike.vratitePutanjuDoSlikaProizvoda(-1);
        assertTrue(slike.premestiteSlikeIzPrivrFolderaUFolderProizvoda("test", -1, -1));

        List<String> slikePremestene = slike.vratiteSveSlikeIzFoldera(konacnaPutanja);
        assertEquals(3, slikePremestene.size());
       assertTrue(slike.obrisiteDirektorijum(konacnaPutanja));
    }

    @Test
    public void vratiteSveSlikeProizvoda() throws Exception {
        assertTrue(slike.vratiteSveSlikeProizvoda(-2).size()==3);
    }

    //@Test
    public void sacuvajteSlikuProizvodaPaPosleObrisite() throws Exception {
        File f = new File("/var/www/htdocs/masterRadKA163404143095/slike/slikeZaSajt/ETF.png");
        assertTrue(f.exists());
        byte[] bajtovi = new byte[(int)f.length()];
        FileInputStream fi = new FileInputStream(f);
        fi.read(bajtovi);
        MultipartFile mf = new MockMultipartFile(f.getName(),bajtovi );
        assertEquals(f.getName(), mf.getName());
        int id=-3;
      // assertTrue(slike.sacuvajteSlikuProizvoda(mf, id));
       String putanja = "/var/www/htdocs/masterRadKA163404143095/slike/proizvod/-3/ETF.png";
        //assertTrue(slike.obrisiteSlikuProizvoda(putanja));
    }

    @Test
    public void ekstenzijaIspravan() throws Exception {
        String slika = "slika.png";
        assertEquals("png", slike.ekstenzija(slika));

    }
    @Test
    public void ekstenzijaNeispravan() throws Exception {
        String slika = "bezEkstenzije";
        assertEquals("", slike.ekstenzija(slika));
    }

    @Test
    public void vratitePunuPutanjuDoSlike() throws Exception {
        String putanjaDoSlikeZaProveru= "/var/www/htdocs/masterRadKA163404143095/slike/proizvod/-2/123.jpg";
        String generisanaPutanja = slike.vratitePunuPutanjuDoSlike(-2, 123, "jpg");
        assertEquals(putanjaDoSlikeZaProveru, generisanaPutanja);
    }

    @Test
    public void vratitePunuPutanjuDoSlikeBezEkstenzije() throws Exception {
        String putanjaDoSlikeZaProveru= "/var/www/htdocs/masterRadKA163404143095/slike/proizvod/-2/123.jpg";
        assertEquals(putanjaDoSlikeZaProveru, slike.vratitePunuPutanjuDoSlikeBezEkstenzije(-2, "123.jpg"));
    }
}