package srb.akikrasic.zahtevi.novi.registrovanikorisnik.poruke.vratiterazgovor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.baza.novipaket.baza.BazaPoruka;
import srb.akikrasic.domen.Poruka;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.poruke.vratiterazgovor.RezultatPoruke;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by aki on 12/19/17.
 */
@RestController
public class VratiteRazgovorRegistrovaniKorisnik extends Zahtev{

    @Autowired
    private BazaPoruka bazaPoruka;

    @RequestMapping(value="/registrovaniKorisnikVratiteRazgovor", method= RequestMethod.GET)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String email,
            @RequestParam String limit,
            @RequestParam String pomeraj
    ){
        if(email==null | limit==null||pomeraj==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        Integer limitI = this.konverzija.konverzijaInteger(limit);
        if(limitI==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        Integer pomerajI = this.konverzija.konverzijaInteger(pomeraj);
        if(pomerajI==null){
            return  dosloJeDoNeovlascenogPristupa();
        }
        if(limitI<0 || pomerajI<0){
            return dosloJeDoNeovlascenogPristupa();
        }

        String email2 = this.emailIzAutorizacije(autorizacija);
        return vratiteRezultat(bazaPoruka.vratitePoruke(email2, email, limitI, pomerajI));
    }
    private RezultatRegistrovaniKorisnik vratiteRezultat(List<Poruka> poruke){
        RezultatPoruke r = new RezultatPoruke();
        r.setDaLiJeURedu(true);
        r.setPoruke(poruke);
        return r;
    }

}
