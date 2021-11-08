package srb.akikrasic.zahtevi.novi.registrovanikorisnik.korisnik.stanjeracuna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.racuni.UpravljanjeRacunom;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.stanjeracuna.RezultatStanjeRacunaNaPocetkuRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import org.springframework.web.bind.annotation.*;
/**
 * Created by aki on 12/19/17.
 */
@RestController
public class PocetnoStanjeRacuneRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private UpravljanjeRacunom upravljanjeRacunom;

    @RequestMapping(value="/registrovaniKorisnikVratiteStanjeRacuna", method= RequestMethod.GET)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String email
    ) {

        if(email ==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        return vratiteRezultat(upravljanjeRacunom.vratiteStanjeRacunaKorisnika(email));
    }
    private RezultatRegistrovaniKorisnik vratiteRezultat(double stanje){
        RezultatStanjeRacunaNaPocetkuRegistrovaniKorisnik r = new RezultatStanjeRacunaNaPocetkuRegistrovaniKorisnik();
        r.setDaLiJeURedu(true);
        r.setStanje(stanje);
        return r;
    }
}
