package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.poruke.vratiterazgovor;

import srb.akikrasic.domen.Poruka;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.List;

/**
 * Created by aki on 11/15/17.
 */
public class RezultatPoruke extends RezultatRegistrovaniKorisnik {
    private List<Poruka> poruke;

    public List<Poruka> getPoruke() {
        return poruke;
    }

    public void setPoruke(List<Poruka> poruke) {
        this.poruke = poruke;
    }
}
