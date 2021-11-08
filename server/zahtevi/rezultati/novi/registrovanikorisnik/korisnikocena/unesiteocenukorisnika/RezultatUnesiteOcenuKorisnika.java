package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnikocena.unesiteocenukorisnika;

import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

/**
 * Created by aki on 2/24/18.
 */
public class RezultatUnesiteOcenuKorisnika extends RezultatRegistrovaniKorisnik {
    private int idUneteOcene;
    double prosek;

    public int getIdUneteOcene() {
        return idUneteOcene;
    }

    public void setIdUneteOcene(int idUneteOcene) {
        this.idUneteOcene = idUneteOcene;
    }

    public double getProsek() {
        return prosek;
    }

    public void setProsek(double prosek) {
        this.prosek = prosek;
    }
}
