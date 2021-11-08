package srb.akikrasic.zahtevi.novi.registrovanikorisnik.naruceniproizvod.potvrdaslanjaproizvoda;

import org.springframework.beans.factory.annotation.Autowired;
import srb.akikrasic.baza.novipaket.repozitorijum.NaruceniProizvodiRepozitorijum;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.websocket.klase.ObavestenjaRukovalac;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class PotvrdaSlanjaProizvodaRegistrovaniKorisnik extends Zahtev {


    @Autowired
    private NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;

    @Autowired
    private ObavestenjaRukovalac porudzbinaRukovanje;

    @RequestMapping(value = "/potvrditeSlanjeProizvoda", method = RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> param
    ) {
        if (param == null)
            return dosloJeDoNeovlascenogPristupa();
        String idString = param.get("id").toString();
        Integer id = this.konverzija.konverzijaInteger(idString);
        if (id == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        NaruceniProizvod npIzBaze = naruceniProizvodiRepozitorijum.vratiteNaruceniProizvodPoId(id);
        Map<String, Object> proizvod = (Map<String, Object>) param.get("proizvod");
        if (proizvod == null){
            return dosloJeDoNeovlascenogPristupa();
        }
        Map<String, Object> korisnik = (Map<String, Object>) proizvod.get("korisnik");
        if (korisnik == null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String email = (String) korisnik.get("email");
        if (email == null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String emailIzAutorizacije = this.emailIzAutorizacije(autorizacija);
        if (!email.equals(emailIzAutorizacije)){
            return dosloJeDoNeovlascenogPristupa();
        }
        if (!npIzBaze.getProizvod().getKorisnik().getEmail().equals(email)){
            return dosloJeDoNeovlascenogPristupa();
        }
        if(npIzBaze.isOtkazan()){
            return dosloJeDoNeovlascenogPristupa();
        }

        naruceniProizvodiRepozitorijum.prodavacPotvrdjujeNaruceniProizvod(npIzBaze);
        //ovamo treba mala refaktorizacija
        String poruka = String.format("Продавац %s Вам је послао поручени производ: %s.",
                npIzBaze.getProizvod().getKorisnik().getImeNaziv(),
                npIzBaze.getProizvod().getNaziv());
        this.porudzbinaRukovanje.posaljite(npIzBaze.getNarudzbenica().getKupac().getEmail(),
                poruka);
        return sveJeURedu();
    }

}
