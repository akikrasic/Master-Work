package srb.akikrasic.zahtevi.novi.registrovanikorisnik.proizvod.promenasamocene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

import java.util.LinkedHashMap;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class PromenaSamoCeneRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private ProizvodiRepozitorijum proizvodiRepozitorijum;

    @RequestMapping(value = "/korisnikPromeniteSamoCenu", method = RequestMethod.PUT)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> mapa
    ) {
        String email = this.emailIzAutorizacije(autorizacija);
        String proizvodIdString = mapa.get("id").toString();
        Integer proizvodId = this.konverzija.konverzijaInteger(proizvodIdString);
        if (proizvodId == null) {
            return dosloJeDoNeovlascenogPristupa();
        }

        Proizvod p = proizvodiRepozitorijum.vratiteProizvodPoId(proizvodId);
        if (!p.getKorisnik().getEmail().equals(email)) {
            return dosloJeDoNeovlascenogPristupa();
        }
        String novaCenaProizvodaString = mapa.get("novacena").toString();
        Double novaCena = this.konverzija.konverzijaDouble(novaCenaProizvodaString);
        if (novaCena == null) {
            return dosloJeDoNeovlascenogPristupa();

        }
        p.setTrenutnaCena(novaCena);
        proizvodiRepozitorijum.izmeniteProizvodCenu(p);
        return vratiteRezultat();

    }
    private RezultatRegistrovaniKorisnik vratiteRezultat(){
        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        r.setDaLiJeURedu(true);
        return r;
    }

}
