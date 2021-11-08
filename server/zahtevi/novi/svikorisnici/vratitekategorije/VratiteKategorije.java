package srb.akikrasic.zahtevi.novi.svikorisnici.vratitekategorije;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import srb.akikrasic.baza.novipaket.repozitorijum.KategorijeRepozitorijum;

import org.springframework.web.bind.annotation.*;
import srb.akikrasic.domen.Kategorija;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.vratitekategorije.RezultatVratiteKategorije;

import java.util.List;

/**
 * Created by aki on 12/20/17.
 */
@RestController
public class VratiteKategorije {

    @Autowired
    private KategorijeRepozitorijum kategorijeRepozitorijum;

    @RequestMapping(value="/vratiteKategorije", method= RequestMethod.GET)
    public RezultatVratiteKategorije vratiteKategorije(){
        return vratiteRezultat(kategorijeRepozitorijum.vratiteSveKategorije());
    }
    private RezultatVratiteKategorije vratiteRezultat(List<Kategorija> kategorije){
        RezultatVratiteKategorije r = new RezultatVratiteKategorije();
        r.setKategorije(kategorije);
        return r;
    }
}
