package srb.akikrasic.slike;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import srb.akikrasic.baza.Baza;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by aki on 8/26/17.
 */
@Component
public class Slike {
    private AtomicLong brojSlike;
    private AtomicLong brojProizvoda;
    private String putanjaZaCuvanjeSlikеKorisnika = "/var/www/htdocs/masterRadKA163404143095/slike/korisnik/";
    private String putanjaZacuvanjePrivremenihSlika = "/var/www/htdocs/masterRadKA163404143095/slike/privr/";
    private String putanjaZaCuvanjeSlikaProizvoda = "/var/www/htdocs/masterRadKA163404143095/slike/proizvod/";
    private Map<Long, String> sigurnosnaMapaFolderKorisnikKojiSmeDaSnimaUNjega;

    public Slike() {
        brojSlike = new AtomicLong();
        brojProizvoda = new AtomicLong();
        this.sigurnosnaMapaFolderKorisnikKojiSmeDaSnimaUNjega = new LinkedHashMap<>();
    }

    public String getPutanjaZaCuvanjeSlikeKorisnika() {
        return this.putanjaZaCuvanjeSlikеKorisnika;
    }

    public String getPutanjaZacuvanjePrivremenihSlika() {
        return putanjaZacuvanjePrivremenihSlika;
    }

    public String getPutanjaZaCuvanjeSlikaProizvoda() {
        return putanjaZaCuvanjeSlikaProizvoda;
    }

    @Autowired
    private Baza b;

    @PostConstruct
    public void inic() {
        brojSlike.set(b.ucitajteVrednostSlikeNaPocetku());
        brojProizvoda.set(b.ucitajteVrednostBrojaPrivremenogProizvodaNaPocetku());
    }

    @PreDestroy
    public void kraj() {
        b.sacuvajteVrednostSlikeNaKraju(brojSlike.get());
        b.sacuvajteVrednostBrojaPrivremenogPRoizvodaNaKraju(brojProizvoda.get());
    }

    public AtomicLong getBrojSlike() {
        return brojSlike;
    }

    //za lakse testiranje
    public void setBrojSlike(AtomicLong br) {
        this.brojSlike = br;
    }

    public long uzmiteBrojSledeceSlike() {
        return brojSlike.getAndAdd(1);
    }

    public boolean napraviteDirektorijumZaSmestajSlikaKorisnika(String email) {
        String putanja = new StringBuilder(this.putanjaZaCuvanjeSlikеKorisnika).append(email).append("/").toString();
        File snimanje = new File(putanja);
        return snimanje.mkdirs();
    }

    public boolean napraviteDirektorijumZaSmestajPrivremenihSlika(String email) {
        String putanja = new StringBuilder(this.putanjaZacuvanjePrivremenihSlika).append(email).append("/").toString();
        File snimanje = new File(putanja);
        return snimanje.mkdirs();
    }

    public void obrisiteSlikuAkoJeUneta(String direktorijum) {
        File f = new File(direktorijum);
        File[] fajlovi = f.listFiles();

        for (int i = 0; i < fajlovi.length; i++) {
            if (fajlovi[i].getName().startsWith("slikaProfila")) {
                fajlovi[i].delete();
            }
        }

    }


    public boolean sacuvajteSlikuKorisnika(MultipartFile f, String email, String ekstenzija) throws IOException, FileNotFoundException {

        String putanjaZaSnimanje = new StringBuilder(this.putanjaZaCuvanjeSlikеKorisnika).append(email).toString();
        this.obrisiteSlikuAkoJeUneta(putanjaZaSnimanje);
        File slika = new File(new StringBuilder(putanjaZaSnimanje).append("/slikaProfila.").append(ekstenzija).toString());
        FileOutputStream out = new FileOutputStream(slika);
        out.write(f.getBytes());
        out.flush();
        out.close();
       /* File lokacijaZaSnimanje
        System.out.println(snimanje.getAbsolutePath());
        File f1 = new File(snimanje+"/slikaProfila."+extenzija);

        FileOutputStream out = new FileOutputStream(f1);
        out.write(f.getBytes());
        out.flush();
        out.close();
        rpsp.setDaLiJeURedu(true);*/
        return true;
    }

    public String vratiteEkstenzijuSlikeProfila(String email) {
        String direktorijum = new StringBuilder(this.putanjaZaCuvanjeSlikеKorisnika).append(email).append("/").toString();
        File[] fajlovi = new File(direktorijum).listFiles();
        if (fajlovi == null || fajlovi.length == 0) return "";
        return fajlovi[0].getName().split("\\.")[1];
    }


    public long uzmiteBrojSledecegDirektorijuma() {
        return this.brojProizvoda.getAndAdd(1);
    }

    public boolean napraviteDirektorijumZaKorisnikaKojiUnosiNoviProizvod(String email, long id) {
        String putanja = new StringBuilder(this.putanjaZacuvanjePrivremenihSlika).append(email).append("/").append(id).append("/").toString();
        File f = new File(putanja);
        return f.mkdirs();
    }

