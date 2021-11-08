package srb.akikrasic.zahtevi.stari.svikorisnici;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.dalijeulogovankorisnik.RezultatDaLiJeUlogovanKorisnik;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.pretragakorisnikapoemailu.RezultatPretrageKorisnikaPoEmailu;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.vratitekategorije.RezultatVratiteKategorije;
import srb.akikrasic.token.Tokeni;

/**
 * Created by aki on 8/17/17.
 */
//@RestController
public class SviKorisniciPrijemZahteva {
    @Autowired
    private Baza b;
    @Autowired
    private Tokeni radSaTokenima;
    @RequestMapping(value="/korisnikPoEmailu", method=RequestMethod.GET)
    public RezultatPretrageKorisnikaPoEmailu pretragaKorisnikaPoEmailu(@RequestParam String email){
        RezultatPretrageKorisnikaPoEmailu r = new RezultatPretrageKorisnikaPoEmailu();

        Korisnik k = b.pretragaKorisnikaPoEmailu(email);
        if(k==null)r.setPronadjen(false);
        else{
            r.setPronadjen(true);
            r.setKorisnik(k);
        }

        return r;
    }
    @RequestMapping(value="/daLiJeUlogovanAdmin", method= RequestMethod.GET)
    public RezultatDaLiJeUlogovanKorisnik daLiJeUlogovanAdmin(
            @RequestParam String token){

        RezultatDaLiJeUlogovanKorisnik r = new RezultatDaLiJeUlogovanKorisnik();
        r.setDaLiJeUlogovan(radSaTokenima.daLiJeUlogovanAdmin(token));
        return r;
    }

    @RequestMapping(value="/daLiJeUlogovanKorisnik", method=RequestMethod.GET)
    public RezultatDaLiJeUlogovanKorisnik daLiJeUlogovanKorisnik(@RequestParam String email,
                                                                 @RequestParam String token){

        RezultatDaLiJeUlogovanKorisnik r = new RezultatDaLiJeUlogovanKorisnik();
        r.setDaLiJeUlogovan(radSaTokenima.daLiJeUlogovanKorisnik(email, token));
        return r;
    }

    @RequestMapping(value="/vratiteKategorije", method=RequestMethod.GET)
    public RezultatVratiteKategorije vratiteKategorije(){
        RezultatVratiteKategorije r = new RezultatVratiteKategorije();
        r.setKategorije(b.vratiteSveKategorije());
        return r;
    }
}
