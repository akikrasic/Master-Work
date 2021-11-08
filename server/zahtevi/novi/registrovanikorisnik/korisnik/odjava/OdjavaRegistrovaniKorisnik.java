package srb.akikrasic.zahtevi.novi.registrovanikorisnik.korisnik.odjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.token.Tokeni;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.odjava.RezultatOdjaveRegistrovaniKorisnik;

/**
 * Created by aki on 12/17/17.
 */
@RestController
public class OdjavaRegistrovaniKorisnik extends Zahtev {
    @Autowired
    private Tokeni radSaTokenima;

    private RezultatOdjaveRegistrovaniKorisnik r = new RezultatOdjaveRegistrovaniKorisnik();


    @RequestMapping(value = "/korisnikOdjava", method = RequestMethod.GET)
    public RezultatOdjaveRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String email,
            @RequestParam String token) {

        return this.vratiteRezultat(radSaTokenima.korisnikOdjava(email, token));


    }

    public RezultatOdjaveRegistrovaniKorisnik vratiteRezultat(boolean uspesnaOdjava) {
        RezultatOdjaveRegistrovaniKorisnik r = new RezultatOdjaveRegistrovaniKorisnik();
        r.setOdjavaUspesna(uspesnaOdjava);
        r.setDaLiJeURedu(true);
        return r;
    }
}
