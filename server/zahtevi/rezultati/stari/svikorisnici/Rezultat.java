package srb.akikrasic.zahtevi.rezultati.stari.svikorisnici;

import org.springframework.stereotype.Component;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

/**
 * Created by aki on 10/7/17.
 */



//dupli kod
@Component
public class Rezultat {
    RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();

    public RezultatRegistrovaniKorisnik neovlasceniPristup(){
        r.dosloJeDoNeovlascenogPristupa();
        return r;
    }

    public RezultatRegistrovaniKorisnik greska(){
        r.setDaLiJeURedu(false);
        return r;
    }
    public RezultatRegistrovaniKorisnik postaviteSveJeURedu(boolean daLiJeURedu){
        r.setDaLiJeURedu(daLiJeURedu);
        return r;
    }

}
