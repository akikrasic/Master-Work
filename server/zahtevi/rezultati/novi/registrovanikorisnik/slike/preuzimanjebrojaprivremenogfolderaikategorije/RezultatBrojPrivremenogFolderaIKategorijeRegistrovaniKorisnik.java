package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.preuzimanjebrojaprivremenogfolderaikategorije;

import srb.akikrasic.domen.Kategorija;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.List;

/**
 * Created by aki on 8/28/17.
 */
public class RezultatBrojPrivremenogFolderaIKategorijeRegistrovaniKorisnik extends RezultatRegistrovaniKorisnik {
    private long idPrivremenogFoldera;
    private List<Kategorija> kategorije;
    public long getIdPrivremenogFoldera() {
        return idPrivremenogFoldera;
    }

    public void setIdPrivremenogFoldera(long idPrivremenogFoldera) {
        this.idPrivremenogFoldera = idPrivremenogFoldera;
    }

    public List<Kategorija> getKategorije() {
        return kategorije;
    }

    public void setKategorije(List<Kategorija> kategorije) {
        this.kategorije = kategorije;
    }
}
