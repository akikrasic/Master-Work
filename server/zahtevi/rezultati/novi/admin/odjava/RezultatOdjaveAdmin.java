package srb.akikrasic.zahtevi.rezultati.novi.admin.odjava;

import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;

/**
 * Created by aki on 8/17/17.
 */
public class RezultatOdjaveAdmin extends RezultatAdmin {
    private boolean odjavaUspesna;

    public boolean isOdjavaUspesna() {
        return odjavaUspesna;
    }

    public void setOdjavaUspesna(boolean odjavaUspesna) {
        this.odjavaUspesna = odjavaUspesna;
    }
}
