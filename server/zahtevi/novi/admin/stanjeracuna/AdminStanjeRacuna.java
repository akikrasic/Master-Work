package srb.akikrasic.zahtevi.novi.admin.stanjeracuna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import srb.akikrasic.baza.novipaket.baza.BazaRacunSajta;
import srb.akikrasic.domen.Racun;
import srb.akikrasic.racuni.UpravljanjeRacunom;
import srb.akikrasic.zahtevi.rezultati.novi.admin.stanjeracuna.RezultatStanjeRacunaAdmin;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;

/**
 * Created by aki on 11/24/17.
 */
@RestController
public class AdminStanjeRacuna {


    @Autowired
    private UpravljanjeRacunom upravljanjeRacunom;

    @RequestMapping(value="/adminVratiteStanjeRacuna", method= RequestMethod.GET)
    public RezultatAdmin izvrsiteZahtev(@RequestHeader("Authorization")String autorizacija){
        return vratiteRezultat(upravljanjeRacunom.vratiteStanjeRacuna());
    }

    private RezultatAdmin vratiteRezultat(Racun racun){
        RezultatStanjeRacunaAdmin r = new RezultatStanjeRacunaAdmin();
        r.setDaLiJeURedu(true);
        r.setRacun(racun);
        return r;
    }
}
