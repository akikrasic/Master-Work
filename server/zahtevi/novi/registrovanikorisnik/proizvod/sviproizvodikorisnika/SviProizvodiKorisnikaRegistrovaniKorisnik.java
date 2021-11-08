package srb.akikrasic.zahtevi.novi.registrovanikorisnik.proizvod.sviproizvodikorisnika;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.domen.Proizvod;

import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.novi.osnovne.RezultatPrivilegovanogKorisnika;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.proizvod.sviproizvodikorisnika.RezultatSviProizvodiKorisnika;

import java.util.List;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class SviProizvodiKorisnikaRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private ProizvodiRepozitorijum proizvodiRepozitorijum;

    @RequestMapping(value = "/vratiteSveProizvodeKorisnika", method = RequestMethod.GET)
    public RezultatPrivilegovanogKorisnika izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija
    ) {
        String email = this.emailIzAutorizacije(autorizacija);
        return vratiteRezultat(proizvodiRepozitorijum.vratiteSveProizvodeNekogKorisnikaPoEmailuDaIhOnMenja(email));
    }

    private RezultatPrivilegovanogKorisnika vratiteRezultat(List<Proizvod> listaProizvoda){
        RezultatSviProizvodiKorisnika r = new RezultatSviProizvodiKorisnika();
        r.setDaLiJeURedu(true);
        r.setProizvodi(listaProizvoda);
        r.setUkBrojProizvoda(r.getProizvodi().size());
        return r;
    }
}
