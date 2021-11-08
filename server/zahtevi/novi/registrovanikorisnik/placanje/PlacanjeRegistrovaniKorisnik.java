package srb.akikrasic.zahtevi.novi.registrovanikorisnik.placanje;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.NarudzbeniceRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.domen.Narudzbenica;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.racuni.UpravljanjeRacunom;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.stripe.RadSaPlacanjem;
import srb.akikrasic.websocket.klase.ObavestenjaRukovalac;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
/**
 * Created by aki on 12/18/17.
 */
@RestController
public class PlacanjeRegistrovaniKorisnik extends Zahtev{

    @Autowired
    private UpravljanjeRacunom upravljanjeRacunom;

    @Autowired
    private RadSaPlacanjem placanje;

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @Autowired
    private ProizvodiRepozitorijum proizvodiRepozitorijum;

    @Autowired
    private NarudzbeniceRepozitorijum narudzbeniceRepozitorijum;

    @Autowired
    private ObavestenjaRukovalac porudzbinaRukovanje;

    public boolean sigurnosnaProveraNullPlacanje(Map<String, Object> mapa) {
        if (mapa.get("proizvodiKolicineKomentari") == null) return false;
        if (mapa.get("token") == null) return false;
        if (mapa.get("dostava") == null) return false;
        if (mapa.get("zbir") == null) return false;

        return true;
    }

    public boolean sigurnosnaProveraNullProizvodKolicinaKomentari(Map<String, Object> mapa) {
        if (mapa.get("proizvod") == null) return false;
        if (mapa.get("komentar") == null) return false;
        if (mapa.get("kolicina") == null) return false;
        if (mapa.get("cenaPutaKolicina") == null) return false;
        return true;

    }

    public boolean proveraProizvodJeUVlasnistvuKupca(Proizvod p, Korisnik kupac) {
        return p.getKorisnik().equals(kupac);
    }
    
    
    @RequestMapping(value = "/placanjeRegistrovaniKorisnik", method = RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> mapa
    ) {


        if (!sigurnosnaProveraNullPlacanje(mapa)) return dosloJeDoNeovlascenogPristupa();
        List<Map<String, Object>> proizvodiKolicineKomentari = (List<Map<String, Object>>) mapa.get("proizvodiKolicineKomentari");

          /*Map<String, Object> proizvodMapa =(Map<String, Object>)proizvodiKolicineKomentari.get("proizvod");
          String kolicina = (String)proizvodiKolicineKomentari.get("kolicina");
          String komentar = (String)proizvodiKolicineKomentari.get("komentar");
          String cenaPutaKolicina*/
        String token = (String) mapa.get("token");
        String dostava = (String) mapa.get("dostava");

        String zbirString = String.valueOf((Integer) mapa.get("zbir"));
        Double zbir = this.konverzija.konverzijaDouble(zbirString);
        if (zbir == null) {
            dosloJeDoNeovlascenogPristupa();
        }

        String email = this.emailIzAutorizacije(autorizacija);
        Korisnik kupac = korisniciRepozitorijum.pretragaKorisnikaPoEmailu(email);
        if (!dostava.equals("Курирска служба") && !dostava.equals("Лично преузимање")) {
            return dosloJeDoNeovlascenogPristupa();
        }

        Narudzbenica n = new Narudzbenica();
        n.setDostava(dostava);
        n.setZbir(zbir);
        n.setDatum(LocalDateTime.now());
        n.setKupac(kupac);
        double zbir1 = 0;
        for (Map<String, Object> m : proizvodiKolicineKomentari) {
            if (!sigurnosnaProveraNullProizvodKolicinaKomentari(m))
                return dosloJeDoNeovlascenogPristupa();
            Map<String, Object> proizvodMapa = (Map<String, Object>) m.get("proizvod");
            String kolicinaString = (String) m.get("kolicina");
            Double kolicina = this.konverzija.konverzijaDouble(kolicinaString);
            if (kolicina == null) {
                return dosloJeDoNeovlascenogPristupa();
            }
            String komentar = (String) m.get("komentar");

            //25.01.2018.
            if(komentar==null){
                return dosloJeDoNeovlascenogPristupa();
            }

            String cenaPutaKolicinaString = String.valueOf(m.get("cenaPutaKolicina"));
            Double cenaPutaKolicina = this.konverzija.konverzijaDouble(cenaPutaKolicinaString);
            if (cenaPutaKolicina == null) {
                return dosloJeDoNeovlascenogPristupa();
            }
            String proizvodIdString = String.valueOf(proizvodMapa.get("id"));
            Integer proizvodId = this.konverzija.konverzijaInteger(proizvodIdString);
            if (proizvodId == null) {
                return dosloJeDoNeovlascenogPristupa();
            }
            Proizvod proizvod = proizvodiRepozitorijum.vratiteProizvodPoId(proizvodId);
            if (proveraProizvodJeUVlasnistvuKupca(proizvod, kupac)) {
                return dosloJeDoNeovlascenogPristupa();
            }
            double cena = proizvod.getTrenutnaCena();
            String cenaString = String.valueOf(proizvodMapa.get("trenutnaCena"));
            Double cenaIzZahtev = this.konverzija.konverzijaDouble(cenaString);
            if (cenaIzZahtev == null) {
                return dosloJeDoNeovlascenogPristupa();
            }
            if (cenaIzZahtev != cena) {
                return dosloJeDoNeovlascenogPristupa();
            }
            if (cena * kolicina != cenaPutaKolicina)
                return dosloJeDoNeovlascenogPristupa();
            NaruceniProizvod np = new NaruceniProizvod();
            np.setCenaPutaKolicina(cenaPutaKolicina);
            np.setCena(cena);
            np.setKolicina(kolicina);
            np.setProizvod(proizvod);
            np.setNarudzbenica(n);
            np.setKomentar(komentar);
            n.getNaruceniProizvodi().add(np);
            zbir1 += cenaPutaKolicina;
        }
        if (zbir != zbir1) {
            return dosloJeDoNeovlascenogPristupa();
        }
        String chargeId = placanje.platiteIVratiteIdChargea(token, zbir);
        if (chargeId == null) {
            return greska();
        }
        n.setChargeId(chargeId);
        this.upravljanjeRacunom.dodajteNaRacunZaPrenos(zbir);
        narudzbeniceRepozitorijum.snimiteNarudzbenicu(n);
        n.getNaruceniProizvodi().forEach(np -> {
            String poruka = String.format("Стигла Вам је поруџбина за производ %s: Количина: %.2f Укупни износ: %.2f %s.",
                    np.getProizvod().getNaziv(), np.getKolicina(), np.getCenaPutaKolicina(), dinarIliDinara(np.getCenaPutaKolicina()));
            this.porudzbinaRukovanje.posaljite(np.getProizvod().getKorisnik().getEmail(), poruka);
        });
        return sveJeURedu();
    }

}
