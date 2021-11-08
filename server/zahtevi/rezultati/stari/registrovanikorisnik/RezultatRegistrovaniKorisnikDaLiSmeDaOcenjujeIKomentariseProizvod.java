package srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik;

import srb.akikrasic.domen.Komentar;
import srb.akikrasic.domen.Ocena;

/**
 * Created by aki on 10/28/17.
 */
public class RezultatRegistrovaniKorisnikDaLiSmeDaOcenjujeIKomentariseProizvod extends RezultatRegistrovaniKorisnik {
    private boolean daLiImaPrava;
    private Komentar komentar;
    private Ocena ocena;


    public boolean isDaLiImaPrava() {
        return daLiImaPrava;
    }

    public void setDaLiImaPrava(boolean daLiImaPrava) {
        this.daLiImaPrava = daLiImaPrava;
    }

    public Komentar getKomentar() {
        return komentar;
    }

    public void setKomentar(Komentar komentar) {
        this.komentar = komentar;
    }

    public Ocena getOcena() {
        return ocena;
    }

    public void setOcena(Ocena ocena) {
        this.ocena = ocena;
    }
}
