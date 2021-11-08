package srb.akikrasic.zahtevi.novi.svikorisnici.pretragaproizvoda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.novi.osnovne.Zahtev;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.pretragaproizvodazaizmenu.RezultatPretrageProizvoda;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aki on 9/7/17.
 */
@RestController
public class PretragaProizvoda extends Zahtev {

    @Autowired
    private ProizvodiRepozitorijum proizvodiRepozitorijum;

    public boolean sigurnosnaProveraImeNazivKorisnikaJeNull(String imeNazivKorisnika) {
        return imeNazivKorisnika == null;
    }

    public boolean sigurnosnaProveraEmailKorisnikaJeNull(String emailKorisnika) {
        return emailKorisnika == null;
    }

    public boolean sigurnosnaProveraNazivProizvodaJeNull(String nazivProizvoda) {
        return nazivProizvoda == null;
    }

    public boolean sigurnosnaProveraCenaOdJeNull(String cenaOd) {
        return cenaOd == null;
    }

    public boolean sigurnosnaProveraCenaDoJeNull(String cenaDo) {
        return cenaDo == null;
    }

    public boolean sigurnosnaProveraKategorijaNazivJeNull(String kategorijaNaziv) {
        return kategorijaNaziv == null;
    }

    public boolean sigurnosnaProveraOpisJeNull(String opis) {
        return opis == null;
    }

    public boolean sigurnosnaProveraKljucneReciJeNull(String kljucneReci) {
        return kljucneReci == null;
    }

    public boolean sigurnosnaProveraLimitOdJeNull(String limitOd) {
        return limitOd == null;
    }

    public boolean sigurnosnaProveraLimitDoJeNull(String limitDo) {
        return limitDo == null;
    }

    public boolean sigurnosnaProveraNull(String imeNazivKorisnika,
                                         String emailKorisnika,
                                         String nazivProizvoda,
                                         String cenaOd,
                                         String cenaDo,
                                         String kategorijaNaziv,
                                         String opis,
                                         String kljucneReci
                                         ) {
        if (sigurnosnaProveraImeNazivKorisnikaJeNull(imeNazivKorisnika)) return false;
        if (sigurnosnaProveraEmailKorisnikaJeNull(emailKorisnika)) return false;
        if (sigurnosnaProveraNazivProizvodaJeNull(nazivProizvoda)) return false;
        if (sigurnosnaProveraCenaOdJeNull(cenaOd)) return false;
        if (sigurnosnaProveraCenaDoJeNull(cenaDo)) return false;
        if (sigurnosnaProveraKategorijaNazivJeNull(kategorijaNaziv)) return false;
        if (sigurnosnaProveraOpisJeNull(opis)) return false;
        if (sigurnosnaProveraKljucneReciJeNull(kljucneReci)) return false;
      /*  if (sigurnosnaProveraLimitOdJeNull(limitOd)) return false;
        if (sigurnosnaProveraLimitDoJeNull(limitDo)) return false;*/
        return true;
    }

    //razlikuje se od ostalih konverzija po tome sto za prazan string vraca vrednost -1 koja se kasnije koristi
    //dok ostale konverzije i za prazan string vracaju null koji se tretira kao neispravan parametar
    public Double konverzijaDoubleVrednosti(String vr){
        Double vrednost = null;
        try{
            vrednost = Double.parseDouble(vr);
        }
        catch(Exception e){
            if(vr.equals(""))return -1.0;
            return null;
        }
        return vrednost;
    }
    public Integer konverzijaIntegerVrednosti(String vr){
        Integer vrednost = null;
        try{
            vrednost = Integer.parseInt(vr);
        }
        catch(Exception e){
            if(vr.equals(""))return -1;
            return null;
        }
        return vrednost;
    }
    public List<String> razdvajanjeKljucnihReci(String kljucneReci){
        kljucneReci = kljucneReci.trim();
        List<String> lista = new ArrayList<>();
        String[] niz = kljucneReci.split(" ");
        for(int i=0;i<niz.length;i++){
            if(!niz[i].equals("")&&!niz[i].equals(" "))lista.add(niz[i]);
        }
        return lista;
    }

    @RequestMapping(value = "pretragaProizvoda", method = RequestMethod.GET)
    public RezultatRegistrovaniKorisnik pretragaProizvoda(
            @RequestParam("imeNazivKorisnika") String imeNazivKorisnika,
            @RequestParam("emailKorisnika") String emailKorisnika,
            @RequestParam("nazivProizvoda") String nazivProizvoda,
            @RequestParam("cenaOd") String cenaOd,
            @RequestParam("cenaDo") String cenaDo,
            @RequestParam("kategorijaNaziv") String kategorijaNaziv,
            @RequestParam("opis") String opis,
            @RequestParam("kljucneReci") String kljucneReci

    ) {
        if (!sigurnosnaProveraNull(imeNazivKorisnika,
                emailKorisnika,
                nazivProizvoda,
                cenaOd,
                cenaDo,
                kategorijaNaziv,
                opis,
                kljucneReci)) {
            return dosloJeDoNeovlascenogPristupa();

        }
        //idemo druga provera da se brojevi konvertuju u numericki format cenaodcenado limitod limitdo, znaci to su 4 broja 2 int 2 double
        Double cenaOdDouble = this.konverzijaDoubleVrednosti(cenaOd);
        Double cenaDoDouble = this.konverzijaDoubleVrednosti(cenaDo);
       /* Integer limitOdInt = this.konverzijaIntegerVrednosti(limitOd);
        Integer limitDoInt = this.konverzijaIntegerVrednosti(limitDo);
        if(cenaOdDouble==null||cenaDoDouble==null||limitOdInt==null||limitDoInt==null){
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }*/
       //to treba se prepravi posle kad refaktorisem da rezultat bude tu a da  vracam rezultat sam po sebi
        List<String> kljucneReciLista = this.razdvajanjeKljucnihReci(kljucneReci);

        List<Proizvod> listaProizvoda = proizvodiRepozitorijum.pretragaProizvoda(imeNazivKorisnika,
                emailKorisnika,
                nazivProizvoda,
                cenaOdDouble,
                cenaDoDouble,
                kljucneReciLista,
                kategorijaNaziv,
                opis) ;
        //dodato 3.12.2017. da se lepo prebaci nadleznost tu se prave liste tamo se samo radi sa bazom

        return vratiteRezultat(listaProizvoda);
        /*
        if(r!=null){//to treba da podesim ako izbije neka greska slucajno da vrne null
            r.setDaLiJeURedu(true);

        }
        else{
            r.setDaLiJeURedu(false);
        }*/
    }

    private RezultatRegistrovaniKorisnik vratiteRezultat(List<Proizvod> listaProizvoda){
        RezultatPretrageProizvoda r = new RezultatPretrageProizvoda();
        r.setDaLiJeURedu(true);
        r.setLista(listaProizvoda);
        r.setUkBrojProizvoda(listaProizvoda.size());
        return r;

    }

}
