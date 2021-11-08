package srb.akikrasic.zahtevi.novi.svikorisnici.loginkorisnika;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.baza.novipaket.baza.BazaLogovanje;
import srb.akikrasic.token.Tokeni;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.loginkorisnika.RezultatLogovanja;
import org.springframework.web.bind.annotation.*;
/**
 * Created by aki on 12/20/17.
 */
@RestController
public class LoginKorisnika {

    @Autowired
    private BazaLogovanje b;



    @RequestMapping(value = "loginKorisnika", method = RequestMethod.GET)
    public RezultatLogovanja logovanjeKorisnika(
            @RequestParam String email,
            @RequestParam String sifra) {
        return b.logovanjeKorisnika(email, sifra.getBytes());

    }
}
