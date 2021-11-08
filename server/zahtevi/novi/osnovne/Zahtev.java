package srb.akikrasic.zahtevi.novi.osnovne;

import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

/**
 * Created by aki on 12/17/17.
 */

public class Zahtev extends ZahtevOsnovna<RezultatRegistrovaniKorisnik> {

    public Zahtev(){
        this.rezNeovlasceniPristup= new RezultatRegistrovaniKorisnik();
        this.rezSveJeURedu = new RezultatRegistrovaniKorisnik();
        this.rezZaPostavljanjeTrueIliFalse= new RezultatRegistrovaniKorisnik();
        this.rezGreska = new RezultatRegistrovaniKorisnik();
        postaviteVrednostiNaPocetne();
    }



}
