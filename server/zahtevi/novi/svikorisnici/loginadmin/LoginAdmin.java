package srb.akikrasic.zahtevi.novi.svikorisnici.loginadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.domen.Racun;
import srb.akikrasic.racuni.UpravljanjeRacunom;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.loginkorisnika.RezultatLogovanja;
import srb.akikrasic.token.Tokeni;

import java.util.Arrays;
import org.springframework.web.bind.annotation.*;
/**
 * Created by aki on 12/20/17.
 */
@RestController
public class LoginAdmin {

    @Autowired
    private Tokeni radSaTokenima;

    @Autowired
    private UpravljanjeRacunom upravljanjeRacunom;

    @RequestMapping(value = "ginLoDminaa", method = RequestMethod.GET)
    public RezultatLogovanja logovanjeAdmina(@RequestParam String korisnicko, @RequestParam String sifra) {

        if (korisnicko.equals("zmaj") && Arrays.equals(sifra.getBytes(), "fraSi".getBytes())) {
           return uspesnoLogovanje();
        } else {
            return neuspesnoLogovanje();
        }
    }
    private RezultatLogovanja uspesnoLogovanje(){
        RezultatLogovanjaAdmin r = new RezultatLogovanjaAdmin();
        r.setUspesno(true);
        r.setToken(radSaTokenima.adminUspesnoUlogovan());
        r.setStanjeRacunaAdmin(upravljanjeRacunom.vratiteStanjeRacuna());
        return r;
    }
    private RezultatLogovanja neuspesnoLogovanje(){
        RezultatLogovanja r = new RezultatLogovanja();
        r.setUspesno(false);
        r.setRazlogNeuspesnogLogovanja("Унели сте погрешно корисничко име или шифру!");
        return r;
    }

    public class RezultatLogovanjaAdmin extends RezultatLogovanja{
        private Racun stanjeRacunaAdmin;


        public Racun getStanjeRacunaAdmin() {
            return stanjeRacunaAdmin;
        }

        public void setStanjeRacunaAdmin(Racun stanjeRacunaAdmin) {
            this.stanjeRacunaAdmin = stanjeRacunaAdmin;
        }
    }

}
