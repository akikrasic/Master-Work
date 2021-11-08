package srb.akikrasic.zahtevi.stari.registrovanikorisnik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.*;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.brisanjeslikeproizvoda.RezultatBrisanjaSlikeProizvodaRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.brisanjeslikeuprivremenomfolderu.RezultatBrisanjaSlikeUPrivremenojMemorijiRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.izmenaslikeproizvoda.RezultatDodavanjaSlikeProizvodaRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.preuzimanjebrojaprivremenogfolderaikategorije.RezultatBrojPrivremenogFolderaIKategorijeRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.unosslikeprofila.RezultatPostavljanjaSlikeProfila;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.unosslikeproizvoda.RezultatPostavljanjaSlikeProizvodaRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.vracanjeekstenzijeprofilneslike.RezultatEkstenzijeProfilneSlikeRegistrovaniKorisnik;

import java.io.IOException;
import java.util.List;

/**
 * Created by aki on 8/27/17.
 */
//@RestController
public class RegistrovaniKorisnikUploadSlike {

    @Autowired
    private Baza b;

    @Autowired
    private Slike slike;
    @Autowired
    private GenerisanjeTokena radSaGenerisanjemTokena;
    private String emailIzAutorizacije(String autorizacija) {
        return (radSaGenerisanjemTokena.dekodujteURLFormat(autorizacija)).split(":")[0];
    }

