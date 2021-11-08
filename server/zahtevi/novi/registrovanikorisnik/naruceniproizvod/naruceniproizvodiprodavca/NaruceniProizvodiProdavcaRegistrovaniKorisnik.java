package srb.akikrasic.zahtevi.novi.registrovanikorisnik.naruceniproizvod.naruceniproizvodiprodavca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import srb.akikrasic.baza.novipaket.repozitorijum.NaruceniProizvodiRepozitorijum;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.naruceniproizvod.naruceniproizvodiprodavca.RezultatNaruceniProizvodiProdavca;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class NaruceniProizvodiProdavcaRegistrovaniKorisnik extends Zahtev {

    @Autowired
    protected NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;

    @RequestMapping(value = "/naruceniProizvodiProdavca", method = RequestMethod.GET)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija
    ) {
        String email = this.emailIzAutorizacije(autorizacija);
        return vratiteRezultat(naruceniProizvodiRepozitorijum.vratiteNaruceneProizvodeProdavca(email));
    }
    protected RezultatNaruceniProizvodiProdavca vratiteRezultat(List<NaruceniProizvod> naruceniProizvodi){
        RezultatNaruceniProizvodiProdavca r = new RezultatNaruceniProizvodiProdavca();
        r.setNaruceniProizvodi(naruceniProizvodi);
        r.setDuzina(naruceniProizvodi.size());
        r.setDaLiJeURedu(true);
        return r;
    }

}
