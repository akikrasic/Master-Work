package srb.akikrasic.zahtevi.novi.registrovanikorisnik.slike.unosslikeproizvoda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.unosslikeproizvoda.RezultatPostavljanjaSlikeProizvodaRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

import java.util.List;

/**
 * Created by aki on 12/19/17.
 */
@RestController
public class UnosSlikeProizvodaRegistrovaniKorisnik extends Zahtev{

    @Autowired
    private Slike slike;

    @RequestMapping(value="korisnikUnosSlikeProizvoda",method=RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam("slika") MultipartFile f,
            @RequestParam("idPrivremenogFoldera") long idPrivremenogFoldera

    ){
        String email = this.emailIzAutorizacije(autorizacija);
        if(!slike.daLIKorisnikImaPravaDaSnimaUFolder(email, idPrivremenogFoldera)){
            return dosloJeDoNeovlascenogPristupa();
        }
        if(!this.slike.sacuvajteSlikuProizvodaUPrivremenomFolderu(f, email, idPrivremenogFoldera)){
            return greska();
        }
        List<String> slikeIzPrivrFoldera =slike.vratiteSveSlikeIZPrivremenogFolderaProizvoda(email, idPrivremenogFoldera);
        return vratiteRezultat(slikeIzPrivrFoldera);
    }
    private RezultatRegistrovaniKorisnik vratiteRezultat(List<String> slikeLista){
        RezultatPostavljanjaSlikeProizvodaRegistrovaniKorisnik r = new RezultatPostavljanjaSlikeProizvodaRegistrovaniKorisnik();
        r.setSlike(slikeLista);
        r.setDaLiJeURedu(true);
        return r;
    }
}
