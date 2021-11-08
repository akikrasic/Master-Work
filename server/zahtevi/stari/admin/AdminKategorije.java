package srb.akikrasic.zahtevi.stari.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.zahtevi.rezultati.novi.admin.kategorijedodavanje.RezultatDodavanjaKategorijeAdmin;
import srb.akikrasic.zahtevi.rezultati.novi.admin.kategorijeizmena.RezultatIzmeneKategorijeAdmin;
import srb.akikrasic.zahtevi.rezultati.novi.admin.kategorijevratitesve.RezultatVratiteKategorijeAdmin;

import java.util.LinkedHashMap;

/**
 * Created by aki on 11/24/17.
 */
//@RestController
public class AdminKategorije {

    @Autowired
    private Baza b;

    @RequestMapping(value="/adminDodajteKategoriju", method= RequestMethod.POST)
    public RezultatDodavanjaKategorijeAdmin dodavanjeKategorije(
            @RequestHeader("Authorization")String autorizacija,
            @RequestBody LinkedHashMap<String, Object> parametri
    ){
        String nazivKategorije= (String)parametri.get("nazivKategorije");
        RezultatDodavanjaKategorijeAdmin r = new RezultatDodavanjaKategorijeAdmin();
        r.setDaLiJeURedu(b.dodajteKategoriju(nazivKategorije));
        return r;
    }

    @RequestMapping(value="/adminVratiteKategorije", method=RequestMethod.GET)
    public RezultatVratiteKategorijeAdmin vratiteKategorije(@RequestHeader("Authorization")String autorizacija
    ){
        RezultatVratiteKategorijeAdmin r = new RezultatVratiteKategorijeAdmin();
        r.setDaLiJeURedu(true);
        r.setKategorije(b.vratiteSveKategorije());
        return r;
    }
    @RequestMapping(value="/adminIzmeniteKategoriju", method=RequestMethod.PUT)
    public RezultatIzmeneKategorijeAdmin izmenitekategoriju(
            @RequestHeader("Authorization")String autorizacija,
            @RequestBody LinkedHashMap<String, Object> param
    ){
        String idS = (String)param.get("id");
        int id = Integer.parseInt(idS);
        String noviNaziv = (String)param.get("noviNaziv");
        RezultatIzmeneKategorijeAdmin r = new RezultatIzmeneKategorijeAdmin();
        r.setDaLiJeURedu(b.izmenaKategorije(id, noviNaziv));

        return r;
    }
    @RequestMapping(value="/adminObrisiteKategoriju", method=RequestMethod.DELETE)
    public RezultatIzmeneKategorijeAdmin obrisiteKategoriju(
            @RequestHeader("Authorization")String autorizacija,
            @RequestParam int id

    ){
        RezultatIzmeneKategorijeAdmin r = new RezultatIzmeneKategorijeAdmin();
        r.setDaLiJeURedu(b.obrisiteKategoriju(id));

        return r;
    }
}
