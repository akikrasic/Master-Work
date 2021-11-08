package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.izmenapodatakadobavljanje;

import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

/**
 * Created by aki on 8/20/17.
 */
public class RezultatIzmenePodatakaDobavljanje extends RezultatRegistrovaniKorisnik {
    private Korisnik korisnik;

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}
