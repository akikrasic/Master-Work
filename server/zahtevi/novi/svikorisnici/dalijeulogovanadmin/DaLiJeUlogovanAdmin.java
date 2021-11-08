package srb.akikrasic.zahtevi.novi.svikorisnici.dalijeulogovanadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.token.Tokeni;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.dalijeulogovankorisnik.RezultatDaLiJeUlogovanKorisnik;
import org.springframework.web.bind.annotation.*;

/**
 * Created by aki on 12/20/17.
 */
@RestController
public class DaLiJeUlogovanAdmin {
    @Autowired
    private Tokeni radSaTokenima;
    @RequestMapping(value="/daLiJeUlogovanAdmin", method= RequestMethod.GET)
    public RezultatDaLiJeUlogovanKorisnik daLiJeUlogovanAdmin(
            @RequestParam String token){

        return vratiteRezultat(radSaTokenima.daLiJeUlogovanAdmin(token));

    }
    private RezultatDaLiJeUlogovanKorisnik vratiteRezultat(boolean ulogovan){
        RezultatDaLiJeUlogovanKorisnik r = new RezultatDaLiJeUlogovanKorisnik();
        r.setDaLiJeUlogovan(ulogovan);
        return r;
    }

}
