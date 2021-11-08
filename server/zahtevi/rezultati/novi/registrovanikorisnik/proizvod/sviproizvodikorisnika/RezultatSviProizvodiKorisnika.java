package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.proizvod.sviproizvodikorisnika;

import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.List;

/**
 * Created by aki on 9/21/17.
 */
public class RezultatSviProizvodiKorisnika extends RezultatRegistrovaniKorisnik {

    private List<Proizvod> proizvodi;
    private int ukBrojProizvoda;
    public List<Proizvod> getProizvodi() {
        return proizvodi;
    }

    public void setProizvodi(List<Proizvod> proizvodi) {
        this.proizvodi = proizvodi;
    }

    public int getUkBrojProizvoda() {
        return ukBrojProizvoda;
    }

    public void setUkBrojProizvoda(int ukBrojProizvoda) {
        this.ukBrojProizvoda = ukBrojProizvoda;
    }
}
