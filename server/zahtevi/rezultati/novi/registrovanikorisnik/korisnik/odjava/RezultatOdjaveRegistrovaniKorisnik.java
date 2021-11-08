package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.odjava;

import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

/**
 * Created by aki on 8/17/17.
 */
public class RezultatOdjaveRegistrovaniKorisnik extends RezultatRegistrovaniKorisnik {
    private boolean odjavaUspesna;

    public boolean isOdjavaUspesna() {
        return odjavaUspesna;
    }

    public void setOdjavaUspesna(boolean odjavaUspesna) {
        this.odjavaUspesna = odjavaUspesna;
    }
}
