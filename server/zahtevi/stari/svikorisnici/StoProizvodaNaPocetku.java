package srb.akikrasic.zahtevi.stari.svikorisnici;

/**
 * Created by aki on 9/17/17.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.stoproizvoda.RezultatStoProizvoda;

//@RestController
public class StoProizvodaNaPocetku {

    @Autowired
    private Baza b;

    @RequestMapping (value="/vratiteStoPoslednjihProizvoda", method = RequestMethod.GET)
    public RezultatStoProizvoda vratiteStoProizvoda(){
        RezultatStoProizvoda r = new RezultatStoProizvoda();
        //r.setProizvodi(b.vratiteStoProizvoda());
        r.setProizvodi(b.vratiteStoProizvodaNovi());
        r.setUkBrojProizvoda(r.getProizvodi().size());
        return r;
    }
}
