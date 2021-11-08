package srb.akikrasic.zahtevi.stari.svikorisnici;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import srb.akikrasic.baza.Baza;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.NepotvrdjeniKorisniciRepozitorijum;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.token.Tokeni;

/**
 * Created by aki on 8/5/17.
 */
//@RestController
public class PotvrdaRegistracije {
    @Autowired
    private Baza b;
    @Autowired
    private Tokeni radSaTokenima;

    @Autowired
    private Slike slike;
    @Autowired
    private NepotvrdjeniKorisniciRepozitorijum nepotvrdjeniKorisniciRepozitorijum;

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijump;

    class UspesnoPotvrdjen{
        private boolean uspesnoPotvrdjen;

        public boolean isUspesnoPotvrdjen() {
            return uspesnoPotvrdjen;
        }

        public void setUspesnoPotvrdjen(boolean uspesnoPotvrdjen) {
            this.uspesnoPotvrdjen = uspesnoPotvrdjen;
        }
    }


    @RequestMapping(value="/potvrdaRegistracije", method= RequestMethod.GET)
    public UspesnoPotvrdjen potvrditeRegistraciju(
            @RequestParam String email,
            @RequestParam String token
    ){

       // String kodiraniToken = radSaTokenima.dekodujteURLFormat(token);
        String dekodiraniToken  = new String(Base64.decodeBase64(token.getBytes()));

        UspesnoPotvrdjen up = new UspesnoPotvrdjen();
        /*
        up.setUspesnoPotvrdjen(b.daLiJeMogucaPotvrdaKorisnika(email, dekodiraniToken));
        */
        Korisnik k = b.daLiJeMogucaPotvrdaKorisnika(email, dekodiraniToken);
        if(k==null){
            up.setUspesnoPotvrdjen(false);
            return up;
        }
        up.setUspesnoPotvrdjen(b.potvrditeKorisnika(k));
        slike.napraviteDirektorijumZaSmestajSlikaKorisnika(email);
        slike.napraviteDirektorijumZaSmestajPrivremenihSlika(email);
        return up;
    }
}
