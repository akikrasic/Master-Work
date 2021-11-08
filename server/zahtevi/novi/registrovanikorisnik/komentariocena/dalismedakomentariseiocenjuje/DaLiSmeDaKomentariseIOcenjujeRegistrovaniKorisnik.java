package srb.akikrasic.zahtevi.novi.registrovanikorisnik.komentariocena.dalismedakomentariseiocenjuje;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.baza.novipaket.repozitorijum.KomentariIOceneRepozitorijum;
import srb.akikrasic.domen.KomentarIOcena;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.komentariocena.dalismedakomentariseiocenjuje.RezultatRegistrovaniKorisnikNoviDaLiSmeDaOcenjujeIKomentarise;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class DaLiSmeDaKomentariseIOcenjujeRegistrovaniKorisnik extends Zahtev {


    @Autowired
    private KomentariIOceneRepozitorijum komentariIOceneRepozitorijum;


    @RequestMapping(value = "/registrovaniKorisnikDaLiSmeDaKomentariseIOcenjuje", method= RequestMethod.GET)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String proizvodIdS
    ){
        if(proizvodIdS==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        Integer proizvodId  = this.konverzija.konverzijaInteger(proizvodIdS);
        if(proizvodId==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String email = this.emailIzAutorizacije(autorizacija);

        boolean rezultat = komentariIOceneRepozitorijum.daLiKorisnikSmeDaKomentariseProizvod(email, proizvodId);
        /*if(!rezultat){
            return rez.rezultatRezultatRegistrovaniKorisnikDaLiSmeDaOcenjujeIKomentariseProizvod(rezultat, null, null);
        }
        Komentar k = b.vratiteKomentar(email, proizvodId);
        Ocena o = b.vratiteOcenu(email, proizvodId);
        return rez.rezultatRezultatRegistrovaniKorisnikDaLiSmeDaOcenjujeIKomentariseProizvod(rezultat, k, o);
    */
        if(!rezultat){
            return vratiteRezultat(rezultat, null);
        }
        KomentarIOcena ko = komentariIOceneRepozitorijum.vratiteKomentarIOcenuZaKorisnikaIProizvodNovi(email, proizvodId);
        return vratiteRezultat(rezultat, ko);
    }

    private RezultatRegistrovaniKorisnik vratiteRezultat(boolean rezultat, KomentarIOcena komentarIOcena){
        RezultatRegistrovaniKorisnikNoviDaLiSmeDaOcenjujeIKomentarise r = new RezultatRegistrovaniKorisnikNoviDaLiSmeDaOcenjujeIKomentarise();
        r.setDaLiJeURedu(true);
        r.setDaLiImaPrava(rezultat);
        r.setKomentarIOcena(komentarIOcena);
        return r;
    }
}
