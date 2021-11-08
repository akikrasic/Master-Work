package srb.akikrasic.zahtevi.novi.registrovanikorisnik.korisnik.izmenapodatakadobavljanje;

import org.springframework.beans.factory.annotation.Autowired;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.izmenapodatakadobavljanje.RezultatIzmenePodatakaDobavljanje;

/**
 * Created by aki on 12/17/17.
 */
@RestController
public class IzmenaPodatakaDobavljanjeRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;


    @RequestMapping(value = "/korisnikIzmenaPodatakaDobavljanje", method = RequestMethod.GET)
    public RezultatIzmenePodatakaDobavljanje izvrsiteZahtev(@RequestHeader("Authorization") String autorizacija) {

        String email = this.emailIzAutorizacije(autorizacija);
        return vratiteRezultat(korisniciRepozitorijum.pretragaKorisnikaPoEmailu(email));

    }
    public RezultatIzmenePodatakaDobavljanje vratiteRezultat(Korisnik k) {
        RezultatIzmenePodatakaDobavljanje r = new RezultatIzmenePodatakaDobavljanje();
        r.setDaLiJeURedu(true);
        r.setKorisnik(k);
        return r;
    }
}
