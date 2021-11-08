package srb.akikrasic.zahtevi.novi.svikorisnici.pretragakorisnikapoemailu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.domen.Korisnik;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.pretragakorisnikapoemailu.RezultatPretrageKorisnikaPoEmailu;

/**
 * Created by aki on 12/20/17.
 */
@RestController
public class PretragaKorisnikaPoEmailu {
    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @RequestMapping(value="/korisnikPoEmailu", method= RequestMethod.GET)
    public RezultatPretrageKorisnikaPoEmailu pretragaKorisnikaPoEmailu(@RequestParam String email){

        Korisnik k = korisniciRepozitorijum.pretragaKorisnikaPoEmailu(email);
        if(k==null) {
            return vratiteRezultatNijeNadjen();
        }
        else{
            return vratiteRezultatPronadjen(k);
        }
    }
    private RezultatPretrageKorisnikaPoEmailu vratiteRezultatPronadjen(Korisnik k){
        RezultatPretrageKorisnikaPoEmailu r = new RezultatPretrageKorisnikaPoEmailu();
        r.setPronadjen(true);
        r.setKorisnik(k);
        return r;
    }

    private RezultatPretrageKorisnikaPoEmailu vratiteRezultatNijeNadjen(){
        RezultatPretrageKorisnikaPoEmailu r = new RezultatPretrageKorisnikaPoEmailu();
        r.setPronadjen(false);
        return r;
    }

}
