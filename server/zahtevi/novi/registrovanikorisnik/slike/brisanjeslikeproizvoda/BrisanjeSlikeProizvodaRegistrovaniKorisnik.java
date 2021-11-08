package srb.akikrasic.zahtevi.novi.registrovanikorisnik.slike.brisanjeslikeproizvoda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.domen.Proizvod;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.slike.brisanjeslikeproizvoda.RezultatBrisanjaSlikeProizvodaRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

/**
 * Created by aki on 12/19/17.
 */
@RestController
public class BrisanjeSlikeProizvodaRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private Slike slike;

    @Autowired
    private ProizvodiRepozitorijum proizvodiRepozitorijum;

    @RequestMapping(value="/korisnikBrisanjeSlikeProizvoda", method= RequestMethod.DELETE)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization")String autorizacija,
            @RequestParam("putanja") String putanja

    ){
        //tu se sad proverava smeje li zmijata da brise onda da li je ovo null i jos kvo sdve trebe
        if(putanja==null||putanja.equals("")){
            return dosloJeDoNeovlascenogPristupa();
        }
        String informacije[] = putanja.split("/");
        if(informacije.length<2){
            return dosloJeDoNeovlascenogPristupa();
        }
        String emailIzAutorizacije = this.emailIzAutorizacije(autorizacija);
        String proizvodIdString = informacije[informacije.length-2];
        int proizvodId=0;
        try{
            proizvodId=Integer.parseInt(proizvodIdString);
        }
        catch(Exception e){
            return dosloJeDoNeovlascenogPristupa();
        }
        Proizvod p = proizvodiRepozitorijum.vratiteProizvodPoId(proizvodId);
        if(!emailIzAutorizacije.equals(p.getKorisnik().getEmail())){
            return dosloJeDoNeovlascenogPristupa();
        }
        return vratiteRezultat(slike.obrisiteSlikuProizvoda(proizvodId,informacije[informacije.length-1] ));


    }
    private RezultatRegistrovaniKorisnik vratiteRezultat(boolean rezultat){
        RezultatBrisanjaSlikeProizvodaRegistrovaniKorisnik r = new RezultatBrisanjaSlikeProizvodaRegistrovaniKorisnik();
        r.setSlikaUspesnoObrisana(rezultat);
        r.setDaLiJeURedu(rezultat);
        return r;
    }

}
