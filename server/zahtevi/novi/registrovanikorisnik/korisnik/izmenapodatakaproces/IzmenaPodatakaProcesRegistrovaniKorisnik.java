package srb.akikrasic.zahtevi.novi.registrovanikorisnik.korisnik.izmenapodatakaproces;

import org.springframework.beans.factory.annotation.Autowired;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.izmenapodatakaproces.RezultatIzmenePodatakaProces;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class IzmenaPodatakaProcesRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;


    //napomena vrednost regularnog izraza je preuzeta sa adrese: http://emailregex.com/
    private Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private Pattern sestIVisePattern = Pattern.compile(".{6,}");
    private Pattern maloSlovoPattern = Pattern.compile("^.{0,}[a-zа-ш]{1,}.{0,}");
    private Pattern velikoSlovoPattern = Pattern.compile("^.{0,}[A-ZА-Ш]{1,}.{0,}");
    private Pattern brojPattern = Pattern.compile("^.{0,}[0-9]{1,}.{0,}");
    private Pattern specKarakteriPattern = Pattern.compile("^.{0,}[!@#$%^&*()]{1,}.{0,}");
    private Pattern telefonPattern = Pattern.compile("^[0-9]{8,}$");


    private boolean sigurnosnaProveraIzmenjenihPodataka(String imeNaziv, String sifra, String adresa, String mesto, String tel1, String tel2, String tel3) {
        if (imeNaziv == null || sifra == null || adresa == null || mesto == null || tel1 == null || tel2 == null || tel3 == null)
            return false;
        if (imeNaziv.equals("")) return false;

        if (!sifra.equals("")) {
            if (!sestIVisePattern.matcher(sifra).matches()) return false;
            if (!maloSlovoPattern.matcher(sifra).matches()) return false;
            if (!velikoSlovoPattern.matcher(sifra).matches()) return false;
            if (!brojPattern.matcher(sifra).matches()) return false;
            if (!specKarakteriPattern.matcher(sifra).matches()) return false;
        }
        if (adresa.equals("")) return false;
        if (mesto.equals("")) return false;
        if (tel1.equals("")) return false;
        if (!telefonPattern.matcher(tel1).matches()) return false;
        if (!tel2.equals(""))
            if (!telefonPattern.matcher(tel2).matches()) return false;
        if (!tel3.equals(""))
            if (!telefonPattern.matcher(tel3).matches()) return false;

        return true;
    }







    @RequestMapping(value = "/korisnikIzmenaPodatakaProces", method = RequestMethod.PUT)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> param
    ) {
        String email = emailIzAutorizacije(autorizacija);
        String imeNaziv = (String) param.get("imeNaziv");
        byte[] sifra = ((String) param.get("sifra")).getBytes();
        String adresa = (String) param.get("adresa");
        String mesto = (String) param.get("mesto");
        String tel1 = (String) param.get("tel1");
        String tel2 = (String) param.get("tel2");
        String tel3 = (String) param.get("tel3");
        //sada moramo da proverimo da li su parametri validni
        if (sigurnosnaProveraIzmenjenihPodataka(imeNaziv, new String(sifra), adresa, mesto, tel1, tel2, tel3)) {
           return  vratiteRezultat(korisniciRepozitorijum.izmenitePodatkeZaKorisnika(imeNaziv, email, sifra, adresa, mesto, tel1, tel2, tel3));

        } else {
            return dosloJeDoNeovlascenogPristupa();
        }

    }

    private RezultatIzmenePodatakaProces vratiteRezultat(boolean daLiJeURedu){
        RezultatIzmenePodatakaProces r = new RezultatIzmenePodatakaProces();
        r.setDaLiJeURedu(daLiJeURedu);
        return r;

    }

}
