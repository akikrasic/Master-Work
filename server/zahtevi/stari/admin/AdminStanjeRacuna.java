package srb.akikrasic.zahtevi.stari.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.zahtevi.rezultati.stari.admin.AdminRezultat;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;

/**
 * Created by aki on 11/24/17.
 */
//@RestController
public class AdminStanjeRacuna {
    @Autowired
    private Baza b;
    @Autowired
    private AdminRezultat rez;
    @RequestMapping(value="/adminVratiteStanjeRacuna", method= RequestMethod.GET)
    public RezultatAdmin vratiteStanjeRacuna(@RequestHeader("Authorization")String autorizacija){
        return rez.vratiteStanjeRacuna(b.vratiteStanjeRacuna());
    }

}
