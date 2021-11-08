package srb.akikrasic.zahtevi.stari.svikorisnici;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.svekategorije.RezultatSveKategorije;


/**
 * Created by aki on 9/10/17.
 */
//@RestController
public class SveKategorije {
    @Autowired
    private Baza b;

    @RequestMapping(value="/vratiteSveKategorije", method=RequestMethod.GET)
    public RezultatSveKategorije sveKategorije(){
        RezultatSveKategorije r = new RezultatSveKategorije();
        r.setKategorije(b.vratiteSveKategorije());
        return r;
    }
}
