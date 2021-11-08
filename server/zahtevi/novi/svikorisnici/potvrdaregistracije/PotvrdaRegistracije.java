package srb.akikrasic.zahtevi.novi.svikorisnici.potvrdaregistracije;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import srb.akikrasic.baza.novipaket.repozitorijum.NepotvrdjeniKorisniciRepozitorijum;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.potvrdaregistracije.RezultatUspesnoPotvrdjen;

/**
 * Created by aki on 8/5/17.
 */
@RestController
public class PotvrdaRegistracije {

    @Autowired
    private Slike slike;
    @Autowired
    private NepotvrdjeniKorisniciRepozitorijum nepotvrdjeniKorisniciRepozitorijum;





    @RequestMapping(value="/potvrdaRegistracije", method= RequestMethod.GET)
    public RezultatUspesnoPotvrdjen potvrditeRegistraciju(
            @RequestParam String email,
            @RequestParam String token
    ){

       // String kodiraniToken = radSaTokenima.dekodujteURLFormat(token);
        String dekodiraniToken  = new String(Base64.decodeBase64(token.getBytes()));

        /*
        up.setUspesnoPotvrdjen(b.daLiJeMogucaPotvrdaKorisnika(email, dekodiraniToken));
        */
        Korisnik k = nepotvrdjeniKorisniciRepozitorijum.daLiJeMogucaPotvrdaKorisnika(email, dekodiraniToken);
        if(k==null){
            return vratiteRezultat(false);
        }
        napraviteDirektorijumeZaSmestajSlika(email);
        return vratiteRezultat(nepotvrdjeniKorisniciRepozitorijum.potvrditeKorisnika(k));
    }

    private RezultatUspesnoPotvrdjen vratiteRezultat(boolean uspesnoPotvrdjen){
        RezultatUspesnoPotvrdjen up = new RezultatUspesnoPotvrdjen();
        up.setUspesnoPotvrdjen(uspesnoPotvrdjen);
        return up;
    }

    private void napraviteDirektorijumeZaSmestajSlika(String email){
        slike.napraviteDirektorijumZaSmestajSlikaKorisnika(email);
        slike.napraviteDirektorijumZaSmestajPrivremenihSlika(email);
    }
}
