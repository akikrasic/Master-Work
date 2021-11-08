package srb.akikrasic.zahtevi.stari.registrovanikorisnik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.domen.*;
import srb.akikrasic.racuni.UpravljanjeRacunom;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.*;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.stripe.RadSaPlacanjem;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Sifra;
import srb.akikrasic.token.Tokeni;
import srb.akikrasic.websocket.klase.AdminRacunRukovalac;
import srb.akikrasic.websocket.klase.KorisnikRacunRukovalac;
import srb.akikrasic.websocket.klase.ObavestenjaRukovalac;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.izmenapodatakadobavljanje.RezultatIzmenePodatakaDobavljanje;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.izmenapodatakaproces.RezultatIzmenePodatakaProces;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.proizvod.izmenaproizvoda.RezultatIzmeneProizvoda;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.korisnik.odjava.RezultatOdjaveRegistrovaniKorisnik;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.proizvod.sviproizvodikorisnika.RezultatSviProizvodiKorisnika;
import srb.akikrasic.zahtevi.rezultati.novi.registrovanikorisnik.proizvod.unosproizvoda.RezultatUnosaProizvoda;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by aki on 8/13/17.
 */
//@RestController
public class RegistrovaniKorisnikPrijemZahteva {
    @Autowired
    private Tokeni radSaTokenima;

    @Autowired
    private Baza b;

    @Autowired
    private GenerisanjeTokena radSaGenerisanjemTokena;

    @Autowired
    private Sifra radSaSifrom;
    @Autowired
    private Slike slike;
    @Autowired
    private RezultatRegistrovani rez;
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

    //napomena vrednost regularnog izraza je preuzeta sa adrese: http://emailregex.com/
    private Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private Pattern sestIVisePattern = Pattern.compile(".{6,}");
    private Pattern maloSlovoPattern = Pattern.compile("^.{0,}[a-zа-ш]{1,}.{0,}");
    private Pattern velikoSlovoPattern = Pattern.compile("^.{0,}[A-ZА-Ш]{1,}.{0,}");
    private Pattern brojPattern = Pattern.compile("^.{0,}[0-9]{1,}.{0,}");
    private Pattern specKarakteriPattern = Pattern.compile("^.{0,}[!@#$%^&*()]{1,}.{0,}");
    private Pattern telefonPattern = Pattern.compile("^[0-9]{8,}$");


    @RequestMapping(value = "/korisnikOdjava", method = RequestMethod.GET)
    public RezultatOdjaveRegistrovaniKorisnik korisnikOdjava(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String email,
            @RequestParam String token) {

        RezultatOdjaveRegistrovaniKorisnik r = new RezultatOdjaveRegistrovaniKorisnik();
        r.setOdjavaUspesna(radSaTokenima.korisnikOdjava(email, token));
        return r;

    }

    @RequestMapping(value = "/korisnikIzmenaPodatakaDobavljanje", method = RequestMethod.GET)
    public RezultatIzmenePodatakaDobavljanje nabavitePodatke(@RequestHeader("Authorization") String autorizacija) {
        String dekodiran = radSaGenerisanjemTokena.dekodujteURLFormat(autorizacija);
        String email = dekodiran.split(":")[0];
        RezultatIzmenePodatakaDobavljanje r = new RezultatIzmenePodatakaDobavljanje();
        r.setKorisnik(b.pretragaKorisnikaPoEmailu(email));
        r.setDaLiJeURedu(true);
        return r;
    }

    private boolean sigurnosnaProveraIzmenjenihPodataka(String imeNaziv, String sifra, String adresa, String mesto, String tel1, String tel2, String tel3) {
        if (imeNaziv == null || sifra == null || adresa == null || mesto == null || tel1 == null || tel2 == null || tel3 == null)
            return false;
        if (imeNaziv.equals("")) return false;

        if (!sifra.equals("")) {
            if (!sestIVisePattern.matcher(sifra).matches()) return false;
            if (!maloSlovoPattern.matcher(sifra).matches()) return false;
            if (!velikoSlovoPattern.matcher(sifra).matches()) return false;
            if (!brojPattern.matcher(sifra).matches()) return false;
            if (!specKarakteriPattern.matcher(sifra).matches()) return false;
        }
        if (adresa.equals("")) return false;
        if (mesto.equals("")) return false;
        if (tel1.equals("")) return false;
        if (!telefonPattern.matcher(tel1).matches()) return false;
        if (!tel2.equals(""))
            if (!telefonPattern.matcher(tel2).matches()) return false;
        if (!tel3.equals(""))
            if (!telefonPattern.matcher(tel3).matches()) return false;

        return true;
    }

    private String emailIzAutorizacije(String autorizacija) {
        return (radSaGenerisanjemTokena.dekodujteURLFormat(autorizacija)).split(":")[0];
    }


