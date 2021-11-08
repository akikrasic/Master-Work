package srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.unosslikeproizvoda;

import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.List;

/**
 * Created by aki on 8/29/17.
 */
public class RezultatPostavljanjaSlikeProizvodaRegistrovaniKorisnik extends RezultatRegistrovaniKorisnik {
    private List<String> slike ;


    public List<String> getSlike() {
        return slike;
    }

    public void setSlike(List<String> slike) {
        this.slike = slike;
    }
}
