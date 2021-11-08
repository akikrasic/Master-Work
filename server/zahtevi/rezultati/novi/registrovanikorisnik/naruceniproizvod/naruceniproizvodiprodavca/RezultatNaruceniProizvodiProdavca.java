package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.naruceniproizvod.naruceniproizvodiprodavca;

import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.List;

/**
 * Created by aki on 10/13/17.
 */
public class RezultatNaruceniProizvodiProdavca extends RezultatRegistrovaniKorisnik {
    private int duzina;

    private List<NaruceniProizvod> naruceniProizvodi;

    public List<NaruceniProizvod> getNaruceniProizvodi() {
        return naruceniProizvodi;
    }

    public void setNaruceniProizvodi(List<NaruceniProizvod> naruceniProizvodi) {
        this.naruceniProizvodi = naruceniProizvodi;
    }

    public int getDuzina() {
        return duzina;
    }

    public void setDuzina(int duzina) {
        this.duzina = duzina;
    }
}
