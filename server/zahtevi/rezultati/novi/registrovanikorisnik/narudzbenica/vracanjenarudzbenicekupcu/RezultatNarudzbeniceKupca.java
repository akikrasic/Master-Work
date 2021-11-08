package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.narudzbenica.vracanjenarudzbenicekupcu;

import srb.akikrasic.domen.Narudzbenica;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.List;

/**
 * Created by aki on 10/14/17.
 */
public class RezultatNarudzbeniceKupca extends RezultatRegistrovaniKorisnik {
    private int duzina;
    private List<Narudzbenica> narudzbenice;

    public List<Narudzbenica> getNarudzbenice() {
        return narudzbenice;
    }

    public void setNarudzbenice(List<Narudzbenica> narudzbenice) {
        this.narudzbenice = narudzbenice;
    }

    public int getDuzina() {
        return duzina;
    }

    public void setDuzina(int duzina) {
        this.duzina = duzina;
    }
}
