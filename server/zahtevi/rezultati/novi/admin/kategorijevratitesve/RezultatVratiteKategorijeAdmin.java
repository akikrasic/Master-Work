package srb.akikrasic.zahtevi.rezultati.novi.admin.kategorijevratitesve;

import srb.akikrasic.domen.Kategorija;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;

import java.util.List;

/**
 * Created by aki on 8/20/17.
 */
public class RezultatVratiteKategorijeAdmin extends RezultatAdmin {
    private List<Kategorija> kategorije;

    public List<Kategorija> getKategorije() {
        return kategorije;
    }

    public void setKategorije(List<Kategorija> kategorije) {
        this.kategorije = kategorije;
    }
}
