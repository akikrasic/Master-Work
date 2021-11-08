package srb.akikrasic.zahtevi.novi.registrovanikorisnik.zalba.korisnikodgovaranazalbu;

import org.springframework.beans.factory.annotation.Autowired;
import srb.akikrasic.baza.novipaket.repozitorijum.NaruceniProizvodiRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.ZalbeRepozitorijum;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.domen.Zalba;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class KorisnikodgovaraNaZalbuRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;

    @Autowired
    private ZalbeRepozitorijum zalbeRepozitorijum;

    @RequestMapping(value = "/korisnikOdgovaraNaZalbu", method = RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody Map<String, Object> podaci
    ) {
        Map<String, Object> naruceniProizvodMapa = (Map<String, Object>) podaci.get("naruceniProizvod");
        if (naruceniProizvodMapa == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        if (naruceniProizvodMapa.get("id") == null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String idString = naruceniProizvodMapa.get("id").toString();
        Integer id = this.konverzija.konverzijaInteger(idString);
        if (id == null){
            return dosloJeDoNeovlascenogPristupa();
        }
        //provera cene kolicine  i ceneputakolicine

        NaruceniProizvod np = naruceniProizvodiRepozitorijum.vratiteNaruceniProizvodPoId(id);

        if (!np.getProizvod().getKorisnik().getEmail().equals(this.emailIzAutorizacije(autorizacija))) {
            return dosloJeDoNeovlascenogPristupa();
        }
        Zalba z = zalbeRepozitorijum.vratiteZalbuZaNaruceniProizvod(np);
        Object zalbaOdgovorObj = podaci.get("odgovor");
        if (zalbaOdgovorObj == null){
            return dosloJeDoNeovlascenogPristupa();
        }

        z.setTekstProdavca(zalbaOdgovorObj.toString());
        boolean rezultat = zalbeRepozitorijum.prodavacOdgovaraNaZalbu(z);
        if(rezultat){
            String poruka =String.format("Корисник %s је одговорио на Вашу жалбу на производ %s. ",
                    np.getProizvod().getKorisnik().getImeNaziv(), np.getProizvod().getNaziv());
        }
        return postaviteSvejeURedu(rezultat);
    }
}
