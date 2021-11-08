package srb.akikrasic.zahtevi.novi.registrovanikorisnik.narudzbenica.vracanjenarudzbenicakupcu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import srb.akikrasic.baza.novipaket.repozitorijum.NarudzbeniceRepozitorijum;
import srb.akikrasic.domen.Narudzbenica;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.narudzbenica.vracanjenarudzbenicekupcu.RezultatNarudzbeniceKupca;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

import java.util.List;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class VracanjeNarudzbeniceKupcuRegistrovaniKorisnik extends Zahtev {

    @Autowired
    protected NarudzbeniceRepozitorijum narudzbeniceRepozitorijum;


    @RequestMapping(value = "/vratiteNarudzbeniceKupcu", method = RequestMethod.GET)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija

    ) {
        String email = this.emailIzAutorizacije(autorizacija);
        return vratiteRezultat(narudzbeniceRepozitorijum.vratiteNarudzbeniceKupcu(email));
    }

    protected RezultatNarudzbeniceKupca vratiteRezultat(List<Narudzbenica> l) {
        RezultatNarudzbeniceKupca r = new RezultatNarudzbeniceKupca();
        r.setDaLiJeURedu(true);
        r.setNarudzbenice(l);
        r.setDuzina(l.size());
        return r;
    }
}
