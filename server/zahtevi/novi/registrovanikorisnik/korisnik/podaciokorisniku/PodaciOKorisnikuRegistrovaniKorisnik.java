package srb.akikrasic.zahtevi.novi.registrovanikorisnik.korisnik.podaciokorisniku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisnikOcenaRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.KorisnikOcena;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.podaciokorisniku.RezultatPodaciOKorisniku;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.stari.svikorisnici.Rezultat;

import java.util.List;

/**
 * Created by aki on 1/15/18.
 */
@RestController
public class PodaciOKorisnikuRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @Autowired
    private ProizvodiRepozitorijum proizvodiRepozitorijum;

    @Autowired
    private KorisnikOcenaRepozitorijum korisnikOcenaRepozitorijum;

    @Autowired
    private Slike slike;

    @RequestMapping(value="/korisnikPodaciOKorisniku", method=RequestMethod.GET)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String email
    ){
        if(email==null)return dosloJeDoNeovlascenogPristupa();
        Korisnik korisnik = korisniciRepozitorijum.pretragaKorisnikaPoEmailu(email);
        if( korisnik==null)
            return dosloJeDoNeovlascenogPristupa();
        String ekstenzija = slike.vratiteEkstenzijuSlikeProfila(email);
        List<Proizvod> proizvodiKorisnika = proizvodiRepozitorijum.vratiteProizvodeKorisnikaZaPrikazSaKomentarimaIOcenama(email);
        List<KorisnikOcena> oceneKorisnika = korisnikOcenaRepozitorijum.vratiteOceneZaKorisnika(email);
        String emailKupca = this.emailIzAutorizacije(autorizacija);
        boolean daLiKupacMozeDaOcenjujeProdavca = korisnikOcenaRepozitorijum.daLiKupacMozeDaOcenjujeProdavca(emailKupca, email);
        return vratiteRezultat(korisnik, proizvodiKorisnika,ekstenzija, oceneKorisnika, daLiKupacMozeDaOcenjujeProdavca );
    }

    private RezultatRegistrovaniKorisnik vratiteRezultat(Korisnik korisnik, List<Proizvod> proizvodi, String ekstenzija, List<KorisnikOcena> oceneKorisnika, boolean daLiKupacMozeDaOcenjujeProdavca){
        RezultatPodaciOKorisniku r = new RezultatPodaciOKorisniku();
        r.setKorisnik(korisnik);
        r.setProizvodi(proizvodi);
        r.setUkBrojProizvoda(proizvodi.size());
        r.setEkstenzija(ekstenzija);
        r.setOceneKorisnika(oceneKorisnika);
        r.setDaLiMozeKupacDaOcenjujeProdavca(daLiKupacMozeDaOcenjujeProdavca);
        r.setDaLiJeURedu(true);
        return r;
    }
}