    public long vratiteIDPrivremenogFolderaIKreirajteGa(String email) {
        long id = this.uzmiteBrojSledecegDirektorijuma();
        boolean rez = this.napraviteDirektorijumZaKorisnikaKojiUnosiNoviProizvod(email, id);
        if (!rez) return -1;
        this.sigurnosnaMapaFolderKorisnikKojiSmeDaSnimaUNjega.put(id, email);
        return id;
    }

    public boolean daLIKorisnikImaPravaDaSnimaUFolder(String email, long id) {
        String emailIzMape = this.sigurnosnaMapaFolderKorisnikKojiSmeDaSnimaUNjega.get(id);
        if (emailIzMape == null) return false;
        return email.equals(emailIzMape);
    }

    public String napravitePutanjuFolderaPrivremenogProizvoda(String email, long id) {
        return new StringBuilder(this.putanjaZacuvanjePrivremenihSlika).append(email).append("/").append(id).append("/").toString();
    }


    public String putanjeNoveSlikeSaNovimImenom(String email, long id, String ekstenzija) {

        return new StringBuilder(this.napravitePutanjuFolderaPrivremenogProizvoda(email, id)).append(this.uzmiteBrojSledeceSlike()).append(".").append(ekstenzija).toString();
    }

    public String putanjaSlikeUPrivremenomFolderu(String email, long id, String naziv) {
        return new StringBuilder(this.napravitePutanjuFolderaPrivremenogProizvoda(email, id)).append(naziv).toString();
    }

