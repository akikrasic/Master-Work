package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.brisanjeslikeproizvoda;

import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

/**
 * Created by aki on 9/15/17.
 */
public class RezultatBrisanjaSlikeProizvodaRegistrovaniKorisnik extends RezultatRegistrovaniKorisnik {
    private boolean slikaUspesnoObrisana;

    public boolean isSlikaUspesnoObrisana() {
        return slikaUspesnoObrisana;
    }

    public void setSlikaUspesnoObrisana(boolean slikaUspesnoObrisana) {
        this.slikaUspesnoObrisana = slikaUspesnoObrisana;
    }
}
