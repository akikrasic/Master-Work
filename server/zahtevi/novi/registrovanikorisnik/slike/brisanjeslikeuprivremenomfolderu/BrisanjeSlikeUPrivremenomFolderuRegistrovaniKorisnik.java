package srb.akikrasic.zahtevi.novi.registrovanikorisnik.slike.brisanjeslikeuprivremenomfolderu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.brisanjeslikeuprivremenomfolderu.RezultatBrisanjaSlikeUPrivremenojMemorijiRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

/**
 * Created by aki on 12/19/17.
 */
@RestController
public class BrisanjeSlikeUPrivremenomFolderuRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private Slike slike;


    @RequestMapping(value="/korisnikBriseSlikuPrivremeniFolder", method = RequestMethod.DELETE)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String putanja){
        String[] niz = putanja.split("/");
        String email = niz[niz.length-3];
        long id = Long.parseLong(niz[niz.length-2]);
        String nazivSlike=niz[niz.length-1];
        String emailIzAutorizacije = this.emailIzAutorizacije(autorizacija);
        if(!emailIzAutorizacije.equals(email)){
            return dosloJeDoNeovlascenogPristupa();
        }
        if(!slike.daLIKorisnikImaPravaDaSnimaUFolder(email, id)){
            return dosloJeDoNeovlascenogPristupa();
        }
        String putanjaDoSameSlike = slike.putanjaDoSlikeZaBrisanje(email, id, nazivSlike);
        return vratiteRezultat(slike.obrisiteSlikuIzPrivremenogFoldera(putanjaDoSameSlike));
    }
    private RezultatBrisanjaSlikeUPrivremenojMemorijiRegistrovaniKorisnik vratiteRezultat(boolean rezultat){
        RezultatBrisanjaSlikeUPrivremenojMemorijiRegistrovaniKorisnik r = new RezultatBrisanjaSlikeUPrivremenojMemorijiRegistrovaniKorisnik();
        r.setDaLiJeURedu(rezultat);
        return r;
    }

}
