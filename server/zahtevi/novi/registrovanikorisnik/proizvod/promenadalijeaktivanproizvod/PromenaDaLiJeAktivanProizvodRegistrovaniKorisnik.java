package srb.akikrasic.zahtevi.novi.registrovanikorisnik.proizvod.promenadalijeaktivanproizvod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

import java.util.LinkedHashMap;
import org.springframework.web.bind.annotation.*;
/**
 * Created by aki on 12/18/17.
 */
@RestController
public class PromenaDaLiJeAktivanProizvodRegistrovaniKorisnik extends Zahtev {


    @Autowired
    private ProizvodiRepozitorijum proizvodiRepozitorijum;

    @RequestMapping(value = "/korisnikPromeniteDaLiJeAktivan", method = RequestMethod.PUT)
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
        String aktivanString = mapa.get("aktivan").toString();
        if (aktivanString == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        Boolean daLiJeAktivan = this.konverzija.konverzijaBoolean(aktivanString);
        if (daLiJeAktivan == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        p.setAktivan(daLiJeAktivan);
        proizvodiRepozitorijum.izmeniteProizvodAktivan(p);
        return vratiteRezultat();
    }
    private RezultatRegistrovaniKorisnik vratiteRezultat(){
        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        r.setDaLiJeURedu(true);
        return r;
    }
}
