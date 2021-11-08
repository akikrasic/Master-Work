package srb.akikrasic.zahtevi.rezultati.novi.admin.izvestaj;

import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;

import java.util.List;

/**
 * Created by aki on 11/18/17.
 */
public class AdminIzvestajRezultat extends RezultatAdmin {
    private List<NaruceniProizvod> izvestaj;


    public List<NaruceniProizvod> getIzvestaj() {
        return izvestaj;
    }

    public void setIzvestaj(List<NaruceniProizvod> izvestaj) {
        this.izvestaj = izvestaj;
    }
}
