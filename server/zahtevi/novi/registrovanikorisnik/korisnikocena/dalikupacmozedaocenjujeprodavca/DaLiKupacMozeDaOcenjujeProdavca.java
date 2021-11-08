package srb.akikrasic.zahtevi.novi.registrovanikorisnik.korisnikocena.dalikupacmozedaocenjujeprodavca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisnikOcenaRepozitorijum;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.Map;

/**
 * Created by aki on 2/23/18.
 */
@RestController
public class DaLiKupacMozeDaOcenjujeProdavca extends Zahtev {

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @Autowired
    private KorisnikOcenaRepozitorijum korisnikOcenaRepozitorijum;

    @RequestMapping(value="/daLiKupacMozeDaOcenjujeProdavca", method=RequestMethod.GET)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String kupacEmail,
            @RequestParam String prodavacEmail
    ){



        if(kupacEmail==null||prodavacEmail==null){
            return dosloJeDoNeovlascenogPristupa();
        }

        Korisnik kupac = korisniciRepozitorijum.pretragaKorisnikaPoEmailu(kupacEmail);
        Korisnik prodavac = korisniciRepozitorijum.pretragaKorisnikaPoEmailu(prodavacEmail);
        if(kupac==null||prodavac==null){
            return dosloJeDoNeovlascenogPristupa();

        }
        if(!kupacEmail.equals(this.emailIzAutorizacije(autorizacija))){
            return dosloJeDoNeovlascenogPristupa();
            //da ne moze da se lako hakuje
        }


        return vratiteRezultat(korisnikOcenaRepozitorijum.daLiKupacMozeDaOcenjujeProdavca(kupacEmail, prodavacEmail));
    }


    private RezultatRegistrovaniKorisnik vratiteRezultat(boolean rezultat){
        RezultatRegistrovaniKorisnik r= new RezultatRegistrovaniKorisnik();
        r.setDaLiJeURedu(rezultat);

        return r;
    }
}
