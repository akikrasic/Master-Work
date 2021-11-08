package srb.akikrasic.zahtevi.novi.registrovanikorisnik.slike.unosslikeprofila;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.unosslikeprofila.RezultatPostavljanjaSlikeProfila;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

import java.io.IOException;

/**
 * Created by aki on 12/19/17.
 */
@RestController
public class UnosSlikeProfilaRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private Slike slike;

    @RequestMapping(value="/korisnikUnosSlikeProfila", method=RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam("slikaProfila") MultipartFile f ) throws IOException {



        String email = this.emailIzAutorizacije(autorizacija);
        String extenzija = f.getOriginalFilename().split("\\.")[1];
        if(extenzija.equals("jpeg")||extenzija.equals("jpg")||extenzija.equals("png")){
            return vratiteRezultat(slike.sacuvajteSlikuKorisnika(f, email, extenzija));

        }

        return vratiteRezultat(false);
    }

    private RezultatRegistrovaniKorisnik vratiteRezultat(boolean rezultat){
        RezultatPostavljanjaSlikeProfila rpsp = new RezultatPostavljanjaSlikeProfila();
        rpsp.setDaLiJeURedu(rezultat);
        return rpsp;
    }
}
