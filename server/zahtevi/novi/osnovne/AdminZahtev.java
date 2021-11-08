package srb.akikrasic.zahtevi.novi.osnovne;

import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;

/**
 * Created by aki on 12/20/17.
 */
public class AdminZahtev extends ZahtevOsnovna<RezultatAdmin> {

    public AdminZahtev(){
        this.rezNeovlasceniPristup= new RezultatAdmin();
        this.rezSveJeURedu = new RezultatAdmin();
        this.rezZaPostavljanjeTrueIliFalse= new RezultatAdmin();
        this.rezGreska = new RezultatAdmin();
        postaviteVrednostiNaPocetne();
    }
}
