package srb.akikrasic.zahtevi.novi.admin.kategorijedodavanje;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.LinkedHashMap;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.KategorijeRepozitorijum;
import srb.akikrasic.zahtevi.rezultati.novi.admin.kategorijedodavanje.RezultatDodavanjaKategorijeAdmin;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;
import srb.akikrasic.zahtevi.novi.osnovne.AdminZahtev;

/**
 * Created by aki on 12/20/17.
 */
@RestController
public class AdminKategorijeDodavanje extends AdminZahtev {

    @Autowired
    private KategorijeRepozitorijum kategorijeRepozitorijum;

    @RequestMapping(value="/adminDodajteKategoriju", method= RequestMethod.POST)
    public RezultatAdmin izvrsiteZahtev(
            @RequestHeader("Authorization")String autorizacija,
            @RequestBody LinkedHashMap<String, Object> parametri
    ){
        String nazivKategorije= (String)parametri.get("nazivKategorije");
        return vratiteRezultat(kategorijeRepozitorijum.dodajteKategoriju(nazivKategorije));

    }
    private RezultatDodavanjaKategorijeAdmin vratiteRezultat(boolean rezultat){
        RezultatDodavanjaKategorijeAdmin r = new RezultatDodavanjaKategorijeAdmin();
        r.setDaLiJeURedu(rezultat);
        return r;
    }
}
