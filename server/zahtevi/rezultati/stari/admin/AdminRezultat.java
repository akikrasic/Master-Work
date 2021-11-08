package srb.akikrasic.zahtevi.rezultati.stari.admin;

import org.springframework.stereotype.Component;
import srb.akikrasic.domen.Racun;
import srb.akikrasic.zahtevi.rezultati.stari.svikorisnici.Rezultat;
import srb.akikrasic.zahtevi.rezultati.novi.admin.stanjeracuna.RezultatStanjeRacunaAdmin;

/**
 * Created by aki on 11/5/17.
 */
@Component
public class AdminRezultat extends Rezultat {
   public RezultatAdmin vratiteStanjeRacuna(Racun racun){
    RezultatStanjeRacunaAdmin r = new RezultatStanjeRacunaAdmin();
    r.setDaLiJeURedu(true);
    r.setRacun(racun);

    return r;
   }
}
