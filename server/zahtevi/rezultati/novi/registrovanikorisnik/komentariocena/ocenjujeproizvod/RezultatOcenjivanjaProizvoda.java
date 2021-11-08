package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.komentariocena.ocenjujeproizvod;

import srb.akikrasic.domen.KomentarIOcena;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

/**
 * Created by aki on 1/11/18.
 */
public class RezultatOcenjivanjaProizvoda extends RezultatRegistrovaniKorisnik {
    private KomentarIOcena komentarIOcena;



    public KomentarIOcena getKomentarIOcena() {
        return komentarIOcena;
    }

    public void setKomentarIOcena(KomentarIOcena komentariOcena) {
        this.komentarIOcena = komentariOcena;
    }
}
