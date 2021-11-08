package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.komentariocena.dalismedakomentariseiocenjuje;

import srb.akikrasic.domen.KomentarIOcena;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

/**
 * Created by aki on 10/31/17.
 */
public class RezultatRegistrovaniKorisnikNoviDaLiSmeDaOcenjujeIKomentarise extends RezultatRegistrovaniKorisnik {
    private boolean daLiImaPrava;
    private KomentarIOcena komentarIOcena;

    public boolean isDaLiImaPrava() {
        return daLiImaPrava;
    }

    public void setDaLiImaPrava(boolean daLiImaPrava) {
        this.daLiImaPrava = daLiImaPrava;
    }

    public KomentarIOcena getKomentarIOcena() {
        return komentarIOcena;
    }

    public void setKomentarIOcena(KomentarIOcena komentarIOcena) {
        this.komentarIOcena = komentarIOcena;
    }
}
