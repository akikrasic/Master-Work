package srb.akikrasic.zahtevi.novi.admin.kategorijeizmena;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.LinkedHashMap;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.KategorijeRepozitorijum;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;
import srb.akikrasic.zahtevi.rezultati.novi.admin.kategorijebrisanje.RezultatBrisanjaKategorijeAdmin;
import srb.akikrasic.zahtevi.novi.osnovne.AdminZahtev;

/**
 * Created by aki on 12/20/17.
 */
@RestController
public class AdminKategorijeIzmena extends AdminZahtev {

    @Autowired
    private KategorijeRepozitorijum kategorijeRepozitorijum;

    @RequestMapping(value="/adminIzmeniteKategoriju", method= RequestMethod.PUT)
    public RezultatAdmin izvrsiteZahtev(
            @RequestHeader("Authorization")String autorizacija,
            @RequestBody LinkedHashMap<String, Object> param
    ){
        String idS = (String)param.get("id");
        Integer id = this.konverzija.konverzijaInteger(idS);
        if(id==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String noviNaziv = (String)param.get("noviNaziv");
        return vratiteRezultat(kategorijeRepozitorijum.izmenaKategorije(id, noviNaziv));
    }
    private RezultatBrisanjaKategorijeAdmin vratiteRezultat(boolean rezultat){
        RezultatBrisanjaKategorijeAdmin r = new RezultatBrisanjaKategorijeAdmin();
        r.setDaLiJeURedu(rezultat);
        return r;
    }
}
