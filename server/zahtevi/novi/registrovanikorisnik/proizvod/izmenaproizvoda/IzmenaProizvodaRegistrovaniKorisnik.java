package srb.akikrasic.zahtevi.novi.registrovanikorisnik.proizvod.izmenaproizvoda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import srb.akikrasic.baza.novipaket.baza.BazaKljucneReci;
import srb.akikrasic.baza.novipaket.repozitorijum.KategorijeRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.domen.Kategorija;
import srb.akikrasic.domen.Proizvod;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import srb.akikrasic.zahtevi.novi.sigurnost.SigurnostProizvod;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.proizvod.izmenaproizvoda.RezultatIzmeneProizvoda;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class IzmenaProizvodaRegistrovaniKorisnik extends Zahtev {

    @Autowired
    private ProizvodiRepozitorijum proizvodiRepozitorijum;

    @Autowired
    private BazaKljucneReci bazaKljucneReci;

    @Autowired
    private KategorijeRepozitorijum kategorijeRepozitorijum;

    @Autowired
    private SigurnostProizvod sigurnostProizvod;

    @RequestMapping(value = "/korisnikIzmenaProizvoda", method = RequestMethod.PUT)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> mapa

    ) {
        LinkedHashMap<String, Object> proizvod = (LinkedHashMap<String, Object>) mapa.get("proizvod");
        if (proizvod == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        List<String> kljucneReci = (List<String>) mapa.get("kljucneReci");
        if (kljucneReci == null) {
            return dosloJeDoNeovlascenogPristupa();
        }

        String proizvodIdString = proizvod.get("id").toString();//mora tu da se prvo proveri da proizvod nije null
        Integer proizvodId = konverzija.konverzijaInteger(proizvodIdString);// this.konverzijaIdja(proizvodIdString);
        if (proizvodId == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        String email = this.emailIzAutorizacije(autorizacija);
        String nazivProizvoda = (String) proizvod.get("naziv");
        String opisProizvoda = (String) proizvod.get("opis");
        String cenaProizvodaString = proizvod.get("trenutnacena").toString();//to je objekat i pretvara se
        String idKategorijeString = proizvod.get("kategorija_id").toString();
        Object aktivanObj = proizvod.get("aktivan");
        if (aktivanObj == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        boolean aktivan = (Boolean) aktivanObj;
        //ne sme da bude null, ali sme da bude prazno
        if (
                email == null
                        || nazivProizvoda == null
                        || opisProizvoda == null
                        || cenaProizvodaString == null
                        || idKategorijeString == null
                ) {
            return dosloJeDoNeovlascenogPristupa();
        }
        Proizvod p = proizvodiRepozitorijum.vratiteProizvodPoId(proizvodId);
        if (p == null) {
            return dosloJeDoNeovlascenogPristupa();

        }
        //korisnik koji je uneo proizvod sme da ga menja
        if (!p.getKorisnik().getEmail().equals(email)) {
            return dosloJeDoNeovlascenogPristupa();
        }
        if (nazivProizvoda != "") {
            p.setNaziv(nazivProizvoda);
        }
        if (opisProizvoda != "") {
            p.setOpis(opisProizvoda);
        }
        if (!cenaProizvodaString.equals("")) {
            Double cena = konverzija.konverzijaDouble(cenaProizvodaString);
            if (cena == null) {
                return dosloJeDoNeovlascenogPristupa();
            }
            p.setTrenutnaCena(cena);
        }
        if (!idKategorijeString.equals("")) {
            Integer kategorijaId = konverzija.konverzijaInteger(idKategorijeString);
            if (kategorijaId == null) {
                return dosloJeDoNeovlascenogPristupa();
            }
            Kategorija kat = kategorijeRepozitorijum.vratiteKategorijuPoId(kategorijaId);
            p.setKategorija(kat);
        }
        p.setAktivan(aktivan);//tu nema greske
        if (!sigurnostProizvod.sigurnosnaProveraListeKljucnihReci(kljucneReci)) {
            return dosloJeDoNeovlascenogPristupa();
        }
        if (proizvodiRepozitorijum.izmeniteProizvodPodaci(p)) {
            bazaKljucneReci.izmeniteKljucneReci(kljucneReci, p.getId());
            return vratiteRezultat();
        } else {
            return vratiteRezultatPogresan();
        }
    }

    private RezultatRegistrovaniKorisnik vratiteRezultat(){
        RezultatIzmeneProizvoda r = new RezultatIzmeneProizvoda();
        r.setDaLiJeURedu(true);
        return r;
    }

    private RezultatRegistrovaniKorisnik vratiteRezultatPogresan(){
        RezultatIzmeneProizvoda r = new RezultatIzmeneProizvoda();
        r.setDaLiJeURedu(false);
        return r;
    }

}