    @RequestMapping(value="/korisnikUnosSlikeProfila", method=RequestMethod.POST)
    public RezultatPostavljanjaSlikeProfila uploadSlikeProfila(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam("slikaProfila") MultipartFile f ) throws IOException {


        RezultatPostavljanjaSlikeProfila rpsp = new RezultatPostavljanjaSlikeProfila();

        String email = this.emailIzAutorizacije(autorizacija);
        String extenzija = f.getOriginalFilename().split("\\.")[1];
        if(extenzija.equals("jpeg")||extenzija.equals("jpg")||extenzija.equals("png")){
            rpsp.setDaLiJeURedu(slike.sacuvajteSlikuKorisnika(f, email, extenzija));
            return rpsp;
        }

        rpsp.setDaLiJeURedu(false);
        return rpsp;
    }
    @RequestMapping(value="/korisnikVratiteEkstenzijuProfilneSlike", method=RequestMethod.GET)
    public RezultatEkstenzijeProfilneSlikeRegistrovaniKorisnik ekstenzijaProfilneSlike(
            @RequestHeader("Authorization") String autorizacija

    ){
        RezultatEkstenzijeProfilneSlikeRegistrovaniKorisnik rez = new RezultatEkstenzijeProfilneSlikeRegistrovaniKorisnik();
        rez.setDaLiJeURedu(true);
        String email = this.emailIzAutorizacije(autorizacija);
        rez.setEkstenzija(slike.vratiteEkstenzijuSlikeProfila(email));
        return rez;
    }
//u sustini jedna metoda ne treba da radi dva posla, ali posto saljemo jedan zahtev, onda je najbolje da u njemu sakupimo sve sto je potrebno
    //a potrebne su i kategrije i id slike
    @RequestMapping(value="/korisnikPreuzmiteBrojPrivremenogFolderaZaUploadSlikaIKategorije", method=RequestMethod.GET)
    public RezultatBrojPrivremenogFolderaIKategorijeRegistrovaniKorisnik preuzmiteBroi(
            @RequestHeader("Authorization")String autorizacija

    ){
        String email = this.emailIzAutorizacije(autorizacija);
        RezultatBrojPrivremenogFolderaIKategorijeRegistrovaniKorisnik r = new RezultatBrojPrivremenogFolderaIKategorijeRegistrovaniKorisnik();
        long id = slike.vratiteIDPrivremenogFolderaIKreirajteGa(email);
        if(id==-1){
            r.setDaLiJeURedu(false);
            return r;
        }
        r.setDaLiJeURedu(true);
        r.setIdPrivremenogFoldera(id);
        r.setKategorije(b.vratiteSveKategorije());
        return r;

    }
    @RequestMapping(value="korisnikUnosSlikeProizvoda",method=RequestMethod.POST)
    public RezultatPostavljanjaSlikeProizvodaRegistrovaniKorisnik postaviteSlikuProizvoda(
        @RequestHeader("Authorization") String autorizacija,
        @RequestParam("slika") MultipartFile f,
        @RequestParam("idPrivremenogFoldera") long idPrivremenogFoldera

    ){
        RezultatPostavljanjaSlikeProizvodaRegistrovaniKorisnik r = new RezultatPostavljanjaSlikeProizvodaRegistrovaniKorisnik();
        String email = this.emailIzAutorizacije(autorizacija);
        if(!slike.daLIKorisnikImaPravaDaSnimaUFolder(email, idPrivremenogFoldera)){
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        if(!this.slike.sacuvajteSlikuProizvodaUPrivremenomFolderu(f, email, idPrivremenogFoldera)){
            r.setDaLiJeURedu(false);
            return r;
        }
        List<String> slikeIzPrivrFoldera =slike.vratiteSveSlikeIZPrivremenogFolderaProizvoda(email, idPrivremenogFoldera);
        r.setSlike(slikeIzPrivrFoldera);
        r.setDaLiJeURedu(true);
        return r;
    }

    @RequestMapping(value="/obrisitePrivremeniFolder", method=RequestMethod.DELETE)
    public RezultatRegistrovaniKorisnik obrisitePrivremeniFolder(@RequestHeader("Authorization") String autorizacija,
                                                                 @RequestParam long idDirektorijumaZaBrisanje){
            RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
            String email = this.emailIzAutorizacije(autorizacija);
           if(!slike.obrisitePrivremeniDirektorijumZaKorisnika(email, idDirektorijumaZaBrisanje)){
               r.dosloJeDoNeovlascenogPristupa();
               return r;
           }
           r.setDaLiJeURedu(true);
            return r;
    }

    @RequestMapping(value="/korisnikBriseSlikuPrivremeniFolder", method = RequestMethod.DELETE)
    public RezultatBrisanjaSlikeUPrivremenojMemorijiRegistrovaniKorisnik obrisiteSlikuUPrivrMem(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String putanja){
        RezultatBrisanjaSlikeUPrivremenojMemorijiRegistrovaniKorisnik r = new RezultatBrisanjaSlikeUPrivremenojMemorijiRegistrovaniKorisnik();
        String[] niz = putanja.split("/");
        String email = niz[niz.length-3];
        long id = Long.parseLong(niz[niz.length-2]);
        String nazivSlike=niz[niz.length-1];
        String emailIZAutorizacije = this.emailIzAutorizacije(autorizacija);
        if(!emailIZAutorizacije.equals(email)){
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        if(!slike.daLIKorisnikImaPravaDaSnimaUFolder(email, id)){
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        String putanjaDoSameSlike = slike.putanjaDoSlikeZaBrisanje(email, id, nazivSlike);
        r.setDaLiJeURedu(slike.obrisiteSlikuIzPrivremenogFoldera(putanjaDoSameSlike));
        return r;


    }

    @RequestMapping(value="/korisnikIzmenaSlikeProizvoda", method=RequestMethod.POST)
    public RezultatDodavanjaSlikeProizvodaRegistrovaniKorisnik korisnikIzmenaSlike(
        @RequestHeader("Authorization")String autorizacija,
        @RequestParam("slika") MultipartFile slika,
        @RequestParam("proizvodId") int proizvodId

    ){
        RezultatDodavanjaSlikeProizvodaRegistrovaniKorisnik r = new RezultatDodavanjaSlikeProizvodaRegistrovaniKorisnik();
        if(slika==null){
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        String ekstenzija = slike.ekstenzija(slika.getOriginalFilename());
        if(ekstenzija.equals("png")||ekstenzija.equals("jpeg")||ekstenzija.equals("jpg")) {
            Proizvod p = b.vratiteProizvodPoId(proizvodId);
            String email = this.emailIzAutorizacije(autorizacija);
            if(!email.equals(p.getKorisnik().getEmail())){
                r.dosloJeDoNeovlascenogPristupa();
                return r;
            }

            String imeSlike= slike.sacuvajteSlikuProizvoda(slika,proizvodId, ekstenzija );
            if(imeSlike==null){
                r.setDaLiJeURedu(false);
                return r;
            }
            r.setDaLiJeURedu(true);
            r.setImeUneteSlike(imeSlike);
        }
        else{
            r.setDaLiJeURedu(false);
            //nije u pitanju neovlasceni pristup, jer treba da se nastavi sa radom
            //ali ne sme da bude nesto sto nema neku od tri navedene ekstenzije
        }


        return r;

    }
    @RequestMapping(value="/korisnikBrisanjeSlikeProizvoda", method=RequestMethod.DELETE)
    public RezultatBrisanjaSlikeProizvodaRegistrovaniKorisnik obrisiteSlikuKorisnika(
            @RequestHeader("Authorization")String autorizacija,
            @RequestParam("putanja") String putanja

    ){
        RezultatBrisanjaSlikeProizvodaRegistrovaniKorisnik r = new RezultatBrisanjaSlikeProizvodaRegistrovaniKorisnik();
        //tu se sad proverava smeje li zmijata da brise onda da li je ovo null i jos kvo sdve trebe
        if(putanja==null||putanja.equals("")){
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        String informacije[] = putanja.split("/");
        if(informacije.length<2){
            r.dosloJeDoNeovlascenogPristupa();
            return r;

        }
        String emailIzAutorizacije = this.emailIzAutorizacije(autorizacija);
        String proizvodIdString = informacije[informacije.length-2];
        int proizvodId=0;
        try{
            proizvodId=Integer.parseInt(proizvodIdString);
        }
        catch(Exception e){
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        Proizvod p = b.vratiteProizvodPoId(proizvodId);
        if(!emailIzAutorizacije.equals(p.getKorisnik().getEmail())){
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        r.setDaLiJeURedu(slike.obrisiteSlikuProizvoda(proizvodId,informacije[informacije.length-1] ));
        return r;

    }


}
