package srb.akikrasic.zahtevi.novi.registrovanikorisnik.proizvod.unosproizvoda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import srb.akikrasic.baza.novipaket.baza.BazaKljucneReci;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import srb.akikrasic.zahtevi.novi.sigurnost.SigurnostProizvod;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.proizvod.unosproizvoda.RezultatUnosaProizvoda;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.slike.Slike;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by aki on 12/18/17.
 */
@RestController
public class UnosProizvodaRegistrovaniKorisnik extends Zahtev{

    @Autowired
    private Slike slike;

    @Autowired
    private ProizvodiRepozitorijum proizvodiRepozitorijum;

    @Autowired
    private BazaKljucneReci bazaKljucneReci;


    @Autowired
    private SigurnostProizvod sigurnostProizvod;


    @RequestMapping(value = "/korisnikUnosiProizvod", method = RequestMethod.POST)
    public RezultatRegistrovaniKorisnik izvrsiteZahtev(@RequestHeader("Authorization") String autorizacija,
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
        List<String> slikeIzZahteva = (List<String>) mapa.get("slike");
        if (slikeIzZahteva == null) {
            return dosloJeDoNeovlascenogPristupa();

        }
        //String idPrivremenogFolderaString = (String)mapa.get("idPrivremenogFoldera");

        Integer idPrivremenogFoldera = konverzija.konverzijaInteger( mapa.get("idPrivremenogFoldera").toString()); //this.konverzijaIdja(idPrivremenogFolderaString);
        if(idPrivremenogFoldera==null){
            return dosloJeDoNeovlascenogPristupa();
        }
        String email = this.emailIzAutorizacije(autorizacija);
        String nazivProizvoda = (String) proizvod.get("naziv");
        String opisProizvoda = (String) proizvod.get("opis");
        String cenaProizvodaString = (String) proizvod.get("trenutnacena");
        String idKategorijeString = (String) proizvod.get("kategorija_id");

        if (!sigurnostProizvod.sigurnosnaProveraUnosaProizvoda(nazivProizvoda, opisProizvoda, cenaProizvodaString, idKategorijeString)) {
            return dosloJeDoNeovlascenogPristupa();
        }
        Double cenaProizvoda = konverzija.konverzijaDouble(cenaProizvodaString);
        if (cenaProizvoda == null) {
            return dosloJeDoNeovlascenogPristupa();

        }
        if (cenaProizvoda < 0) {
            return dosloJeDoNeovlascenogPristupa();
        }
        Integer idKategorije = this.konverzija.konverzijaInteger(idKategorijeString);
        if (idKategorije == null) {
            return dosloJeDoNeovlascenogPristupa();
        }
        //parametri su uspesno preuzeti iz zahteva
        if (!sigurnostProizvod.sigurnosnaProveraListeKljucnihReci(kljucneReci)) {
            return dosloJeDoNeovlascenogPristupa();
        }
        //sigurnosna provera slika treba da se odradi
        if (!slike.sigurnosnaProveraSlika(slikeIzZahteva)) {
            return dosloJeDoNeovlascenogPristupa();

        }
        //da li id privremenogFoldera odgovara
        if (!slike.daLIKorisnikImaPravaDaSnimaUFolder(email, idPrivremenogFoldera)) {
            return dosloJeDoNeovlascenogPristupa();
        }
        //snimaju se podaci da se dobije id
        int proizvodId = proizvodiRepozitorijum.dodajteNoviProizvod(email, nazivProizvoda, opisProizvoda, cenaProizvoda, idKategorije);
        //snimaju se kljucne reci po id/ju, a prve se trazi svaka kljucna rec
        bazaKljucneReci.unesiteKljucneReci(kljucneReci, proizvodId);
        //premestaju se slike iz privremenog foldera u folder sa id-jem proizvoda
        slike.premestiteSlikeIzPrivrFolderaUFolderProizvoda(email, idPrivremenogFoldera, proizvodId);


        return vratiteRezultat();
    }

    private RezultatRegistrovaniKorisnik vratiteRezultat(){
        RezultatUnosaProizvoda r = new RezultatUnosaProizvoda();
        r.setDaLiJeURedu(true);
        return r;
    }


}
