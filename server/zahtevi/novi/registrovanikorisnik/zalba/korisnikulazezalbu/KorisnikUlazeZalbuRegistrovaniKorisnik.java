package srb.akikrasic.zahtevi.novi.registrovanikorisnik.zalba.korisnikulazezalbu;

import org.springframework.beans.factory.annotation.Autowired;
import srb.akikrasic.baza.novipaket.repozitorijum.NaruceniProizvodiRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.ZalbeRepozitorijum;
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
public class KorisnikUlazeZalbuRegistrovaniKorisnik extends Zahtev {


    @Autowired
    private ObavestenjaRukovalac porudzbinaRukovanje;

    @Autowired
    private ZalbeRepozitorijum zalbeRepozitorijum;

    @Autowired
    private NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;


    @RequestMapping(value = "/korisnikUlazeZalbu", method = RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> podaci
    ) {
        Map<String, Object> naruceniProizvodMapa = (Map<String, Object>) podaci.get("naruceniProizvod");
        if (naruceniProizvodMapa == null){
            return dosloJeDoNeovlascenogPristupa();
        }
        if (naruceniProizvodMapa.get("id") == null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String idString = naruceniProizvodMapa.get("id").toString();
        Integer id = this.konverzija.konverzijaInteger(idString);
        if (id == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        //provera cene kolicine  i ceneputakolicine

        NaruceniProizvod np = naruceniProizvodiRepozitorijum.vratiteNaruceniProizvodPoId(id);
        if (!np.getNarudzbenica().getKupac().getEmail().equals(this.emailIzAutorizacije(autorizacija))) {
            return dosloJeDoNeovlascenogPristupa();
        }
        if (podaci.get("zalba") == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        String zalba = podaci.get("zalba").toString();

        boolean rezultat =zalbeRepozitorijum.korisnikSeZalio(np, zalba);
        if(rezultat){
            String poruka = String.format("Корисник %s се жалио на поруџбину Вашег производа %s.",np.getNarudzbenica().getKupac().getImeNaziv(), np.getProizvod().getNaziv());
            this.porudzbinaRukovanje.posaljite(np.getProizvod().getKorisnik().getEmail(),poruka);
        }
        return this.postaviteSvejeURedu(rezultat);

    }

}
