package srb.akikrasic.zahtevi.novi.registrovanikorisnik.korisnikocena.izmeniteocenukorisnika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisnikOcenaRepozitorijum;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnikocena.unesiteocenukorisnika.RezultatUnesiteOcenuKorisnika;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.Map;

/**
 * Created by aki on 2/23/18.
 */
@RestController
public class IzmeniteOcenuKorisnika extends Zahtev {

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @Autowired
    private KorisnikOcenaRepozitorijum korisnikOcenaRepozitorijum;

    @RequestMapping(value="/izmeniteOcenuKorisnika", method=RequestMethod.PUT)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody Map<String, Object> podaci
    ){

        Object idO = podaci.get("id");
        if(idO==null){
            return  dosloJeDoNeovlascenogPristupa();
        }
        Integer id = this.konverzija.konverzijaInteger(idO.toString());
        if(id==null){
            return dosloJeDoNeovlascenogPristupa();
        }

        Object ocenaO=podaci.get("ocena");
        if(ocenaO==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        Integer ocena = this.konverzija.konverzijaInteger(ocenaO.toString());
        if(ocena<5||ocena>10){
            return dosloJeDoNeovlascenogPristupa();
        }

        Object kupacEmailO = podaci.get("kupacEmail");
        Object prodavacEmailO= podaci.get("prodavacEmail");
        if(kupacEmailO==null||prodavacEmailO==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String kupacEmail = kupacEmailO.toString();
        String prodavacEmail = prodavacEmailO.toString();
        Korisnik kupac = korisniciRepozitorijum.pretragaKorisnikaPoEmailu(kupacEmail);
        Korisnik prodavac = korisniciRepozitorijum.pretragaKorisnikaPoEmailu(prodavacEmail);
        if(kupac==null||prodavac==null){
            return dosloJeDoNeovlascenogPristupa();

        }
        if(!kupacEmail.equals(this.emailIzAutorizacije(autorizacija))){
            return dosloJeDoNeovlascenogPristupa();
            //da ne moze da se lako hakuje
        }
        if(!korisnikOcenaRepozitorijum.daLiKupacMozeDaOcenjujeProdavca(kupacEmail, prodavacEmail)){
            return dosloJeDoNeovlascenogPristupa();
        }
        boolean rez = korisnikOcenaRepozitorijum.izmeniteOcenuKorisnika(id,ocena);
        double prosek = korisniciRepozitorijum.izracunajteProsecnuOcenuKupaca(prodavacEmail);
        return vratiteRezultat(rez, prosek);
    }


    private RezultatRegistrovaniKorisnik vratiteRezultat(boolean rezultat, double prosek){
        RezultatUnesiteOcenuKorisnika r= new RezultatUnesiteOcenuKorisnika();
        r.setDaLiJeURedu(rezultat);
        r.setProsek(prosek);
        return r;
    }

}


