package srb.akikrasic.zahtevi.novi.registrovanikorisnik.naruceniproizvod.korisnikotkazujeporudzbinu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.baza.BazaRacunSajta;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.NaruceniProizvodiRepozitorijum;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.racuni.UpravljanjeRacunom;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.stripe.RadSaPlacanjem;
import srb.akikrasic.websocket.klase.AdminRacunRukovalac;
import srb.akikrasic.websocket.klase.KorisnikRacunRukovalac;
import srb.akikrasic.websocket.klase.ObavestenjaRukovalac;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

import java.util.Map;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class KorisnikOtkazujePorudzbinuRegistrovaniKorisnik extends Zahtev{

    @Autowired
    private RadSaPlacanjem placanje;

    @Autowired
    private UpravljanjeRacunom upravljanjeRacunom;

    @Autowired
    private ObavestenjaRukovalac porudzbinaRukovanje;

    @Autowired
    private AdminRacunRukovalac adminRacunRukovalac;

    @Autowired
    private KorisnikRacunRukovalac korisnikRacunRukovalac;

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @Autowired
    private NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;
    @Autowired
    private BazaRacunSajta bazaRacunSajta;

    @RequestMapping(value="/korisnikOtkazujePorudzbinu", method=RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody Map<String, Object> podaci
    ){
        Map<String, Object> naruceniProizvodMapa = (Map<String, Object>) podaci.get("naruceniProizvod");
        if (naruceniProizvodMapa == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        if (naruceniProizvodMapa.get("id") == null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String idString = naruceniProizvodMapa.get("id").toString();
        Integer id = this.konverzija.konverzijaInteger(idString);
        if (id == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        //provera cene kolicine  i ceneputakolicine

        NaruceniProizvod np = naruceniProizvodiRepozitorijum.vratiteNaruceniProizvodPoId(id);
        boolean rezultat = naruceniProizvodiRepozitorijum.otkazanNaruceniProizvod(np);
        if(rezultat){
            String poruka = String.format("Корисник %s је отказао поруџбину Вашег производа %s.",
                    np.getNarudzbenica().getKupac().getImeNaziv(), np.getProizvod().getNaziv());

            //tu ide posle vracanje para
            if(this.placanje.ponistitePlacanje(np.getNarudzbenica().getChargeId(), np.getCenaPutaKolicina())){
                this.upravljanjeRacunom.skiniteSaRacunaZaPrenos(np.getCenaPutaKolicina());
                //op op nisam poslao poruku
                this.porudzbinaRukovanje.posaljite(np.getProizvod().getKorisnik().getEmail(),poruka);
                this.adminRacunRukovalac.posaljite(upravljanjeRacunom.vratiteStanjeRacuna());
                this.korisnikRacunRukovalac.posaljite(np.getProizvod().getKorisnik().getEmail(), upravljanjeRacunom.vratiteStanjeRacunaKorisnika(np.getProizvod().getKorisnik().getEmail()));
            }
        }
        else{
            return postaviteSvejeURedu(false);
        }
        return postaviteSvejeURedu(rezultat);
    }



}
