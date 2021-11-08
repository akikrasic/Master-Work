package srb.akikrasic.zahtevi.stari.svikorisnici;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.baza.BazaLogovanje;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.loginkorisnika.RezultatLogovanja;
import srb.akikrasic.token.Tokeni;

import java.util.Arrays;

/**
 * Created by aki on 8/7/17.
 */
//@RestController
public class Login {
    //menjano 25.11.2017.
    @Autowired
    private BazaLogovanje b;

    @Autowired
    private Tokeni radSaTokenima;

    @RequestMapping(value = "loginKorisnika", method = RequestMethod.GET)
    public RezultatLogovanja logovanjeKorisnika(
            @RequestParam String email,
            @RequestParam String sifra) {
        return b.logovanjeKorisnika(email, sifra.getBytes());

    }

    @RequestMapping(value = "ginLoDminaa", method = RequestMethod.GET)
    public RezultatLogovanja logovanjeAdmina(@RequestParam String korisnicko, @RequestParam String sifra) {
        RezultatLogovanja r = new RezultatLogovanja();

        if (korisnicko.equals("zmaj") && Arrays.equals(sifra.getBytes(), "fraSi".getBytes())) {
            r.setUspesno(true);
            r.setToken(radSaTokenima.adminUspesnoUlogovan());
        } else {
            r.setUspesno(false);
            r.setRazlogNeuspesnogLogovanja("Унели сте погрешно корисничко име или шифру!");


        }
        return r;
    }


    public Tokeni getRadSaTokenima() {
        return radSaTokenima;
    }

    public void setRadSaTokenima(Tokeni radSaTokenima) {
        this.radSaTokenima = radSaTokenima;
    }
}
