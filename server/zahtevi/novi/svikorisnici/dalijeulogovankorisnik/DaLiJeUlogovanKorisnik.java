package srb.akikrasic.zahtevi.novi.svikorisnici.dalijeulogovankorisnik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.token.Tokeni;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.dalijeulogovankorisnik.RezultatDaLiJeUlogovanKorisnik;

/**
 * Created by aki on 12/20/17.
 */
@RestController
public class DaLiJeUlogovanKorisnik {
    @Autowired
    private Tokeni radSaTokenima;

    @RequestMapping(value="/daLiJeUlogovanKorisnik", method= RequestMethod.GET)
    public RezultatDaLiJeUlogovanKorisnik daLiJeUlogovanKorisnik(@RequestParam String email,
                                                                 @RequestParam String token){
        return vratiteRezultat(radSaTokenima.daLiJeUlogovanKorisnik(email, token));
    }
    private RezultatDaLiJeUlogovanKorisnik vratiteRezultat(boolean ulogovan){
        RezultatDaLiJeUlogovanKorisnik r = new RezultatDaLiJeUlogovanKorisnik();
        r.setDaLiJeUlogovan(ulogovan);
        return r;
    }
}
