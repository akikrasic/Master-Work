package srb.akikrasic.zahtevi.novi.registrovanikorisnik.naruceniproizvod.kupacpotvrdioprijem;

import org.springframework.beans.factory.annotation.Autowired;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.NaruceniProizvodiRepozitorijum;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.racuni.UpravljanjeRacunom;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.websocket.klase.AdminRacunRukovalac;
import srb.akikrasic.websocket.klase.KorisnikRacunRukovalac;
import srb.akikrasic.websocket.klase.ObavestenjaRukovalac;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class KupacPotvrdioPrijemRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @Autowired
    private UpravljanjeRacunom upravljanjeRacunom;

    @Autowired
    private NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;

    @Autowired
    private ObavestenjaRukovalac porudzbinaRukovanje;
    @Autowired
    private AdminRacunRukovalac adminRacunRukovalac;

    @Autowired
    private KorisnikRacunRukovalac korisnikRacunRukovalac;

    @RequestMapping(value = "/kupacPotvrdioPrijem", method = RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> param
    ) {
        if (param == null){
            return dosloJeDoNeovlascenogPristupa();

        }
        String idString = param.get("id").toString();
        Integer id = this.konverzija.konverzijaInteger(idString);
        if (id == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        NaruceniProizvod npIzBaze = naruceniProizvodiRepozitorijum.vratiteNaruceniProizvodPoId(id);
        Map<String, Object> proizvod = (Map<String, Object>) param.get("proizvod");
        if (proizvod == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        Map<String, Object> korisnik = (Map<String, Object>) proizvod.get("korisnik");
        if (korisnik == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        String email = (String) korisnik.get("email");
        if (email == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        /*String emailIzAutorizacije = this.emailIzAutorizacije(autorizacija);
        if(!email.equals(emailIzAutorizacije))return rez.neovlasceniPristup();*/
        if (!npIzBaze.getProizvod().getKorisnik().getEmail().equals(email)){
            return dosloJeDoNeovlascenogPristupa();
        }

        if(npIzBaze.isOtkazan()){
            return dosloJeDoNeovlascenogPristupa();
        }

        //malo treba da se refaktorise da na rukovaoce idu metode i cao
        naruceniProizvodiRepozitorijum.kupacPotvrdjujeNaruceniProizvod(npIzBaze);
        upravljanjeRacunom.prebaciteNaRacunKorisnika(npIzBaze.getProizvod().getKorisnik().getEmail(), npIzBaze.getCenaPutaKolicina());
        String poruka = String.format("Купац %s је потврдио пријем производа %s. На рачун Вам је легла сума у износу: %.2f %s.",
                npIzBaze.getNarudzbenica().getKupac().getImeNaziv(),
                npIzBaze.getProizvod().getNaziv(),
                npIzBaze.getCenaPutaKolicina(),
                this.dinarIliDinara(npIzBaze.getCenaPutaKolicina())
        );
        this.porudzbinaRukovanje.posaljite(npIzBaze.getProizvod().getKorisnik().getEmail(),
                poruka);
        this.adminRacunRukovalac.posaljite(upravljanjeRacunom.vratiteStanjeRacuna());
        this.korisnikRacunRukovalac.posaljite(npIzBaze.getProizvod().getKorisnik().getEmail(), upravljanjeRacunom.vratiteStanjeRacunaKorisnika(npIzBaze.getProizvod().getKorisnik().getEmail()));
        return sveJeURedu();

    }

}
