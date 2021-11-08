package srb.akikrasic.zahtevi.rezultati.novi.admin.stanjeracuna;

import srb.akikrasic.domen.Racun;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;

/**
 * Created by aki on 11/5/17.
 */
public class RezultatStanjeRacunaAdmin extends RezultatAdmin {
    private Racun racun;

    public Racun getRacun() {
        return racun;
    }

    public void setRacun(Racun racun) {
        this.racun = racun;
    }
}
