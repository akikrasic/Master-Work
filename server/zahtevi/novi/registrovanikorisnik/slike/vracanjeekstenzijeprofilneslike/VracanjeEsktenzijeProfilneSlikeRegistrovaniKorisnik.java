package srb.akikrasic.zahtevi.novi.registrovanikorisnik.slike.vracanjeekstenzijeprofilneslike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.vracanjeekstenzijeprofilneslike.RezultatEkstenzijeProfilneSlikeRegistrovaniKorisnik;

/**
 * Created by aki on 12/19/17.
 */
@RestController
public class VracanjeEsktenzijeProfilneSlikeRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private Slike slike;

    @RequestMapping(value="/korisnikVratiteEkstenzijuProfilneSlike", method=RequestMethod.GET)
    public RezultatEkstenzijeProfilneSlikeRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija

    ){

        String email = this.emailIzAutorizacije(autorizacija);
        return vratiteRezultat(slike.vratiteEkstenzijuSlikeProfila(email));

    }
    private RezultatEkstenzijeProfilneSlikeRegistrovaniKorisnik vratiteRezultat(String ekstenzija){
        RezultatEkstenzijeProfilneSlikeRegistrovaniKorisnik rez = new RezultatEkstenzijeProfilneSlikeRegistrovaniKorisnik();
        rez.setEkstenzija(ekstenzija);
        rez.setDaLiJeURedu(true);
        return rez;
    }
}
