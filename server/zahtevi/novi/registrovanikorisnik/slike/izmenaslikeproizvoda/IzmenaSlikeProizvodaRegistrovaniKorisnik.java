package srb.akikrasic.zahtevi.novi.registrovanikorisnik.slike.izmenaslikeproizvoda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.izmenaslikeproizvoda.RezultatDodavanjaSlikeProizvodaRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

/**
 * Created by aki on 12/19/17.
 */
@RestController
public class IzmenaSlikeProizvodaRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private Slike slike;

    @Autowired
    private ProizvodiRepozitorijum proizvodiRepozitorijum;

    @RequestMapping(value="/korisnikIzmenaSlikeProizvoda", method=RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization")String autorizacija,
            @RequestParam("slika") MultipartFile slika,
            @RequestParam("proizvodId") int proizvodId

    ){
        if(slika==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String ekstenzija = slike.ekstenzija(slika.getOriginalFilename());
        if(ekstenzija.equals("png")||ekstenzija.equals("jpeg")||ekstenzija.equals("jpg")) {
            Proizvod p = proizvodiRepozitorijum.vratiteProizvodPoId(proizvodId);
            String email = this.emailIzAutorizacije(autorizacija);
            if(!email.equals(p.getKorisnik().getEmail())){
                return dosloJeDoNeovlascenogPristupa();
            }

            String imeSlike= slike.sacuvajteSlikuProizvoda(slika,proizvodId, ekstenzija );
            if(imeSlike==null){
                return greska();
            }
            return vratiteRezultat(imeSlike);
        }
        else{
            return greska();
            //nije u pitanju neovlasceni pristup, jer treba da se nastavi sa radom
            //ali ne sme da bude nesto sto nema neku od tri navedene ekstenzije
        }

    }

    private RezultatRegistrovaniKorisnik vratiteRezultat(String imeSlike){
        RezultatDodavanjaSlikeProizvodaRegistrovaniKorisnik r = new RezultatDodavanjaSlikeProizvodaRegistrovaniKorisnik();
        r.setDaLiJeURedu(true);
        r.setImeUneteSlike(imeSlike);
        return r;
    }
}
