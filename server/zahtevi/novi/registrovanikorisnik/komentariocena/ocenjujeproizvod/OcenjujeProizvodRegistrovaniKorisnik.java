package srb.akikrasic.zahtevi.novi.registrovanikorisnik.komentariocena.ocenjujeproizvod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.KomentariIOceneRepozitorijum;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.komentariocena.ocenjujeproizvod.RezultatOcenjivanjaProizvoda;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

import java.util.Map;

/**
 * Created by aki on 12/19/17.
 */
@RestController
public class OcenjujeProizvodRegistrovaniKorisnik extends Zahtev{

    @Autowired
    private KomentariIOceneRepozitorijum komentariIOceneRepozitorijum;

    @RequestMapping(value="/registrovaniKorisnikOcenjujeProizvod", method=RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody Map<String, Object> podaci
    ){

        if(podaci==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        Object ocenaObj = podaci.get("ocena");
        if(ocenaObj==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String ocenaStr = ocenaObj.toString();
        Integer ocena = this.konverzija.konverzijaInteger(ocenaStr);
        if(ocena==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        if(!(ocena>4 &&ocena<11)){
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
        }//ovo ostaje tako


        return vratiteRezultat(komentariIOceneRepozitorijum.snimiteOcenuNovaMetoda(ocena, email, proizvodId ), email, proizvodId);
    }

    private RezultatRegistrovaniKorisnik vratiteRezultat(boolean rezultat, String email, int proizvodId){
                RezultatOcenjivanjaProizvoda r = new RezultatOcenjivanjaProizvoda();
                r.setDaLiJeURedu(rezultat);
                if(rezultat){
                    r.setKomentarIOcena(komentariIOceneRepozitorijum.vratiteKomentarIOcenuZaKorisnikaIProizvodNovi(email, proizvodId));
                   //System.out.println(r.getKomentarIOcena().getOcena()+ " ocena");
                }
                return r;
    }
}
