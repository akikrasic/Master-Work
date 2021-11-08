package srb.akikrasic.zahtevi.novi.admin.kategorijevratitesve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.KategorijeRepozitorijum;
import srb.akikrasic.domen.Kategorija;
import srb.akikrasic.zahtevi.rezultati.novi.admin.kategorijevratitesve.RezultatVratiteKategorijeAdmin;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;
import srb.akikrasic.zahtevi.novi.osnovne.AdminZahtev;

import java.util.List;

/**
 * Created by aki on 12/20/17.
 */
@RestController
public class AdminKategorijeVratiteSve extends AdminZahtev {


    @Autowired
    private KategorijeRepozitorijum kategorijeRepozitorijum;

    @RequestMapping(value="/adminVratiteKategorije", method= RequestMethod.GET)
    public RezultatAdmin izvrsiteZahtev(@RequestHeader("Authorization")String autorizacija
    ){
        return vratiteRezultat(kategorijeRepozitorijum.vratiteSveKategorije());

    }
    private RezultatVratiteKategorijeAdmin vratiteRezultat(List<Kategorija> kategorije){
        RezultatVratiteKategorijeAdmin r = new RezultatVratiteKategorijeAdmin();
        r.setDaLiJeURedu(true);
        r.setKategorije(kategorije);
        return r;
    }
}
