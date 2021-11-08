package srb.akikrasic.zahtevi.novi.registrovanikorisnik.blogovi.izmenabloga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import srb.akikrasic.baza.novipaket.repozitorijum.BlogoviRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.domen.Blog;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.Map;
import org.springframework.web.bind.annotation.*;
/**
 * Created by aki on 2/21/18.
 */
@RestController
public class IzmenaBloga extends Zahtev {
    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @Autowired
    private BlogoviRepozitorijum blogoviRepozitorijum;

    @RequestMapping(value="/izmeniteBlog", method= RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody Map<String, Object> podaci
    ){
        String naslov = (String) podaci.get("naslov");
        String tekst = (String) podaci.get("tekst");
        Integer id = this.konverzija.konverzijaInteger(podaci.get("id").toString());
        if(id==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String email = this.emailIzAutorizacije(autorizacija);
        if(naslov==null || naslov.trim().equals("")||tekst ==null || tekst.trim().equals("")){
            return dosloJeDoNeovlascenogPristupa();
        }
        Korisnik k = korisniciRepozitorijum.pretragaKorisnikaPoEmailu(email);
        Blog b = blogoviRepozitorijum.vratiteBlog(id);
        if(b==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        if(!b.getKorisnik().getEmail().equals(email)){
            return dosloJeDoNeovlascenogPristupa();
        }
        return vratiteRezultat(blogoviRepozitorijum.izmeniteBlog(id, naslov, tekst));
    }
    private RezultatRegistrovaniKorisnik vratiteRezultat(boolean rezultat){
        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        r.setDaLiJeURedu(rezultat);
        return r;
    }
}
