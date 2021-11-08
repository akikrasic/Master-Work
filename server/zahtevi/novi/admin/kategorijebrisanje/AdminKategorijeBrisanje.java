package srb.akikrasic.zahtevi.novi.admin.kategorijebrisanje;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.baza.novipaket.repozitorijum.KategorijeRepozitorijum;
import srb.akikrasic.zahtevi.rezultati.novi.admin.kategorijebrisanje.RezultatBrisanjaKategorijeAdmin;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.novi.osnovne.AdminZahtev;

/**
 * Created by aki on 12/20/17.
 */
@RestController
public class AdminKategorijeBrisanje extends AdminZahtev {

    @Autowired
    private KategorijeRepozitorijum kategorijeRepozitorijum;

    @RequestMapping(value="/adminObrisiteKategoriju", method= RequestMethod.DELETE)
    public RezultatAdmin izvrsiteZahtev(
            @RequestHeader("Authorization")String autorizacija,
            @RequestParam int id

    ){
        return vratiteRezultat(kategorijeRepozitorijum.obrisiteKategoriju(id));
    }

    private RezultatBrisanjaKategorijeAdmin vratiteRezultat(boolean rezultat){
        RezultatBrisanjaKategorijeAdmin r = new RezultatBrisanjaKategorijeAdmin();
        r.setDaLiJeURedu(rezultat);
        return r;
    }
}