    @RequestMapping(value = "/korisnikIzmenaPodatakaProces", method = RequestMethod.PUT)
    public RezultatIzmenePodatakaProces izmenitePodatke(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> param
    ) {
        RezultatIzmenePodatakaProces r = new RezultatIzmenePodatakaProces();
        String email = emailIzAutorizacije(autorizacija);
        String imeNaziv = (String) param.get("imeNaziv");
        byte[] sifra = ((String) param.get("sifra")).getBytes();
        String adresa = (String) param.get("adresa");
        String mesto = (String) param.get("mesto");
        String tel1 = (String) param.get("tel1");
        String tel2 = (String) param.get("tel2");
        String tel3 = (String) param.get("tel3");
        //sada moramo da proverimo da li su parametri validni
        if (sigurnosnaProveraIzmenjenihPodataka(imeNaziv, new String(sifra), adresa, mesto, tel1, tel2, tel3)) {
            r.setDaLiJeURedu(b.izmenitePodatkeZaKorisnika(imeNaziv, email, sifra, adresa, mesto, tel1, tel2, tel3));

        } else {
            r.dosloJeDoNeovlascenogPristupa();
        }

        return r;
    }

    public Baza getB() {
        return b;
    }

    public void setB(Baza b) {
        this.b = b;
    }


    public boolean sigurnosnProveraNazivaProizvoda(String nazivProizvoda) {
        if (nazivProizvoda == null || nazivProizvoda.equals("")) return false;
        return true;
    }

    public boolean sigurnosnProveraOpisaProizvoda(String opisProizvoda) {
        if (opisProizvoda == null || opisProizvoda.equals("")) return false;
        return true;
    }

    public boolean sigurnosnaProveraKategorije(String idKategorije) {
        if (idKategorije == null || idKategorije.equals("")) return false;
        return /*!b.daLiSeNazivKategorijeNalaziUBazi(nazivKategorije);*/true;
    }

    public boolean sigurnosnaProveraCene(String cenaString) {
        if (cenaString == null || cenaString.equals("")) return false;
        return true;
    }

    public Double konverzijaCene(String cenaString) {
        Double d = null;
        try {
            d = Double.parseDouble(cenaString);
        } catch (Exception e) {
            return null;
        }
        return d;
    }

    public Integer konverzijaKategorije(String idString) {
        Integer i = null;

        try {
            i = Integer.parseInt(idString);
        } catch (Exception e) {
            return null;
        }

        return i;

    }

    public Integer konverzijaIdja(String idString) {
        return this.konverzijaKategorije(idString);
        //posteno

    }

    private boolean sigurnosnaProveraUnosaProizvoda(String nazivProizvoda, String opisProizvoda, String cenaProizvoda, String idKategorije) {
        if (!this.sigurnosnProveraNazivaProizvoda(nazivProizvoda)) return false;
        if (!this.sigurnosnProveraOpisaProizvoda(opisProizvoda)) return false;
        if (!this.sigurnosnaProveraCene(cenaProizvoda)) return false;
        return this.sigurnosnaProveraKategorije(idKategorije);
    }

    public boolean sigurnosnaProveraListeKljucnihReci(List<String> lista) {
        if (lista == null || lista.isEmpty()) return false;
        for (String s : lista) {
            if (s == null || s.equals("")) return false;
        }
        return true;
    }

    public Boolean konverzijaAktian(String aktivan) {
        Boolean b = null;
        try {
            b = Boolean.parseBoolean(aktivan);
        } catch (Exception e) {
            return null;
        }
        return b;
    }

