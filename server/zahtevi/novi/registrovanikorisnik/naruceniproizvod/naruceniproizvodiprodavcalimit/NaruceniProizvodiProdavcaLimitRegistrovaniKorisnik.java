package srb.akikrasic.zahtevi.novi.registrovanikorisnik.naruceniproizvod.naruceniproizvodiprodavcalimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.baza.novipaket.repozitorijum.NaruceniProizvodiRepozitorijum;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.naruceniproizvod.naruceniproizvodiprodavca.RezultatNaruceniProizvodiProdavca;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.novi.registrovanikorisnik.naruceniproizvod.naruceniproizvodiprodavca.NaruceniProizvodiProdavcaRegistrovaniKorisnik;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class NaruceniProizvodiProdavcaLimitRegistrovaniKorisnik extends Zahtev {

    @Autowired
    protected NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;
    @RequestMapping(value = "/naruceniProizvodiProdavcaLimit", method = RequestMethod.GET)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam int pocetak,
            @RequestParam int pomeraj
    ) {
        if (!pocetakPomerajuRedu(pocetak, pomeraj))
            return dosloJeDoNeovlascenogPristupa();
        String email = this.emailIzAutorizacije(autorizacija);
        return vratiteRezultat(naruceniProizvodiRepozitorijum.vratiteNaruceneProizvodeProdavcaLimit(email, pocetak, pomeraj));
    }

    protected RezultatNaruceniProizvodiProdavca vratiteRezultat(List<NaruceniProizvod> naruceniProizvodi){
        RezultatNaruceniProizvodiProdavca r = new RezultatNaruceniProizvodiProdavca();
        r.setNaruceniProizvodi(naruceniProizvodi);
        r.setDuzina(naruceniProizvodi.size());
        r.setDaLiJeURedu(true);
        return r;
    }
}
