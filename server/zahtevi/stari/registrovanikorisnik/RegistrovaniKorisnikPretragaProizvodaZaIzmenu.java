package srb.akikrasic.zahtevi.stari.registrovanikorisnik;

import org.springframework.beans.factory.annotation.Autowired;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.pretragaproizvodazaizmenu.RezultatPretrageProizvoda;
import srb.akikrasic.zahtevi.stari.svikorisnici.PretragaProizvoda;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.token.ProizvodSmeDaSeMenja;

import java.util.List;

/**
 * Created by aki on 9/12/17.
 */
//@RestController
public class RegistrovaniKorisnikPretragaProizvodaZaIzmenu {

    @Autowired
    private PretragaProizvoda pretragaProizvoda;
    @Autowired
    private Baza b;
    @Autowired
    private ProizvodSmeDaSeMenja proizvodSmeDaSeMenja;

    @RequestMapping(value="registrovaniKorisnikPretragaProizvodaZaIzmenu", method= RequestMethod.GET)
    public RezultatPretrageProizvoda pretragaProizvodaZaIzmenu(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam("imeNazivKorisnika") String imeNazivKorisnika,
            @RequestParam("emailKorisnika") String emailKorisnika,
            @RequestParam("nazivProizvoda") String nazivProizvoda,
            @RequestParam("cenaOd") String cenaOd,
            @RequestParam("cenaDo") String cenaDo,
            @RequestParam("kategorijaNaziv") String kategorijaNaziv,
            @RequestParam("opis") String opis,
            @RequestParam("kljucneReci") String kljucneReci
    ){
        RezultatPretrageProizvoda r = pretragaProizvoda.pretragaProizvoda(imeNazivKorisnika, emailKorisnika, nazivProizvoda, cenaOd, cenaDo, kategorijaNaziv, opis, kljucneReci);
        if(!r.isDaLiJeURedu()||r.isNeovlasceniPristup())return r;
        r.getLista().forEach(p->{
            List<String> kljucneReciList = b.vratiteSveKljucnereciZaProizvodStringovi(p);
            p.setKljucneReci(kljucneReciList);
            p.setSmeDaSeMenja(proizvodSmeDaSeMenja.smeDaSeMenja(p.getId()));
        });

        return r;
    }
}
