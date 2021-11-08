package srb.akikrasic.zahtevi.novi.registrovanikorisnik.blogovi.unosbloga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.BlogoviRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.Map;

/**
 * Created by aki on 2/21/18.
 */
@RestController
public class UnosBloga extends Zahtev {

    @Autowired
    private BlogoviRepozitorijum blogoviRepozitorijum;

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @RequestMapping (value="/unesiteBlog", method=RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody Map<String, Object> podaci
    ){
        String naslov = (String) podaci.get("naslov");
        String tekst = (String) podaci.get("tekst");
        String email = this.emailIzAutorizacije(autorizacija);
        if(naslov==null || naslov.trim().equals("")||tekst ==null || tekst.trim().equals("")){
            return dosloJeDoNeovlascenogPristupa();
        }
        Korisnik k = korisniciRepozitorijum.pretragaKorisnikaPoEmailu(email);
        /*if(k==null){
            return dosloJeDoNeovlascenogPristupa();
        }*///suvisno, prosao je autorizaciju

        int id= blogoviRepozitorijum.snimiteBlog(naslov,tekst,k);
        return vratiteRezultat(id>0);
    }
    private RezultatRegistrovaniKorisnik vratiteRezultat(boolean rezultat){
        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        r.setDaLiJeURedu(rezultat);
        return r;
    }
}
