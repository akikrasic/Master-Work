package srb.akikrasic.zahtevi.novi.registrovanikorisnik.poruke.vratitesagovornike;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import srb.akikrasic.baza.novipaket.baza.BazaPoruka;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.Poruka;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.poruke.vratitesagovornike.RezultataKorisniciSaKojimaImaPravaDaPrica;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

/**
 * Created by aki on 12/19/17.
 */
@RestController
public class VratiteSagovornikeRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private BazaPoruka bazaPoruka;

    @RequestMapping(value="/registrovaniKorisnikVratiteSagovornike", method= RequestMethod.GET)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija
    ){
        String email = this.emailIzAutorizacije(autorizacija);
        List<Korisnik> korisnici = bazaPoruka.vratiteSagovornikeZaKorisnika(email);
        HashMap<String, List<Poruka>> poruke = new LinkedHashMap<>();
        korisnici.forEach(k->{
            List<Poruka> porukeZaKorisnika = bazaPoruka.vratitePoruke(email, k.getEmail(),30, 0);
            poruke.put(k.getEmail(), porukeZaKorisnika);
        });
        return vratiteRezultat(korisnici, poruke);
    }

    private RezultatRegistrovaniKorisnik vratiteRezultat(List<Korisnik> korisnici,HashMap<String, List<Poruka>> poruke ){
        RezultataKorisniciSaKojimaImaPravaDaPrica r = new RezultataKorisniciSaKojimaImaPravaDaPrica();
        r.setDaLiJeURedu(true);
        r.setKorisnici(korisnici);
        r.setPoruke(poruke);
        return r;
    }
}