    public boolean sacuvajteSlikuProizvodaUPrivremenomFolderu(MultipartFile mf, String email, long idPrivremenogFoldera) {
        String imeSlike = mf.getOriginalFilename();
        String ekstenzija = this.ekstenzija(imeSlike);//ovako je bezbednije onako ne bi naslo pa bi iskocila greska
        if (ekstenzija.equals("jpeg") || ekstenzija.equals("png") || ekstenzija.equals("jpg")) {
            String putanjaZaSnimanje = putanjeNoveSlikeSaNovimImenom(email, idPrivremenogFoldera, ekstenzija);
            File f = new File(putanjaZaSnimanje);
            try (FileOutputStream out = new FileOutputStream(f)) {
                out.write(mf.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                return false;
            }

            return true;
        }

        return false;
    }


    public List<String> vratiteSveSlikeIzFoldera(String putanja) {
        List<String> rez = new ArrayList<>();
        TreeMap<Long, String> mapaZaSortiranje = new TreeMap<>();

        File f = new File(putanja);
        File[] fajlovi = f.listFiles();
        Arrays.stream(fajlovi).filter(fajl -> (!fajl.getName().equals(".") && (!fajl.getName().equals("..")))).forEach(fajl -> {
            String ime = fajl.getName();
            Long id = Long.parseLong(ime.split("\\.")[0]);
            mapaZaSortiranje.put(id, ime);
        });
        rez.addAll(mapaZaSortiranje.values());
        return rez;
    }

    public List<String> vratiteSveSlikeIZPrivremenogFolderaProizvoda(String email, long id) {
        String putanja = this.napravitePutanjuFolderaPrivremenogProizvoda(email, id);
        return this.vratiteSveSlikeIzFoldera(putanja);
    }

    public List<String> vratiteSveSlikeProizvoda(int idProizvoda) {
        String putanja = this.vratitePutanjuDoSlikaProizvoda(idProizvoda);
        return this.vratiteSveSlikeIzFoldera(putanja);
    }

    public boolean obrisiteDirektorijum(String putanja) {
        File f = new File(putanja);
        File[] fajlovi = f.listFiles();
        Arrays.stream(fajlovi).forEach(fajl -> {
            fajl.delete();
        });
        return f.delete();
    }

    public void izbaciteDirektorijumIKorisnikaIZMape(long id) {
        this.sigurnosnaMapaFolderKorisnikKojiSmeDaSnimaUNjega.remove(id);
    }

    public boolean obrisitePrivremeniDirektorijumZaKorisnika(String email, long id) {
        String putanja = this.napravitePutanjuFolderaPrivremenogProizvoda(email, id);
        if (!daLIKorisnikImaPravaDaSnimaUFolder(email, id)) {
            return false;
        }
        this.izbaciteDirektorijumIKorisnikaIZMape(id);
        return this.obrisiteDirektorijum(putanja);

    }

    public String putanjaDoSlikeZaBrisanje(String email, long id, String nazivSlike) {
        return new StringBuilder(this.putanjaZacuvanjePrivremenihSlika)
                .append(email)
                .append("/")
                .append(id)
                .append("/")
                .append(nazivSlike).toString();
    }

    public boolean obrisiteSlikuIzPrivremenogFoldera(String putanja) {
        File f = new File(putanja);
        return f.delete();
    }

    public boolean sigurnosnaProveraEkstenzije(String nazivSlike) {
        if (!nazivSlike.endsWith("jpg") && !nazivSlike.endsWith("jpeg") && !nazivSlike.endsWith("png")) {
            return false;
        }
        return true;
    }

    public boolean sigurnosnaProveraSlika(List<String> slikeIzZahteva) {

        for (int i = 0; i < slikeIzZahteva.size(); i++) {
            String s = slikeIzZahteva.get(i);
            if (s == null || s == "") return false;
            String[] niz = s.split("/");
            String nazivSlike = niz[niz.length - 1];
            if (!this.sigurnosnaProveraEkstenzije(nazivSlike)) return false;
            String putanja = this.putanjaSlikeUPrivremenomFolderu(niz[niz.length - 3], Long.parseLong(niz[niz.length - 2]), nazivSlike);
            File f = new File(putanja);
            if (!f.exists()) return false;
        }
        return true;
    }


    public String vratitePutanjuDoSlikaProizvoda(int proizvodId) {
        return new StringBuilder(this.putanjaZaCuvanjeSlikaProizvoda).append(proizvodId).append("/").toString();

    }

    public boolean premestiteSlikeIzPrivrFolderaUFolderProizvoda(String email, long idPrivremenogFoldera, int idProizvodaUBazi) {
        String putanjaPrivremenogFoldera = this.napravitePutanjuFolderaPrivremenogProizvoda(email, idPrivremenogFoldera);
        String putanjaDestinacija = this.vratitePutanjuDoSlikaProizvoda(idProizvodaUBazi);
        File destinacijaFolder = new File(putanjaDestinacija);
        if (!destinacijaFolder.mkdirs()) {
            return false;
        }
        List<String> slikeZaKopiranjeIbrisanje = this.vratiteSveSlikeIzFoldera(putanjaPrivremenogFoldera);
        for (String s : slikeZaKopiranjeIbrisanje) {
            File slikaUPrivr = new File(putanjaPrivremenogFoldera + s);
            File slikaUKonacnom = new File(putanjaDestinacija + s);//moze + jednom to je jedan StringBuilder
            byte[] bajtovi = new byte[(int) slikaUPrivr.length()];
            try {
                FileInputStream in = new FileInputStream(slikaUPrivr);
                in.read(bajtovi);
                in.close();
                if (!slikaUKonacnom.createNewFile()) return false;
                FileOutputStream out = new FileOutputStream(slikaUKonacnom);
                out.write(bajtovi);
                out.close();
                if (!slikaUPrivr.delete()) return false;
            } catch (Exception e) {
                return false;
            }
        }
        File folderZaBrisanjePrivr = new File(putanjaPrivremenogFoldera);
        if (!folderZaBrisanjePrivr.delete()) return false;
        return true;
    }
    /*
    public boolean premestiteSlikeIzPrivremenogFolderaUPravi(String email, Long privremeniFolderId, int proizvodId ){
        String putanjaDoPrivremenogFoldera
    }
    public List<String> vratiteSveSlikeProizvoda(int proizvodId){

    }
*/


 public String vratitePunuPutanjuDoSlike(int proizvodId, long brojSlike, String ekstenzija){
    return new StringBuilder(this.putanjaZaCuvanjeSlikaProizvoda)
            .append(proizvodId)
            .append("/")
            .append(brojSlike)
            .append(".")
            .append(ekstenzija).toString();
 }


    public String sacuvajteSlikuProizvoda(MultipartFile slika, int proizvodId, String ekstenzija) {
        long brojSlikeGenerisan= this.uzmiteBrojSledeceSlike();
        String punaPutanjaDoSlike = this.vratitePunuPutanjuDoSlike(proizvodId,brojSlikeGenerisan, ekstenzija );
        String imeSlike = new StringBuilder().append(brojSlikeGenerisan).append(".").append(ekstenzija).toString();
        File f = new File(punaPutanjaDoSlike);
           try {
               FileOutputStream out = new FileOutputStream(f);
               out.write(slika.getBytes());
               out.flush();
               out.close();
           }
           catch(Exception e){
               return null;
           }
        return imeSlike;

    }
    public String vratitePunuPutanjuDoSlikeBezEkstenzije(int proizvodId, String imeSlike){
        return new StringBuilder(this.putanjaZaCuvanjeSlikaProizvoda)
                .append(proizvodId)
                .append("/")
                .append(imeSlike)
                .toString();
    }

    public boolean obrisiteSlikuProizvoda(int proizvodId,String nazivSlike) {
    String putanja = this.vratitePunuPutanjuDoSlikeBezEkstenzije(proizvodId, nazivSlike);
     File f = new File(putanja);
        return f.delete();
    }

    public String ekstenzija(String nazivSlike) {
        try {
            return nazivSlike.split("\\.")[1];
        } catch (Exception e) {
            return "";
        }
    }

}