    @RequestMapping(value = "/korisnikUnosiProizvod", method = RequestMethod.POST)
    public RezultatUnosaProizvoda unosProizvoda(@RequestHeader("Authorization") String autorizacija,
                                                @RequestBody LinkedHashMap<String, Object> mapa
    ) {
        RezultatUnosaProizvoda r = new RezultatUnosaProizvoda();
        LinkedHashMap<String, Object> proizvod = (LinkedHashMap<String, Object>) mapa.get("proizvod");
        if (proizvod == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        List<String> kljucneReci = (List<String>) mapa.get("kljucneReci");
        if (kljucneReci == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        List<String> slikeIzZahteva = (List<String>) mapa.get("slike");
        if (slikeIzZahteva == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        //String idPrivremenogFolderaString = (String)mapa.get("idPrivremenogFoldera");


        Integer idPrivremenogFoldera = (Integer) mapa.get("idPrivremenogFoldera"); //this.konverzijaIdja(idPrivremenogFolderaString);
      /*  if(idPrivremenogFoldera==null){
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }*/
        String email = this.emailIzAutorizacije(autorizacija);
        String nazivProizvoda = (String) proizvod.get("naziv");
        String opisProizvoda = (String) proizvod.get("opis");
        String cenaProizvodaString = (String) proizvod.get("trenutnacena");
        String idKategorijeString = (String) proizvod.get("kategorija_id");

        if (!sigurnosnaProveraUnosaProizvoda(nazivProizvoda, opisProizvoda, cenaProizvodaString, idKategorijeString)) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        Double cenaProizvoda = this.konverzijaCene(cenaProizvodaString);
        if (cenaProizvoda == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        if (cenaProizvoda < 0) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        Integer idKategorije = this.konverzijaKategorije(idKategorijeString);
        if (idKategorije == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        //parametri su uspesno preuzeti iz zahteva
        if (!sigurnosnaProveraListeKljucnihReci(kljucneReci)) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        //sigurnosna provera slika treba da se odradi 
        if (!slike.sigurnosnaProveraSlika(slikeIzZahteva)) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        //da li id privremenogFoldera odgovara
        if (!slike.daLIKorisnikImaPravaDaSnimaUFolder(email, idPrivremenogFoldera)) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        //snimaju se podaci da se dobije id
        int proizvodId = b.dodajteNoviProizvod(email, nazivProizvoda, opisProizvoda, cenaProizvoda, idKategorije);
        //snimaju se kljucne reci po id/ju, a prve se trazi svaka kljucna rec
        b.unesiteKljucneReci(kljucneReci, proizvodId);
        //premestaju se slike iz privremenog foldera u folder sa id-jem proizvoda
        slike.premestiteSlikeIzPrivrFolderaUFolderProizvoda(email, idPrivremenogFoldera, proizvodId);

        r.setDaLiJeURedu(true);

        return r;
    }

    @RequestMapping(value = "/korisnikIzmenaProizvoda", method = RequestMethod.PUT)
    public RezultatIzmeneProizvoda izmenaProizvoda(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> mapa

    ) {
        RezultatIzmeneProizvoda r = new RezultatIzmeneProizvoda();
        LinkedHashMap<String, Object> proizvod = (LinkedHashMap<String, Object>) mapa.get("proizvod");
        if (proizvod == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        List<String> kljucneReci = (List<String>) mapa.get("kljucneReci");
        if (kljucneReci == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }

        String proizvodIdString = proizvod.get("id").toString();//mora tu da se prvo proveri da proizvod nije null
        Integer proizvodId = (Integer) proizvod.get("id");// this.konverzijaIdja(proizvodIdString);
        if (proizvodId == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        String email = this.emailIzAutorizacije(autorizacija);
        String nazivProizvoda = (String) proizvod.get("naziv");
        String opisProizvoda = (String) proizvod.get("opis");
        String cenaProizvodaString = proizvod.get("trenutnacena").toString();//to je objekat i pretvara se
        String idKategorijeString = proizvod.get("kategorija_id").toString();
        Object aktivanObj = proizvod.get("aktivan");
        if (aktivanObj == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
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
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        Proizvod p = b.vratiteProizvodPoId(proizvodId);
        if (p == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        //korisnik koji je uneo proizvod sme da ga menja
        if (!p.getKorisnik().getEmail().equals(email)) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        if (nazivProizvoda != "") {
            p.setNaziv(nazivProizvoda);
        }
        if (opisProizvoda != "") {
            p.setOpis(opisProizvoda);
        }
        if (!cenaProizvodaString.equals("")) {
            Double cena = konverzijaCene(cenaProizvodaString);
            if (cena == null) {
                r.dosloJeDoNeovlascenogPristupa();
                return r;
            }
            p.setTrenutnaCena(cena);

        }
        if (!idKategorijeString.equals("")) {
            Integer kategorijaId = konverzijaKategorije(idKategorijeString);
            if (kategorijaId == null) {
                r.dosloJeDoNeovlascenogPristupa();
                return r;
            }
            Kategorija kat = b.vratiteKategorijuPoId(kategorijaId);
            p.setKategorija(kat);

        }
        p.setAktivan(aktivan);//tu nema greske
        if (!sigurnosnaProveraListeKljucnihReci(kljucneReci)) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        if (b.izmeniteProizvodPodaci(p)) {
            r.setDaLiJeURedu(true);
            b.izmeniteKljucneReci(kljucneReci, p.getId());
        } else {
            r.setDaLiJeURedu(false);
        }
        return r;
    }

    @RequestMapping(value = "/vratiteSveProizvodeKorisnika", method = RequestMethod.GET)
    public RezultatSviProizvodiKorisnika sviProizvodiKorisnik(
            @RequestHeader("Authorization") String autorizacija
    ) {
        RezultatSviProizvodiKorisnika r = new RezultatSviProizvodiKorisnika();
        r.setDaLiJeURedu(true);
        String email = this.emailIzAutorizacije(autorizacija);
        r.setProizvodi(b.vratiteSveProizvodeNekogKorisnikaPoEmailuDaIhOnMenja(email));
        r.setUkBrojProizvoda(r.getProizvodi().size());
        return r;
    }

    @RequestMapping(value = "/korisnikPromeniteSamoCenu", method = RequestMethod.PUT)
    public RezultatRegistrovaniKorisnik izmeniteSamoCenu(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> mapa
    ) {
        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        String email = this.emailIzAutorizacije(autorizacija);
        String proizvodIdString = mapa.get("id").toString();
        if (proizvodIdString == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        Integer proizvodId = this.konverzijaIdja(proizvodIdString);
        if (proizvodId == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        Proizvod p = b.vratiteProizvodPoId(proizvodId);
        if (!p.getKorisnik().getEmail().equals(email)) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        String novaCenaProizvodaString = mapa.get("novacena").toString();
        Double novaCena = this.konverzijaCene(novaCenaProizvodaString);
        if (novaCena == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        p.setTrenutnaCena(novaCena);
        b.izmeniteProizvodCenu(p);
        r.setDaLiJeURedu(true);

        return r;
    }

    @RequestMapping(value = "/korisnikPromeniteDaLiJeAktivan", method = RequestMethod.PUT)
    public RezultatRegistrovaniKorisnik izmeniteSamoDaLiJeAktivan(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> mapa
    ) {
        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        String email = this.emailIzAutorizacije(autorizacija);
        String proizvodIdString = mapa.get("id").toString();
        if (proizvodIdString == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        Integer proizvodId = this.konverzijaIdja(proizvodIdString);
        if (proizvodId == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        Proizvod p = b.vratiteProizvodPoId(proizvodId);
        if (!p.getKorisnik().getEmail().equals(email)) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        String aktivanString = mapa.get("aktivan").toString();
        if (aktivanString == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        Boolean daLiJeAktivan = this.konverzijaAktian(aktivanString);
        if (daLiJeAktivan == null) {
            r.dosloJeDoNeovlascenogPristupa();
            return r;
        }
        p.setAktivan(daLiJeAktivan);
        b.izmeniteProizvodAktivan(p);
        r.setDaLiJeURedu(true);


        return r;
    }

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
    public RezultatRegistrovaniKorisnik placanjeNarudzbine(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> mapa
    ) {


        if (!sigurnosnaProveraNullPlacanje(mapa)) return rez.neovlasceniPristup();
        List<Map<String, Object>> proizvodiKolicineKomentari = (List<Map<String, Object>>) mapa.get("proizvodiKolicineKomentari");

          /*Map<String, Object> proizvodMapa =(Map<String, Object>)proizvodiKolicineKomentari.get("proizvod");
          String kolicina = (String)proizvodiKolicineKomentari.get("kolicina");
          String komentar = (String)proizvodiKolicineKomentari.get("komentar");
          String cenaPutaKolicina*/
        String token = (String) mapa.get("token");
        String dostava = (String) mapa.get("dostava");

        String zbirString = String.valueOf((Integer) mapa.get("zbir"));
        Double zbir = this.konverzijaCene(zbirString);
        if (zbir == null) {
            rez.neovlasceniPristup();
        }

        String email = this.emailIzAutorizacije(autorizacija);
        Korisnik kupac = b.pretragaKorisnikaPoEmailu(email);
        if (!dostava.equals("Курирска служба") && !dostava.equals("Лично преузимање")) {
            return rez.neovlasceniPristup();
        }

        Narudzbenica n = new Narudzbenica();
        n.setDostava(dostava);
        n.setZbir(zbir);
        n.setDatum(LocalDateTime.now());
        n.setKupac(kupac);
        double zbir1 = 0;
        for (Map<String, Object> m : proizvodiKolicineKomentari) {
            if (!sigurnosnaProveraNullProizvodKolicinaKomentari(m))
                return rez.neovlasceniPristup();
            Map<String, Object> proizvodMapa = (Map<String, Object>) m.get("proizvod");
            String kolicinaString = (String) m.get("kolicina");
            Double kolicina = this.konverzijaCene(kolicinaString);
            if (kolicina == null) {
                return rez.neovlasceniPristup();
            }
            String komentar = (String) m.get("komentar");

            String cenaPutaKolicinaString = String.valueOf(m.get("cenaPutaKolicina"));
            Double cenaPutaKolicina = this.konverzijaCene(cenaPutaKolicinaString);
            if (cenaPutaKolicina == null) {
                return rez.neovlasceniPristup();
            }
            String proizvodIdString = String.valueOf(proizvodMapa.get("id"));
            Integer proizvodId = this.konverzijaKategorije(proizvodIdString);
            if (proizvodId == null) {
                return rez.neovlasceniPristup();
            }

            Proizvod proizvod = b.vratiteProizvodPoId(proizvodId);
            if (proveraProizvodJeUVlasnistvuKupca(proizvod, kupac)) {
                return rez.neovlasceniPristup();
            }
            double cena = proizvod.getTrenutnaCena();
            String cenaString = String.valueOf(proizvodMapa.get("trenutnaCena"));
            Double cenaIzZahtev = this.konverzijaCene(cenaString);
            if (cenaIzZahtev == null) {
                return rez.neovlasceniPristup();
            }
            if (cenaIzZahtev != cena) {
                return rez.neovlasceniPristup();
            }
            if (cena * kolicina != cenaPutaKolicina)
                return rez.neovlasceniPristup();


            NaruceniProizvod np = new NaruceniProizvod();
            np.setCenaPutaKolicina(cenaPutaKolicina);
            np.setCena(cena);
            np.setKolicina(kolicina);
            np.setProizvod(proizvod);
            np.setNarudzbenica(n);
            n.getNaruceniProizvodi().add(np);
            zbir1 += cenaPutaKolicina;
        }
        if (zbir != zbir1) {
            return rez.neovlasceniPristup();
        }
        String chargeId = placanje.platiteIVratiteIdChargea(token, zbir);
        if (chargeId == null) {
            return rez.greska();
        }
        n.setChargeId(chargeId);
        this.upravljanjeRacunom.dodajteNaRacunZaPrenos(zbir);
        b.snimiteNarudzbenicu(n);
        n.getNaruceniProizvodi().forEach(np -> {
            String poruka = String.format("Стигла Вам је поруџбина за производ %s: Количина: %.2f Укупни износ: %.2f %s.",
                    np.getProizvod().getNaziv(), np.getKolicina(), np.getCenaPutaKolicina(), dinarIliDinara(np.getCenaPutaKolicina()));
            this.porudzbinaRukovanje.posaljite(np.getProizvod().getKorisnik().getEmail(), poruka);
        });
        return rez.sveJeURedu();
    }

    private String dinarIliDinara(double broj) {
        if (broj % 10 == 1) return "динар";
        return "динара";
    }


    @RequestMapping(value = "/potvrditeSlanjeProizvoda", method = RequestMethod.POST)
    public RezultatRegistrovaniKorisnik potvrditeSlanjeProizvoda(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> param
    ) {
        if (param == null)
            return rez.neovlasceniPristup();
        String idString = param.get("id").toString();
        Integer id = this.konverzijaIdja(idString);
        if (id == null) {
            return rez.neovlasceniPristup();
        }
        NaruceniProizvod npIzBaze = b.vratiteNaruceniProizvodPoId(id);
        Map<String, Object> proizvod = (Map<String, Object>) param.get("proizvod");
        if (proizvod == null) return rez.neovlasceniPristup();
        Map<String, Object> korisnik = (Map<String, Object>) proizvod.get("korisnik");
        if (korisnik == null) return rez.neovlasceniPristup();
        String email = (String) korisnik.get("email");
        if (email == null) return rez.neovlasceniPristup();
        String emailIzAutorizacije = this.emailIzAutorizacije(autorizacija);
        if (!email.equals(emailIzAutorizacije)) return rez.neovlasceniPristup();
        if (!npIzBaze.getProizvod().getKorisnik().getEmail().equals(email)) return rez.neovlasceniPristup();
        b.prodavacPotvrdjujeNaruceniProizvod(npIzBaze);
        String poruka = String.format("Продавац %s Вам је послао поручени производ: %s.",
                npIzBaze.getProizvod().getKorisnik().getImeNaziv(),
                npIzBaze.getProizvod().getNaziv());
        this.porudzbinaRukovanje.posaljite(npIzBaze.getNarudzbenica().getKupac().getEmail(),
                poruka);
        return rez.sveJeURedu();
    }

    private boolean pocetakPomerajuRedu(int pocetak, int pomeraj) {
        if (pocetak > -1 && pomeraj > -1 ) return true;
        return false;

    }

    @RequestMapping(value = "/vratiteNarudzbeniceKupcu", method = RequestMethod.GET)
    public RezultatRegistrovaniKorisnik vratiteSveNarudzbeniceKupca(
            @RequestHeader("Authorization") String autorizacija

    ) {
        String email = this.emailIzAutorizacije(autorizacija);
        return rez.rezultatNaruceniProizvodiKupca(b.vratiteNarudzbeniceKupcu(email));
    }

    @RequestMapping(value = "/vratiteNarudzbeniceKupcuLimit", method = RequestMethod.GET)
    public RezultatRegistrovaniKorisnik vratiteSveNarudzbeniceKupcaLimit(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam int pocetak,
            @RequestParam int pomeraj
    ) {
        if (!pocetakPomerajuRedu(pocetak, pomeraj)) return rez.neovlasceniPristup();
        String email = this.emailIzAutorizacije(autorizacija);
        return rez.rezultatNaruceniProizvodiKupca(b.vratiteNarudzbeniceKupcuLimit(email, pocetak, pomeraj));
    }

    @RequestMapping(value = "/naruceniProizvodiProdavca", method = RequestMethod.GET)
    public RezultatRegistrovaniKorisnik vratiteNarucenePorizvodeProdavca(
            @RequestHeader("Authorization") String autorizacija
    ) {
        String email = this.emailIzAutorizacije(autorizacija);
        return rez.rezultatNaruceniProizvodiProdavca(b.vratiteNaruceneProizvodeProdavca(email));
    }

    @RequestMapping(value = "/naruceniProizvodiProdavcaLimit", method = RequestMethod.GET)
    public RezultatRegistrovaniKorisnik vratiteNarucenePorizvodeProdavcaLimit(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam int pocetak,
            @RequestParam int pomeraj
    ) {
        if (!pocetakPomerajuRedu(pocetak, pomeraj))
            return rez.neovlasceniPristup();
        String email = this.emailIzAutorizacije(autorizacija);
        return rez.rezultatNaruceniProizvodiProdavca(b.vratiteNaruceneProizvodeProdavcaLimit(email, pocetak, pomeraj));
    }

    @RequestMapping(value = "/kupacPotvrdioPrijem", method = RequestMethod.POST)
    public RezultatRegistrovaniKorisnik potvrditePrijem(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> param
    ) {
        if (param == null)
            return rez.neovlasceniPristup();
        String idString = param.get("id").toString();
        Integer id = this.konverzijaIdja(idString);
        if (id == null) {
            return rez.neovlasceniPristup();
        }
        NaruceniProizvod npIzBaze = b.vratiteNaruceniProizvodPoId(id);
        Map<String, Object> proizvod = (Map<String, Object>) param.get("proizvod");
        if (proizvod == null) return rez.neovlasceniPristup();
        Map<String, Object> korisnik = (Map<String, Object>) proizvod.get("korisnik");
        if (korisnik == null) return rez.neovlasceniPristup();
        String email = (String) korisnik.get("email");
        if (email == null) return rez.neovlasceniPristup();
        /*String emailIzAutorizacije = this.emailIzAutorizacije(autorizacija);
        if(!email.equals(emailIzAutorizacije))return rez.neovlasceniPristup();*/
        if (!npIzBaze.getProizvod().getKorisnik().getEmail().equals(email)) return rez.neovlasceniPristup();

        b.kupacPotvrdjujeNaruceniProizvod(npIzBaze);
        upravljanjeRacunom.prebaciteNaRacunKorisnika(npIzBaze.getProizvod().getKorisnik().getEmail(), npIzBaze.getCenaPutaKolicina());
        String poruka = String.format("Купац %s је потврдио пријем производа %s. На рачун Вам је легла сума у износу: %.2f %s.",
                npIzBaze.getNarudzbenica().getKupac().getImeNaziv(),
                npIzBaze.getProizvod().getNaziv(),
                npIzBaze.getCenaPutaKolicina(),
                this.dinarIliDinara(npIzBaze.getCenaPutaKolicina())
        );
        this.porudzbinaRukovanje.posaljite(npIzBaze.getProizvod().getKorisnik().getEmail(),
                poruka);
        this.adminRacunRukovalac.posaljite(b.vratiteStanjeRacuna());
        this.korisnikRacunRukovalac.posaljite(npIzBaze.getProizvod().getKorisnik().getEmail(), b.vratiteStanjeRacunaKorisnika(npIzBaze.getProizvod().getKorisnik().getEmail()));
        return rez.sveJeURedu();

    }

    public Integer konvertujteVrednostUInteger(Object vrednost) {
        String vrednostString = vrednost.toString();
        return Integer.parseInt(vrednostString);
    }

    @RequestMapping(value = "/korisnikUlazeZalbu", method = RequestMethod.POST)
    public RezultatRegistrovaniKorisnik korisnikUlazeZalbu(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody LinkedHashMap<String, Object> podaci
    ) {
        Map<String, Object> naruceniProizvodMapa = (Map<String, Object>) podaci.get("naruceniProizvod");
        if (naruceniProizvodMapa == null) return rez.neovlasceniPristup();
        if (naruceniProizvodMapa.get("id") == null) return rez.neovlasceniPristup();
        String idString = naruceniProizvodMapa.get("id").toString();
        Integer id = this.konverzijaIdja(idString);
        if (id == null) return rez.neovlasceniPristup();
        //provera cene kolicine  i ceneputakolicine

        NaruceniProizvod np = b.vratiteNaruceniProizvodPoId(id);
        if (!np.getNarudzbenica().getKupac().getEmail().equals(this.emailIzAutorizacije(autorizacija))) {
            return rez.neovlasceniPristup();
        }
        if (podaci.get("zalba") == null)
            return rez.neovlasceniPristup();
        String zalba = podaci.get("zalba").toString();

        boolean rezultat =b.korisnikSeZalio(np, zalba);
        if(rezultat){
            String poruka = String.format("Корисник %s се жалио на поруџбину Вашег производа %s.",np.getNarudzbenica().getKupac().getImeNaziv(), np.getProizvod().getNaziv());
            this.porudzbinaRukovanje.posaljite(np.getProizvod().getKorisnik().getEmail(),poruka);
        }
        return rez.postaviteSveJeURedu(rezultat);

    }

    @RequestMapping(value = "/korisnikOdgovaraNaZalbu", method = RequestMethod.POST)
    public RezultatRegistrovaniKorisnik odgovorNaZalbu(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody Map<String, Object> podaci
    ) {
        Map<String, Object> naruceniProizvodMapa = (Map<String, Object>) podaci.get("naruceniProizvod");
        if (naruceniProizvodMapa == null) return rez.neovlasceniPristup();
        if (naruceniProizvodMapa.get("id") == null) return rez.neovlasceniPristup();
        String idString = naruceniProizvodMapa.get("id").toString();
        Integer id = this.konverzijaIdja(idString);
        if (id == null) return rez.neovlasceniPristup();
        //provera cene kolicine  i ceneputakolicine

        NaruceniProizvod np = b.vratiteNaruceniProizvodPoId(id);

        if (!np.getProizvod().getKorisnik().getEmail().equals(this.emailIzAutorizacije(autorizacija))) {
            return rez.neovlasceniPristup();
        }
        Zalba z = b.vratiteZalbuZaNaruceniProizvod(np);
        Object zalbaOdgovorObj = podaci.get("odgovor");
        if (zalbaOdgovorObj == null) return rez.neovlasceniPristup();

        z.setTekstProdavca(zalbaOdgovorObj.toString());
        boolean rezultat = b.prodavacOdgovaraNaZalbu(z);
        if(rezultat){
            String poruka =String.format("Корисник %s је одговорио на Вашу жалбу на производ %s. ",
                    np.getProizvod().getKorisnik().getImeNaziv(), np.getProizvod().getNaziv());
        }
        return rez.postaviteSveJeURedu(rezultat);

    }
    @RequestMapping(value="/korisnikOtkazujePorudzbinu", method=RequestMethod.POST)
    public RezultatRegistrovaniKorisnik otkazujeSePorudzbina(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody Map<String, Object> podaci
    ){
        Map<String, Object> naruceniProizvodMapa = (Map<String, Object>) podaci.get("naruceniProizvod");
        if (naruceniProizvodMapa == null) return rez.neovlasceniPristup();
        if (naruceniProizvodMapa.get("id") == null) return rez.neovlasceniPristup();
        String idString = naruceniProizvodMapa.get("id").toString();
        Integer id = this.konverzijaIdja(idString);
        if (id == null) return rez.neovlasceniPristup();
        //provera cene kolicine  i ceneputakolicine

        NaruceniProizvod np = b.vratiteNaruceniProizvodPoId(id);
        boolean rezultat = b.otkazanNaruceniProizvod(np);
        if(rezultat){
            String poruka = String.format("Корисник %s је отказао поруџбину Вашег производа %s.",
                    np.getNarudzbenica().getKupac().getImeNaziv(), np.getProizvod().getNaziv());

        //tu ide posle vracanje para
        if(this.placanje.ponistitePlacanje(np.getNarudzbenica().getChargeId(), np.getCenaPutaKolicina())){
            this.upravljanjeRacunom.skiniteSaRacunaZaPrenos(np.getCenaPutaKolicina());
            //op op nisam poslao poruku
            this.porudzbinaRukovanje.posaljite(np.getProizvod().getKorisnik().getEmail(),poruka);
            this.adminRacunRukovalac.posaljite(b.vratiteStanjeRacuna());
            this.korisnikRacunRukovalac.posaljite(np.getProizvod().getKorisnik().getEmail(), b.vratiteStanjeRacunaKorisnika(np.getProizvod().getKorisnik().getEmail()));
        }
        }
        else{
            return rez.rezultatOtkazivanjeNarucenogProizvoda(false);
        }
        return rez.rezultatOtkazivanjeNarucenogProizvoda(rezultat);
    }

    @RequestMapping(value = "/registrovaniKorisnikDaLiSmeDaKomentariseIOcenjuje", method=RequestMethod.GET)
    public RezultatRegistrovaniKorisnik daLiSmeDaKomentariseIOcenjuje(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String proizvodIdS
    ){
       if(proizvodIdS==null)return rez.neovlasceniPristup();
        Integer proizvodId  = this.konverzijaIdja(proizvodIdS);
        if(proizvodId==null){
            return rez.neovlasceniPristup();
        }
        String email = this.emailIzAutorizacije(autorizacija);

        boolean rezultat = b.daLiKorisnikSmeDaKomentariseProizvod(email, proizvodId);
        /*if(!rezultat){
            return rez.rezultatRezultatRegistrovaniKorisnikDaLiSmeDaOcenjujeIKomentariseProizvod(rezultat, null, null);
        }
        Komentar k = b.vratiteKomentar(email, proizvodId);
        Ocena o = b.vratiteOcenu(email, proizvodId);
        return rez.rezultatRezultatRegistrovaniKorisnikDaLiSmeDaOcenjujeIKomentariseProizvod(rezultat, k, o);
    */
        if(!rezultat){
            return rez.rezultatNoviDaLiSmeDaOcenjujeIKomentarise(rezultat, null);
        }
        KomentarIOcena ko = b.vratiteKomentarIOcenuZaKorisnikaIProizvodNovi(email, proizvodId);
        return rez.rezultatNoviDaLiSmeDaOcenjujeIKomentarise(rezultat, ko);
    }


    @RequestMapping(value="/registrovaniKorisnikKomentariseProizvod", method=RequestMethod.POST)
    public RezultatRegistrovaniKorisnik komentarisanjeProizvoda(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody Map<String, Object> podaci
    ){

        if(podaci==null)return rez.neovlasceniPristup();
        Object tekstObj = podaci.get("tekst");
        if(tekstObj==null)return rez.neovlasceniPristup();
        String tekst = tekstObj.toString();
        if(tekst.trim().equals("")){
            return rez.neovlasceniPristup();
        }
        Object proizvodIdObj = podaci.get("proizvodId");
        if(proizvodIdObj==null)return rez.neovlasceniPristup();
        String proizvodIdStr = proizvodIdObj.toString();
        Integer proizvodId = this.konverzijaIdja(proizvodIdStr);
        if(proizvodId==null){
            return rez.neovlasceniPristup();
        }
        String email = this.emailIzAutorizacije(autorizacija);
        if(!b.daLiKorisnikSmeDaKomentariseProizvod(email , proizvodId)){
            return rez.neovlasceniPristup();
        }
        return rez.postaviteSveJeURedu(b.snimiteKomentarNovaMetoda(tekst, email, proizvodId ));
    }

    @RequestMapping(value="/registrovaniKorisnikOcenjujeProizvod", method=RequestMethod.POST)
    public RezultatRegistrovaniKorisnik ocenjivanjeProizvoda(
            @RequestHeader("Authorization") String autorizacija,
            @RequestBody Map<String, Object> podaci
    ){

        if(podaci==null)return rez.neovlasceniPristup();
        Object ocenaObj = podaci.get("ocena");
        if(ocenaObj==null)return rez.neovlasceniPristup();
        String ocenaStr = ocenaObj.toString();
        Integer ocena = this.konverzijaIdja(ocenaStr);
        if(ocena==null){
            return rez.neovlasceniPristup();
        }
        if(!(ocena>4 &&ocena<11)){
            return rez.neovlasceniPristup();
        }
        Object proizvodIdObj = podaci.get("proizvodId");
        if(proizvodIdObj==null)return rez.neovlasceniPristup();
        String proizvodIdStr = proizvodIdObj.toString();
        Integer proizvodId = this.konverzijaIdja(proizvodIdStr);
        if(proizvodId==null){
            return rez.neovlasceniPristup();
        }
        String email = this.emailIzAutorizacije(autorizacija);
        if(!b.daLiKorisnikSmeDaKomentariseProizvod(email , proizvodId)){
            return rez.neovlasceniPristup();
        }//ovo ostaje tako
        return rez.postaviteSveJeURedu(b.snimiteOcenuNovaMetoda(ocena, email, proizvodId ));
    }

    @RequestMapping(value="/registrovaniKorisnikVratiteSagovornike", method=RequestMethod.GET)
    public RezultatRegistrovaniKorisnik vratiteSagovornike(
            @RequestHeader("Authorization") String autorizacija
    ){
                String email = this.emailIzAutorizacije(autorizacija);
                List<Korisnik> korisnici = b.vratiteSagovornikeZaKorisnika(email);
                HashMap<String, List<Poruka>> poruke = new LinkedHashMap<>();
                korisnici.forEach(k->{
                    List<Poruka> porukeZaKorisnika = b.vratitePoruke(email, k.getEmail(),30, 0);
                    poruke.put(k.getEmail(), porukeZaKorisnika);
                });
                return rez.rezultatKorisniciSaKojimaSmeDaPrica(korisnici, poruke);
    }
    @RequestMapping(value="/registrovaniKorisnikVratiteRazgovor", method=RequestMethod.GET)
    public RezultatRegistrovaniKorisnik vratitePoruke(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String email,
            @RequestParam String limit,
            @RequestParam String pomeraj
    ){
         if(email==null | limit==null||pomeraj==null)return rez.neovlasceniPristup();
         Integer limitI = this.konverzijaIdja(limit);
         if(limitI==null) return rez.neovlasceniPristup();
         Integer pomerajI = this.konverzijaIdja(pomeraj);
         if(pomerajI==null) return  rez.neovlasceniPristup();
         if(limitI<0 || pomerajI<0) return rez.neovlasceniPristup();

         String email2 = this.emailIzAutorizacije(autorizacija);
         return rez.rezultatPoruke(b.vratitePoruke(email2, email, limitI, pomerajI));
    }

    @RequestMapping(value="/registrovaniKorisnikVratiteStanjeRacuna", method=RequestMethod.GET)
    public RezultatRegistrovaniKorisnik vratiteStanjeNaPocetku(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String email
    ) {
                if(email ==null)return rez.neovlasceniPristup();
                return rez.pocetnoStanjeRacunaKorisnika(b.vratiteStanjeRacunaKorisnika(email));
    }

}
