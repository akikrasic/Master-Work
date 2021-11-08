package srb.akikrasic.zahtevi.novi.svikorisnici.svekategorije;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import srb.akikrasic.baza.novipaket.repozitorijum.KategorijeRepozitorijum;
import srb.akikrasic.domen.Kategorija;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.svekategorije.RezultatSveKategorije;

import java.util.List;


/**
 * Created by aki on 9/10/17.
 */
@RestController
public class SveKategorije {
    @Autowired
    private KategorijeRepozitorijum kategorijeRepozitorijum;

    @RequestMapping(value="/vratiteSveKategorije", method=RequestMethod.GET)
    public RezultatSveKategorije sveKategorije(){
        return vratiteRezultat(kategorijeRepozitorijum.vratiteSveKategorije());

    }
    private RezultatSveKategorije vratiteRezultat(List<Kategorija> kategorije){
        RezultatSveKategorije r = new RezultatSveKategorije();
        r.setKategorije(kategorije);
        return r;
    }
}
