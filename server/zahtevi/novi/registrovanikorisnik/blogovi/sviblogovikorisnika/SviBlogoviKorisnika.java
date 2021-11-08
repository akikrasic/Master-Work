package srb.akikrasic.zahtevi.novi.registrovanikorisnik.blogovi.sviblogovikorisnika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.BlogoviRepozitorijum;
import srb.akikrasic.domen.Blog;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.blogovi.sviblogovikorisnika.RezultatSviBlogoviKorisnika;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.List;

/**
 * Created by aki on 2/21/18.
 */
@RestController
public class SviBlogoviKorisnika extends Zahtev {

    @Autowired
    private BlogoviRepozitorijum blogoviRepozitorijum;

    @RequestMapping(value="/sviBlogoviKorisnika", method=RequestMethod.GET)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String korisnikEmail,
            @RequestParam int pocetak,
            @RequestParam int pomeraj
    ){
        if(!pocetakPomerajuRedu(pocetak, pomeraj)){
            return dosloJeDoNeovlascenogPristupa();
        }
        if(korisnikEmail==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        return this.vratiteRezultat(blogoviRepozitorijum.vratiteBlogoveKorisnikaSaPomerajem(korisnikEmail, pocetak, pomeraj));
    }

    private RezultatRegistrovaniKorisnik vratiteRezultat(List<Blog> blogovi){
        RezultatSviBlogoviKorisnika r = new RezultatSviBlogoviKorisnika();
        r.setDaLiJeURedu(true);
        r.setBlogovi(blogovi);
        return r;
    }
}
