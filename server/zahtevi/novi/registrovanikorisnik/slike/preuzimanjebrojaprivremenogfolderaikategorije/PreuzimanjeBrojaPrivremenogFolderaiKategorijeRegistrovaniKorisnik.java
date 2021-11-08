package srb.akikrasic.zahtevi.novi.registrovanikorisnik.slike.preuzimanjebrojaprivremenogfolderaikategorije;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.KategorijeRepozitorijum;
import srb.akikrasic.domen.Kategorija;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.preuzimanjebrojaprivremenogfolderaikategorije.RezultatBrojPrivremenogFolderaIKategorijeRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

import java.util.List;

/**
 * Created by aki on 12/19/17.
 */
@RestController
public class PreuzimanjeBrojaPrivremenogFolderaiKategorijeRegistrovaniKorisnik extends Zahtev {


    @Autowired
    private Slike slike;

    @Autowired
    private KategorijeRepozitorijum kategorijeRepozitorijum;
    //u sustini jedna metoda ne treba da radi dva posla, ali posto saljemo jedan zahtev, onda je najbolje da u njemu sakupimo sve sto je potrebno
    //a potrebne su i kategrije i id slike
    @RequestMapping(value="/korisnikPreuzmiteBrojPrivremenogFolderaZaUploadSlikaIKategorije", method= RequestMethod.GET)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization")String autorizacija

    ){
        String email = this.emailIzAutorizacije(autorizacija);
        long id = slike.vratiteIDPrivremenogFolderaIKreirajteGa(email);
        if(id==-1){
            return this.greska();
        }

        return vratiteRezultat(id,kategorijeRepozitorijum.vratiteSveKategorije() );

    }

    private RezultatRegistrovaniKorisnik vratiteRezultat(long id, List<Kategorija> kategorije){
        RezultatBrojPrivremenogFolderaIKategorijeRegistrovaniKorisnik r = new RezultatBrojPrivremenogFolderaIKategorijeRegistrovaniKorisnik();
        r.setDaLiJeURedu(true);
        r.setIdPrivremenogFoldera(id);
        r.setKategorije(kategorije);
        return r;
    }

}
