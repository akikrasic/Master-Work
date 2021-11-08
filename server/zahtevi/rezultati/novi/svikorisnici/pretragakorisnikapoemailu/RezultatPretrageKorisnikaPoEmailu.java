package srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.pretragakorisnikapoemailu;

import srb.akikrasic.domen.Korisnik;

/**
 * Created by aki on 8/17/17.
 */
public class RezultatPretrageKorisnikaPoEmailu {
    private boolean pronadjen;
    private Korisnik korisnik;

    public boolean isPronadjen() {
        return pronadjen;
    }

    public void setPronadjen(boolean pronadjen) {
        this.pronadjen = pronadjen;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}
