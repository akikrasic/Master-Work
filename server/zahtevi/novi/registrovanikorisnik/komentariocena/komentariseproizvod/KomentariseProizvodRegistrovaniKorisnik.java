package srb.akikrasic.zahtevi.novi.registrovanikorisnik.komentariocena.komentariseproizvod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.KomentariIOceneRepozitorijum;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

import java.util.Map;

/**
 * Created by aki on 12/19/17.
 */
@RestController
public class KomentariseProizvodRegistrovaniKorisnik  extends Zahtev{

    @Autowired
    private KomentariIOceneRepozitorijum komentariIOceneRepozitorijum;

    @RequestMapping(value="/registrovaniKorisnikKomentariseProizvod", method=RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody Map<String, Object> podaci
    ){

        if(podaci==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        Object tekstObj = podaci.get("tekst");
        if(tekstObj==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String tekst = tekstObj.toString();
        if(tekst.trim().equals("")){
            return dosloJeDoNeovlascenogPristupa();
        }
        Object proizvodIdObj = podaci.get("proizvodId");
        if(proizvodIdObj==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String proizvodIdStr = proizvodIdObj.toString();
        Integer proizvodId = this.konverzija.konverzijaInteger(proizvodIdStr);
        if(proizvodId==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String email = this.emailIzAutorizacije(autorizacija);
        if(!komentariIOceneRepozitorijum.daLiKorisnikSmeDaKomentariseProizvod(email , proizvodId)){
            return dosloJeDoNeovlascenogPristupa();
        }
        return postaviteSvejeURedu(komentariIOceneRepozitorijum.snimiteKomentarNovaMetoda(tekst, email, proizvodId ));
    }

}
