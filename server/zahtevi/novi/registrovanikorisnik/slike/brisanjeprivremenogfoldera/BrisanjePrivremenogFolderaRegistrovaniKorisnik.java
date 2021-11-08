package srb.akikrasic.zahtevi.novi.registrovanikorisnik.slike.brisanjeprivremenogfoldera;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

/**
 * Created by aki on 12/19/17.
 */
@RestController
public class BrisanjePrivremenogFolderaRegistrovaniKorisnik extends Zahtev{

    @Autowired
    private Slike slike;


    @RequestMapping(value="/obrisitePrivremeniFolder", method= RequestMethod.DELETE)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(@RequestHeader("Authorization") String autorizacija,
                                                                 @RequestParam String idDirektorijumaZaBrisanje){
        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        String email = this.emailIzAutorizacije(autorizacija);
        Long id = konverzija.konverzijaLong(idDirektorijumaZaBrisanje);
        if(idDirektorijumaZaBrisanje==null||id ==null){
            return  dosloJeDoNeovlascenogPristupa();
        }
        if(!slike.obrisitePrivremeniDirektorijumZaKorisnika(email, id)){
            return dosloJeDoNeovlascenogPristupa();
        }
        return sveJeURedu();
    }

}
