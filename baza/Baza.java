package srb.akikrasic.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import srb.akikrasic.domen.*;
import srb.akikrasic.racuni.UpravljanjeRacunom;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.loginkorisnika.RezultatLogovanja;
import srb.akikrasic.slike.Slike;
import srb.akikrasic.stripe.RadSaPlacanjem;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.ProizvodSmeDaSeMenja;
import srb.akikrasic.token.Sifra;
import srb.akikrasic.token.Tokeni;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by aki on 7/28/17.
 */
@Component
public class Baza {
    private JdbcTemplate b;
    @Autowired
    private Sifra radSaSifrom;
    @Autowired
    private Tokeni radSaTokenima;
    @Autowired
    private GenerisanjeTokena radSaGenerisanjemTokena;

    @Autowired
    private Slike slike;

    @Autowired
    private ProizvodSmeDaSeMenja proizvodSmeDaSeMenja;

    @Autowired
    private UpravljanjeRacunom upravljanjeRacunom;

    @Autowired
    private RadSaPlacanjem placanje;


    @PostConstruct
    public void inic(){
        //pravi se sa bazom zato sto koriste bazu za inicijalizaciju
        //posto nema Lazy anotaciju pravi se na pocetku, u sustini je svejedno
        proizvodSmeDaSeMenja.inicijalizacija(this.proizvodiKojiNeSmejuDaSeMenjaju());


    }


    public Baza() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/prodavnica");
        ds.setUsername("postgres");
        ds.setPassword("novasifra");
        b = new JdbcTemplate(ds);
    }

    public void snimiteNovogNepotvrdjenogKorisnika(String imeNaziv, String email, byte[] sifra, String adresa, String mesto, String tel1, String tel2, String tel3, String token) {
        b.update("INSERT INTO nepotvrdjenikorisnik(imeNaziv, email, sifra, adresa, mesto,tel1,tel2,tel3, token) VALUES(?,?,?,?,?,?,?,?,?)", imeNaziv, email, sifra, adresa, mesto, tel1, tel2, tel3, token);

    }

    public void snimiteNovogKorisnika(String imeNaziv, String email, byte[] sifra, String adresa, String mesto, String tel1, String tel2, String tel3) {
        b.update("INSERT INTO korisnik(imeNaziv, email, sifra, adresa, mesto,tel1,tel2,tel3) VALUES(?,?,?,?,?,?,?,?)", imeNaziv, email, sifra, adresa, mesto, tel1, tel2, tel3);

    }

    public boolean daLiJeEmailSlobodanUObeTabele(String email) {
        List<Map<String, Object>> rezultat = b.queryForList("SELECT email FROM nepotvrdjenikorisnik WHERE email=?", email);
        if (!rezultat.isEmpty()) return false;
        rezultat = b.queryForList("SELECT email FROM korisnik WHERE email=?", email);
        return rezultat.isEmpty();
    }

    public Korisnik daLiJeMogucaPotvrdaKorisnika(String email, String token) {
        List<Map<String, Object>> rezultat = b.queryForList("SELECT id, email, imenaziv, sifra, adresa, mesto, tel1, tel2, tel3, token FROM nepotvrdjenikorisnik WHERE email=?", email);
        //System.out.println(rezultat);
        if (rezultat.isEmpty()) return null;
        String tokenIzBaze = (String) rezultat.get(0).get("token");

        if (!token.equals(tokenIzBaze)) return null;
        NeregistrovaniKorisnik nk = NeregistrovaniKorisnik.napraviteNeregistrovanogKorisnikaIzMape(rezultat.get(0));
        return Korisnik.korisnikIzneregistrovanogKorisnika(nk);
    }

    public boolean daLiJeMogucaPotvrdaKorisnika1(String email, String token) {
        List<Map<String, Object>> rezultat = b.queryForList("SELECT id, email, imenaziv, sifra, adresa, mesto, tel1, tel2, tel3, token FROM nepotvrdjenikorisnik WHERE email=?", email);
        //System.out.println(rezultat);
        if (rezultat.isEmpty()) return false;
        String tokenIzBaze = (String) rezultat.get(0).get("token");

        if (!token.equals(tokenIzBaze)) return false;
        NeregistrovaniKorisnik nk = NeregistrovaniKorisnik.napraviteNeregistrovanogKorisnikaIzMape(rezultat.get(0));
        Korisnik k = Korisnik.korisnikIzneregistrovanogKorisnika(nk);
        this.potvrditeKorisnika(k);
        return true;
    }

    public boolean potvrditeKorisnika(Korisnik k) {
        if (b.update("DELETE FROM nepotvrdjenikorisnik WHERE email=?", k.getEmail()) != 1) return false;
        return b.update("INSERT INTO korisnik(imeNaziv, email, sifra, adresa, mesto,tel1,tel2,tel3, token, racun) VALUES(?,?,?,?,?,?,?,?,?,?)", k.getImeNaziv(), k.getEmail(), k.getSifra(), k.getAdresa(), k.getMesto(), k.getTel1(), k.getTel2(), k.getTel3(), null, 0) == 1;

    }

    private String neuspesnoLogovanjePoruka = "Унели сте погрешну адресу електронске поште или шифру.";

    public RezultatLogovanja logovanjeKorisnika(String email, byte[] sifra) {
        List<Map<String, Object>> rezultat = b.queryForList("SELECT imeNaziv, email, adresa, mesto, tel1, sifra FROM korisnik WHERE email=?", email);
        RezultatLogovanja l = new RezultatLogovanja();
        if (rezultat.isEmpty()) {
            l.setUspesno(false);
            List<Map<String, Object>> rezultatDrugePretrage = b.queryForList("SELECT email, sifra from nepotvrdjenikorisnik WHERE email=?", email);
            if (rezultatDrugePretrage.isEmpty()) {
                l.setRazlogNeuspesnogLogovanja(neuspesnoLogovanjePoruka);
            } else {
                byte[] sifraIzBaze = (byte[]) rezultatDrugePretrage.get(0).get("sifra");
               /* System.out.println(sifra);
                System.out.println(sifraIzBaze);*/
                if (radSaSifrom.proveriteSifru(sifra, sifraIzBaze)) {
                    l.setRazlogNeuspesnogLogovanja("Адреса електронске поште коју сте унели није потврђена, молимо Вас да је потврдите. ");
                } else {
                    l.setRazlogNeuspesnogLogovanja(neuspesnoLogovanjePoruka);
                }
            }
        } else {
            byte[] sifraIzBaze = (byte[]) rezultat.get(0).get("sifra");
            if (radSaSifrom.proveriteSifru(sifra, sifraIzBaze)) {
                l.setUspesno(true);
                l.setEmail(email);
                Map<String, Object> redIzMape = rezultat.get(0);

                l.setToken(radSaTokenima.korisnikUspesnoUlogovan((String) redIzMape.get("imeNaziv"), (String) redIzMape.get("email"), (String) redIzMape.get("adresa"), (String) redIzMape.get("mesto"), (String) redIzMape.get("tel1")));
                //l.setToken(radSaTokenima.generisiteTokenZaNeregistrovanogKorisnika());
            } else {
                l.setUspesno(false);
                l.setRazlogNeuspesnogLogovanja(neuspesnoLogovanjePoruka);
            }

        }
        return l;
    }

    public String posaljiteZahtevZaPromenuSifre(String email) {
        List<Map<String, Object>> rez = b.queryForList("SELECT email, imeNaziv, sifra FROM korisnik WHERE email=?", email);

        if (rez.isEmpty()) return null;
        Map<String, Object> m = rez.get(0);

        String imeNaziv = (String) m.get("imeNaziv");
        byte[] sifra = (byte[]) m.get("sifra");
        long vreme = System.nanoTime();
        String token = radSaGenerisanjemTokena.generisiteTokenZaPromenuSifre(imeNaziv, email, sifra, vreme);
        rez = b.queryForList("SELECT email FROM promenasifre WHERE email=?", email);
        if (rez.isEmpty()) {
            b.update("INSERT INTO promenasifre(email, vreme, token) VALUES(?,?,?)", email, vreme, token);
        } else {
            b.update("UPDATE promenasifre SET token=? AND vreme=? WHERE email=? ", token, vreme, email);
        }
        return token;
    }

    public boolean promenaSifre(String email, String token, byte[] novaSifra) {

        //1 provera da li se nalazi u tabelu za promenu sifre
        //2 ako se nalazi да ли се токени поклапају
        //3 ako se poklapaju brisi token menjaj sifru
        List<Map<String, Object>> rezultat = b.queryForList("SELECT  token FROM promenasifre WHERE email=?", email);
        if (rezultat.isEmpty()) return false;
        Map m = rezultat.get(0);
        String tokenIZBaze = (String) m.get("token");

        if (!token.equals(tokenIZBaze)) return false;
        //sada moze sifra da se menja
        b.update("DELETE FROM promenasifre WHERE email=?", email);
        byte[] hesiranaSifra = radSaSifrom.hesirajteSifru(novaSifra);
        b.update("UPDATE korisnik SET sifra =? WHERE email=?", hesiranaSifra, email);

        return true;
    }


    public Korisnik pretragaKorisnikaPoEmailu(String email) {
        List<Map<String, Object>> izBaze = b.queryForList("SELECT * FROM korisnik WHERE email=?", email);
        if (izBaze.isEmpty()) {
            return null;
        } else {

            return Korisnik.korisnikIzBaze(izBaze.get(0));
        }


    }

    public boolean dodajteKategoriju(String naziv) {
        List<Map<String, Object>> rez = b.queryForList("SELECT id, naziv FROM kategorija WHERE naziv=?", naziv);
        if (!rez.isEmpty()) return false;
        b.update("INSERT INTO kategorija(naziv) VALUES(?)", naziv);
        return true;

    }

    public List<Kategorija> vratiteSveKategorije() {
        ArrayList<Kategorija> kategorije = new ArrayList<>();
        List<Map<String, Object>> rez = b.queryForList("SELECT id, naziv FROM kategorija ORDER BY naziv");
        rez.forEach(m -> {
            kategorije.add(Kategorija.kategorijaIzBaze(m));
        });
        return kategorije;
    }

    public boolean izmenaKategorije(int id, String noviNaziv) {
        List<Map<String, Object>> rez = b.queryForList("SELECT id FROM kategorija WHERE naziv=?", noviNaziv);
        if (rez.isEmpty()) {
            b.update("UPDATE kategorija SET naziv =? WHERE id=?", noviNaziv, id);
            return true;// u sustini trebalo bi da se povratna vrednost update-a ispita i ako je 1 onda true ako nije false, ali trebal i i ovako sve da radi
        } else return false;
    }

    public boolean obrisiteKategoriju(int id) {
        return (b.update("DELETE FROM kategorija WHERE id=?", id)) == 1;
    }

    public Kategorija vratiteKategorijuPoId(int id) {
        Kategorija k = null;
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM kategorija WHERE id=?", id);
        if (!rez.isEmpty()) {
            k = Kategorija.izBaze(rez.get(0));

        }

        return k;
    }

    public boolean izmenitePodatkeZaKorisnika(String imeNaziv, String email, byte[] sifra, String adresa, String mesto, String tel1, String tel2, String tel3) {
        List<Map<String, Object>> r = b.queryForList("SELECT email, imeNaziv, sifra, adresa,mesto, tel1, tel2, tel3, racun FROM korisnik WHERE email=? ", email);
        if (r.isEmpty()) return false;
        Korisnik k = Korisnik.korisnikIzBaze(r.get(0));
        StringBuilder deoZaPromenu = new StringBuilder("");
        ArrayList<Object> dodajuSe = new ArrayList<>();
        if (!k.getImeNaziv().equals(imeNaziv)) {
            deoZaPromenu.append(" imeNaziv=?,");
            dodajuSe.add(imeNaziv);

        }
        k.setSifra((byte[]) r.get(0).get("sifra"));
        if (!(new String(sifra)).equals("")) {//sve je  obezbedjeno da ne bude "" osim sifre
            if (!radSaSifrom.proveriteSifru(sifra, k.getSifra())) {
                //ako sifre nisu iste
                deoZaPromenu.append(" sifra=?,");
                dodajuSe.add(radSaSifrom.hesirajteSifru(sifra));
            }
        }
        //System.out.println(k.getAdresa()+" "+adresa);
        if (!k.getAdresa().equals(adresa)) {
            deoZaPromenu.append(" adresa=?,");
            dodajuSe.add(adresa);
        }
        if (!k.getMesto().equals(mesto)) {
            deoZaPromenu.append(" mesto=?,");
            dodajuSe.add(mesto);
        }
        if (!k.getTel1().equals(tel1)) {
            deoZaPromenu.append(" tel1=?,");
            dodajuSe.add(tel1);
        }
        if (!k.getTel2().equals(tel2)) {
            deoZaPromenu.append(" tel2=?,");
            dodajuSe.add(tel2);
        }
        if (!k.getTel3().equals(tel3)) {
            deoZaPromenu.append(" tel3=?,");
            dodajuSe.add(tel3);
        }
        if (deoZaPromenu.toString().equals("")) return false;
        dodajuSe.add(email);
        deoZaPromenu.deleteCharAt(deoZaPromenu.length() - 1);

        // System.out.println(deoZaPromenu);
        String query = new StringBuilder("UPDATE korisnik SET").append(deoZaPromenu).append(" WHERE email=?").toString();
        Object[] niz = dodajuSe.toArray();
        //  System.out.println(query);
        int brojRedova = b.update(query, niz);
        return brojRedova == 1;
    }


    public Proizvod napraviteProizvodSaPodacimaIzBaze(Map<String, Object> m) {
        Proizvod p = Proizvod.izBaze(m);
        p.setKategorija(this.vratiteKategorijuPoId((Integer) m.get("kategorija_id")));
        p.setKorisnik(this.pretragaKorisnikaPoEmailu((String) m.get("korisnik_email")));
        p.setSlike(slike.vratiteSveSlikeProizvoda(p.getId()));
        return p;
    }

    public Proizvod vratiteProizvodPoId(Integer id) {
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM proizvod WHERE id=?", id);
        if (rez.isEmpty()) return null;
        return this.napraviteProizvodSaPodacimaIzBaze(rez.get(0));
    }


    public int dodajteNoviProizvod(String email, String naziv, String opis, double cena, int kategorijaId) {
        KeyHolder drzacKljuca = new GeneratedKeyHolder();
        Korisnik korisnikEmail = this.pretragaKorisnikaPoEmailu(email);

        b.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("INSERT INTO proizvod(naziv, opis, trenutnacena,kategorija_id,korisnik_email, aktivan, prosecna_ocena  ) VALUES(?,?,?,?,?, ?,0)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, naziv);
                ps.setString(2, opis);
                ps.setDouble(3, cena);
                ps.setInt(4, kategorijaId);
                ps.setString(5, email);
                ps.setBoolean(6, true);
                return ps;
            }
        }, drzacKljuca);
        return (Integer) drzacKljuca.getKeys().get("id");

        //  b.update("INSERT INTO proizvod(naziv, opis, trenutnacena, prodavac_id, kategorija_id) VALUES (?,?,?,?,?)", p.getNaziv(), p.getOpis(), p.getTrenutnaCena(),p.getProdavac().getId(), p.getKategorija().getId())==1;


    }


    public void unesiteKljucneReci(List<String> kljucneReci, int proizvodId) {

        kljucneReci.forEach(k -> {
            String kljRec = k.toLowerCase();
            int kljucnaRecId = -1;//pocetnaVrednost
            List<Map<String, Object>> listaKljReciSaUnetimImenom = b.queryForList("SELECT id FROM kljucnarec WHERE naziv = ?", kljRec);
            if (listaKljReciSaUnetimImenom.isEmpty()) {

                KeyHolder drzac = new GeneratedKeyHolder();
                b.update(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement ps = con.prepareStatement("INSERT INTO kljucnarec(naziv) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, k);
                        return ps;
                    }
                }, drzac);
                kljucnaRecId = (Integer) drzac.getKeys().get("id");
            } else {
                kljucnaRecId = (Integer) listaKljReciSaUnetimImenom.get(0).get("id");
            }
            b.update("INSERT INTO kljucnarec_proizvod (kljucnarec_id, proizvod_id) VALUES(?,?)", kljucnaRecId, proizvodId);

        });
    }

    public void izmeniteKljucneReci(List<String> kljucneReciKojeSuDosleIzZahteva, int proizvodId) {
        List<Map<String, Object>> rez = b.queryForList("SELECT kljucnarec.id as id, naziv, proizvod_id FROM kljucnarec, kljucnarec_proizvod WHERE kljucnarec.id=kljucnarec_proizvod.kljucnarec_id AND proizvod_id=? ", proizvodId);
        LinkedHashMap<String, KljucnaRec> kljucneReciIzBaze = new LinkedHashMap<>();
        rez.forEach(m -> {
            KljucnaRec k = KljucnaRec.izBaze(m);
            kljucneReciIzBaze.put(k.getNaziv(), k);
        });
        //ono sto ostane u prvoj listi, to je novo i snima se, ono sto ostane u drugoj listi to treba da se brise
        List<String> kljucneReciKojeSuDosleIzZahtevaZaIteraciju = new ArrayList<>();
        kljucneReciKojeSuDosleIzZahtevaZaIteraciju.addAll(kljucneReciKojeSuDosleIzZahteva);
        kljucneReciKojeSuDosleIzZahtevaZaIteraciju.forEach(nazivKljucneReci -> {
            if (kljucneReciIzBaze.get(nazivKljucneReci) != null) {
                //nalazi se u bazi i treba da se izbaci iz obe liste
                kljucneReciIzBaze.remove(nazivKljucneReci);
                kljucneReciKojeSuDosleIzZahteva.remove(nazivKljucneReci);

            }
        });
        this.unesiteKljucneReci(kljucneReciKojeSuDosleIzZahteva, proizvodId);
        this.obrisiteKljucneReci(kljucneReciIzBaze.values(), proizvodId);

    }

    public List<KljucnaRec> vratiteSveKljucnereciZaProizvod(Proizvod p) {
        List<Map<String, Object>> rez = b.queryForList("SELECT kljucnarec_id as id, naziv FROM kljucnarec, kljucnarec_proizvod WHERE kljucnarec_id=kljucnarec.id AND proizvod_id=? ", p.getId());
        List<KljucnaRec> lista = new ArrayList<>();
        rez.forEach(m -> {
            lista.add(KljucnaRec.izBaze(m));
        });
        return lista;
    }
    public List<String> vratiteSveKljucnereciZaProizvodStringovi(Proizvod p) {
        List<Map<String, Object>> rez = b.queryForList("SELECT naziv FROM kljucnarec, kljucnarec_proizvod WHERE kljucnarec_id=kljucnarec.id AND proizvod_id=? ", p.getId());
        List<String> lista = new ArrayList<>();
        rez.forEach(m -> {
            lista.add((String)m.get("naziv"));
        });
        return lista;
    }

    public void obrisiteKljucneReci(Collection<KljucnaRec> listaZaBrisanje, int proizvodId) {
        listaZaBrisanje.forEach(k -> {//ako se ne upotree oba argumenta izbrisace se veza izmedju nekog drugog proizvoda i date kljucne reci
            //ukoliko se za neku kljucnu rec obrisu sve veze sa proizvodima, ideja je da ostane
            //pa ce da joj se doda neki novi proizvod ako korisnik unese posto je velika verovatnoca
            //da ce zbog slicnosti proizvoda  uneti
            b.update("DELETE FROM kljucnarec_proizvod WHERE kljucnarec_id=? AND proizvod_id=?", k.getId(), proizvodId);
        });

    }


    public boolean izmeniteProizvodPodaci(Proizvod p) {
        return b.update("UPDATE proizvod SET naziv=?, opis=?, trenutnacena=?, kategorija_id=?, aktivan=? WHERE id=?", p.getNaziv(), p.getOpis(), p.getTrenutnaCena(), p.getKategorija().getId(),p.isAktivan(), p.getId()) == 1;
    }

    public boolean izmeniteProizvodCenu(Proizvod p) {
        return b.update("UPDATE proizvod SET trenutnacena=? WHERE id=?", p.getTrenutnaCena(), p.getId()) == 1;
    }
    public boolean izmeniteProizvodAktivan(Proizvod p){
        return b.update("UPDATE proizvod SET aktivan=? WHERE id=?", p.isAktivan(), p.getId())==1;
    }

    public boolean obrisiteProizvod(Proizvod p) {
        if (b.update("DELETE FROM kljucnarec_proizvod WHERE proizvod_id=?", p.getId()) == 0) return false;
        return b.update("DELETE FROM proizvod WHERE id=? ", p.getId()) > 0;
    }



    public List<Proizvod> vratiteSveProizvodeNekogKorisnikaPoEmailu(String email) {
        List<Proizvod> l = new ArrayList<>();
        Korisnik k = this.pretragaKorisnikaPoEmailu(email);
        if (k == null) return l;
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM proizvod WHERE korisnik_email=? and aktivan=true ORDER BY id ", k.getEmail());
        if (rez.isEmpty()) return l;
        rez.forEach(m -> {
            Proizvod p = this.napraviteProizvodZaPrikazKorisnikuSaKomentarimaIOcenama(m);
            l.add(p);
        });
        return l;
    }
    public List<Proizvod> vratiteSveProizvodeNekogKorisnikaPoEmailuDaIhOnMenja(String email) {
        List<Proizvod> l = new ArrayList<>();
        Korisnik k = this.pretragaKorisnikaPoEmailu(email);
        if (k == null) return l;
        //ovde se svi vracaju i aktivni i neaktivni
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM proizvod WHERE korisnik_email=? ORDER BY id ", k.getEmail());
        if (rez.isEmpty()) return l;
        rez.forEach(m -> {
            Proizvod p = this.napraviteProizvodSaPodacimaIzBaze(m);
            l.add(p);
        });
        return l;
    }

    public String like(String s){
        return new StringBuilder("%").append(s).append("%").toString();
    }

    public List<Proizvod> vratiteSveProizvode(){
        List<Proizvod> lista = new ArrayList<Proizvod>();
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM proizvod ORDER BY naziv");

        rez.forEach(m->{
            Proizvod p = this.napraviteProizvodSaPodacimaIzBaze(m);
            lista.add(p);
        });

        return lista;
    }

    public List<Proizvod> vratiteSveProizvodeKorisnika(String email){
        List<Proizvod> lista = new ArrayList<Proizvod>();
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM proizvod WHERE korisnik_email=? ORDER BY naziv", email);

        rez.forEach(m->{
            Proizvod p = this.napraviteProizvodSaPodacimaIzBaze(m);
            lista.add(p);
        });

        return lista;
    }

    public List<Proizvod> pretragaProizvoda(String imeNazivKorisnika,
                                                       String email,
                                                       String nazivProizvoda,
                                                       double cenaOd,//samo ne sme da bude null sto se pre toga proverava
                                                       double cenaDo,//isto vazi
                                                       List<String> kljucneReci,
                                                       String kategorijaNaziv,
                                                       String opis
                                                      ) {
        //prepravka 3.12.2017.
        List<Proizvod> lista = new ArrayList<>();
        //obrisano 3.12.2017. r.setLista(lista) i obrisano pravljenje R
        StringBuilder query = new StringBuilder(
                "SELECT DISTINCT  proizvod.id AS id, proizvod.naziv as naziv, opis,trenutnacena, kategorija_id, korisnik_email, aktivan, prosecna_ocena ").append(
                "FROM proizvod, korisnik, kljucnarec, kljucnarec_proizvod, kategorija ").append(
                "WHERE proizvod.id=kljucnarec_proizvod.proizvod_id AND ").append(
                "proizvod.korisnik_email = korisnik.email AND ").append(
                "proizvod.kategorija_id=kategorija.id AND ").append(
                "kljucnarec.id=kljucnarec_proizvod.kljucnarec_id AND ");
        StringBuilder promenljiviDeo = new StringBuilder();
        List<Object> parametri = new ArrayList<>();

        if (!imeNazivKorisnika.equals("")) {

            promenljiviDeo.append(" lower(korisnik.imeNaziv) LIKE ? AND ");
            parametri.add(like(imeNazivKorisnika.toLowerCase()));
        }
        if (!email.equals("")) {
            promenljiviDeo.append(" lower(korisnik.email) LIKE ? AND ");
            parametri.add(like(email.toLowerCase()));//mada tu i ne treba lower to je email
        }
        if (!nazivProizvoda.equals("")) {
            promenljiviDeo.append(" lower(proizvod.naziv) LIKE ? AND ");
            parametri.add(like(nazivProizvoda.toLowerCase()));
        }

        if (!opis.equals("")) {
            promenljiviDeo.append(" lower(opis) LIKE ? AND ");
            parametri.add(like(opis.toLowerCase()));

        }

        if (!kategorijaNaziv.equals("")) {
            promenljiviDeo.append(" lower(kategorija.naziv) LIKE ? AND ");
            parametri.add(like(kategorijaNaziv.toLowerCase()));

        }
        if (cenaOd > -1) {
            promenljiviDeo.append(" proizvod.trenutnacena>=? AND ");
            parametri.add(cenaOd);

        }

        if (cenaDo > -1) {
            promenljiviDeo.append(" proizvod.trenutnacena<=? AND ");
            parametri.add(cenaDo);

        }



        if(kljucneReci.size()>0){
            promenljiviDeo.append(" ( ");
           kljucneReci.forEach(k->{
            promenljiviDeo.append(" lower(kljucnarec.naziv) LIKE ? OR ");
            parametri.add(like(k.toLowerCase()));
           });
            promenljiviDeo. append(" '1'='1' ) AND ");
        }

        if(promenljiviDeo.toString().equals(""))return lista;//isto 3.12.2017..




        query.append(promenljiviDeo).append(" '1'='1' ORDER BY proizvod.naziv  ");//izbaceno GROUP BY proizvod.id

        //resen problem izbacen limit, sam cu da si limitiram tamo kad imam sve podatke i dovidjenja
        //kad korisnik klikne na pretrazite, onda se nadju novi podaci i onda si njih opet sam limitiram, a ne da se utepujem bez veze
        List<Map<String, Object>> rez = b.queryForList(query.toString(), parametri.toArray());

        rez.forEach(m->{
            Proizvod p = this.napraviteProizvodSaPodacimaIzBaze(m);
            lista.add(p);
        });
        //r.setUkBrojProizvoda(lista.size()); isto zakomentarisano 3.12.2017.
        return lista;
    }


    public boolean obrisiteProizvod(int id) {
        return b.update("DELETE FROM proizvod WHERE id=?", id) == 1;
    }

    public boolean daLiSeNazivKategorijeNalaziUBazi(String naziv) {
        List<Map<String, Object>> rez = b.queryForList("SELECT id FROM kategorija WHERE naziv=?", naziv);
        return !rez.isEmpty();

    }

    @PreDestroy
    public void kraj() {// nema tu nista da se upisuje samo u slika predestroy
    }

    public long ucitajteVrednostSlikeNaPocetku() {
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM slika");
        return (Long) rez.get(0).get("vrednost");
    }

    public void sacuvajteVrednostSlikeNaKraju(long vrednost) {
        b.update("UPDATE slika SET vrednost=?", vrednost);
    }

    public long ucitajteVrednostBrojaPrivremenogProizvodaNaPocetku() {
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM privremeniproizvodbroj");
        return (Long) rez.get(0).get("vrednost");
    }

    public void sacuvajteVrednostBrojaPrivremenogPRoizvodaNaKraju(long vr) {
        b.update("UPDATE privremeniproizvodbroj SET vrednost=?", vr);
    }

    public List<Integer> proizvodiKojiNeSmejuDaSeMenjaju(){
        List<Integer> rezultat = new ArrayList<>();
        List<Map<String, Object>> l = b.queryForList("SELECT DISTINCT proizvod_id FROM naruceniproizvod ");
        l.forEach(m->{
            rezultat.add((Integer)m.get("proizvod_id"));
        });
        //System.out.println(rezultat);
        return rezultat;
    }
    public List<Komentar> vratiteKomentareZaProizvod(int proizvodId){
        return null;
    }

    public Proizvod napraviteProizvodZaPrikazKorisnikuSaKomentarimaIOcenama(Map<String, Object> mapa){
        Proizvod p = this.napraviteProizvodSaPodacimaIzBaze(mapa);
        //tu idu komentari
        p.setKomentari(this.vratiteSveKomentareZaProizvod(p.getId()));
        p.setOcene(this.vratiteSveOceneZaProizvod(p.getId()));
        return p;
    }

    public boolean snimiteKomentar(String tekst,LocalDateTime datum, String email, int id) {
       /* b.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
               PreparedStatement ps = con.prepareStatement("INSERT INTO komentar(tekst, datum, korisnik_email, proizvod_id) VALUES(?,?,?,?) ");
               ps.setString(1, tekst);
               ps.setObject(2, datum);
               ps.setString(3, email);
               ps.setInt(4, id);
                return ps;
            }
        });*/

       return b.update("INSERT INTO komentar(tekst, datum, korisnik_email, proizvod_id) VALUES(?,?,?,?) ",tekst, Timestamp.valueOf(datum), email, id)==1;

    }

    public Komentar vratiteKomentar(String korisnikEmail, int proizvodId) {
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM komentar WHERE korisnik_email=? AND proizvod_id=?", korisnikEmail, proizvodId);
        if(rez.isEmpty())return null;
        Komentar k = Komentar.izBaze(rez.get(0));
        k.setKorisnik(this.pretragaKorisnikaPoEmailu((String)rez.get(0).get("korisnik_email")));
        k.setProizvod(this.vratiteProizvodPoId((Integer)rez.get(0).get("proizvod_id")));
        return k;
    }
    public boolean daLiKorisnikSmeDaKomentariseProizvod(String email, int proizvodId){
        List<Map<String, Object>> l = b.queryForList( "SELECT DISTINCT  proizvod.id FROM proizvod, naruceniproizvod, narudzbenica WHERE narudzbenica.id=naruceniproizvod.narudzbenica_id AND proizvod.id=naruceniproizvod.proizvod_id AND narudzbenica.korisnik_email=? AND proizvod.id=?", email, proizvodId);
        return l.size()==1;
    }
    public boolean obrisiteKomentar(int komentarId) {
        return b.update("DELETE FROM komentar where komentar_id=?", komentarId)==1;
    }

    public boolean izmeniteKomentar(String tekst,LocalDateTime datum, String email, int proizvodId){

        return b.update("UPDATE komentar SET tekst=?, datum=? WHERE korisnik_email=? AND proizvod_id=? ", tekst, Timestamp.valueOf(datum),email, proizvodId)==1;
    }



    public void izracunajteProsecnuOcenu(int proizvodId){
        List<Map<String, Object>> rez = b.queryForList("SELECT avg(ocena) as prosek FROM ocena WHERE proizvod_id=?", proizvodId);
        if(rez.isEmpty())return;
        BigDecimal prosek = (BigDecimal)rez.get(0).get("prosek");
        b.update("UPDATE proizvod SET prosecna_ocena=? WHERE id=?", prosek, proizvodId);
    }
    public boolean daLiKorisnikSmeDaOceniProizvod(String korisnikEmail, int proizvodId){  //nije dobro!!!
        List<Map<String, Object>> rez = b.queryForList("SELECT narudzbenica.id FROM narudzbenica inner join naruceniproizvod on narudzbenica.id=naruceniproizvod.narudzbenica_id WHERE narudzbenica.korisnik_email=?", korisnikEmail);
        return  !rez.isEmpty();
    }

    public boolean snimiteOcenu(int ocena, String korisnikEmail, int proizvodId) {
        return b.update("INSERT INTO ocena(ocena, korisnik_email, proizvod_id) VALUES(?,?,?)", ocena, korisnikEmail, proizvodId)==1;

    }
    public void snimiteOcenuSaRacunanjemProseka(int ocena, String korisnikEmail, int proizvodId){
        snimiteOcenu(ocena, korisnikEmail, proizvodId);
        izracunajteProsecnuOcenu(proizvodId);
    }
    public boolean izmeniteOcenu(int ocena, String korisnikEmail, int proizvodId){
         return b.update("UPDATE ocena SET ocena=? WHERE korisnik_email=? AND proizvod_id=?",ocena, korisnikEmail, proizvodId )==1;
    }
    public boolean izmeniteOcenuSaRacunanjemProseka(int ocena, String korisnikEmail, int proizvodId){
       boolean rez = this.izmeniteOcenu(ocena, korisnikEmail, proizvodId);
       this.izracunajteProsecnuOcenu(proizvodId);
       return rez;
    }
    public boolean obrisiteOcenu(String korisnikEmail, int proizvodId){
        //eee a prosek Bajo moj
        if( b.update("DELETE FROM ocena WHERE korisnik_email=? AND proizvod_id=?", korisnikEmail, proizvodId )==1){
            //sad se racuna prosek
            this.izracunajteProsecnuOcenu(proizvodId);//moze i kveri
            //model  UPDATE proizvod SET prosecna_ocena=vrednost.vrednost  FROM (SELECT AVG(ocena.ocena) as vrednost FROM ocena WHERE proizvod_id=36) as  kveri  WHERE proizvod.id=36;
        }
        else{
            return false;
        }
        return true;

    }

    public Ocena vratiteOcenu(String korisnikEmail, int proizvodId){
        List<Map<String, Object>> l = b.queryForList("SELECT * FROM ocena WHERE korisnik_email=? AND proizvod_id=?", korisnikEmail, proizvodId);
        if(l.isEmpty())return null;
        Map<String, Object> m = l.get(0);
        Ocena o = Ocena.izBaze(m);
        o.setKorisnik(this.pretragaKorisnikaPoEmailu((String)m.get("korisnik_email")));
        o.setProizvod(this.vratiteProizvodPoId((Integer)m.get("proizvod_id")));
        return o;
    }

    public List<Komentar> vratiteSveKomentareZaProizvod(int proizvodId){
        List<Komentar> komentari = new ArrayList<>();
        List<Map<String, Object>> kom = b.queryForList("SELECT * FROM komentar WHERE proizvod_id=?", proizvodId);
        if(kom.isEmpty())return komentari;
        kom.forEach(m->{
            Komentar k = Komentar.izBaze(m);
            k.setKorisnik(this.pretragaKorisnikaPoEmailu((String)m.get("korisnik_email")));
            komentari.add(k);
        });
        return komentari;
    }
    public List<Ocena> vratiteSveOceneZaProizvod(int proizvodId){
        List<Ocena> ocene = new ArrayList<>();
        List<Map<String, Object>> oc = b.queryForList("SELECT * FROM ocena WHERE proizvod_id=?", proizvodId);
        oc.forEach(m->{
            Ocena o = Ocena.izBaze(m);
            o.setKorisnik(this.pretragaKorisnikaPoEmailu((String)m.get("korisnik_email")));
            ocene.add(o);
        });
        return ocene;
    }


 public Proizvod vratiteProizvodZaPrikazKorisnikuSaKomentarimaIOcenama(int proizvodId){
     List<Map<String, Object>> rez = b.queryForList("SELECT * FROM proizvod WHERE id=?", proizvodId);
     return this.napraviteProizvodZaPrikazKorisnikuSaKomentarimaIOcenama(rez.get(0));
 }

    public List<Proizvod> vratiteStoProizvoda() {
        List<Proizvod> proizvodi = new ArrayList<Proizvod>();
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM proizvod ORDER BY id DESC LIMIT 100 OFFSET 0 ");
        if(rez.isEmpty())return proizvodi;
        rez.forEach(m->{
           Proizvod p = this.napraviteProizvodZaPrikazKorisnikuSaKomentarimaIOcenama(m);
           proizvodi.add(p);
        });

        return proizvodi;
    }

    public void snimiteNarudzbenicu(Narudzbenica n){
        //prvo se snima sama narudzbenica, a onda se snimaju naruceni proizvodi jedan po jedan
        KeyHolder drzacKljuca = new GeneratedKeyHolder();
        Korisnik k = this.pretragaKorisnikaPoEmailu(n.getKupac().getEmail());
        b.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement p = con.prepareStatement(
                        "INSERT INTO narudzbenica(datum, korisnik_email, zbir, charge_id, dostava) VALUES(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
                p.setTimestamp(1,java.sql.Timestamp.valueOf(n.getDatum()));
                p.setString(2, n.getKupac().getEmail());
                p.setDouble(3,n.getZbir());
                p.setString(4, n.getChargeId());
                p.setString(5, n.getDostava());
                return p;
            }
        }, drzacKljuca);

        int id = (Integer)drzacKljuca.getKeys().get("id");
        n.setId(id);
        n.getNaruceniProizvodi().forEach(np->{
            this.snimiteNaruceniProizvod(np);
        });
    }

    public void snimiteNaruceniProizvod(NaruceniProizvod p){
        b.update("INSERT INTO naruceniproizvod(proizvod_id, narudzbenica_id, cenaputakolicina, cena, kolicina, otkazan, kupacpotvrdio, prodavacpotvrdio) VALUES(?,?,?,?,?,?,?,?)",
                    p.getProizvod().getId(),
                    p.getNarudzbenica().getId(),
                    p.getCenaPutaKolicina(),
                    p.getCena(),
                    p.getKolicina(),
                false,
                false,
                false
                );

    }
    public Narudzbenica napraviteNarudzbenicuOdPodataka(Map<String, Object> m){
        Narudzbenica n = Narudzbenica.izBaze(m);
        n.setKupac(this.pretragaKorisnikaPoEmailu((String)m.get("korisnik_email")));
        n.setNaruceniProizvodi(this.vratiteNaruceneProizvodeZaNarudzbenicu(n));
        return n;
    }
    public Narudzbenica napraviteNarudzbenicuOdPodatakaBezPovratneVeze(Map<String, Object> m){
        Narudzbenica n = Narudzbenica.izBaze(m);
        n.setKupac(this.pretragaKorisnikaPoEmailu((String)m.get("korisnik_email")));
        n.setNaruceniProizvodi(this.vratiteNaruceneProizvodeZaNarudzbenicuBezPovratneReference(n));
        return n;
    }
    public Narudzbenica napraviteNarudzbenicuOdPodatakaBezNarucenihProizvoda(Map<String, Object>m){
        Narudzbenica n = Narudzbenica.izBaze((m));
        n.setKupac(this.pretragaKorisnikaPoEmailu((String)m.get("korisnik_email")));
        return n;
    }
    public Narudzbenica vratiteNarudzbenicuPoId(int id){
        List<Map<String, Object>> l = b.queryForList("SELECT * FROM narudzbenica WHERE id=?", id);
        if(l.isEmpty())return null;
        Map<String, Object> m = l.get(0);
        return this.napraviteNarudzbenicuOdPodataka(m);
    }
    public Narudzbenica vratiteNarudzbenicuPoIdBezNarucenihProizvoda(int id){
        List<Map<String, Object>> l = b.queryForList("SELECT * FROM narudzbenica WHERE id=?", id);
        if(l.isEmpty())return null;
        Map<String, Object> m = l.get(0);
        return this.napraviteNarudzbenicuOdPodatakaBezNarucenihProizvoda(m);
    }


    public boolean obrisiteNarudzbenicu(Narudzbenica n){
        this.obrisiteNaruceneProizvodeZaNarudzbenicu(n);
        return b.update("DELETE FROM narudzbenica WHERE id=?", n.getId())>0;

    }
    public boolean obrisiteNaruceneProizvodeZaNarudzbenicu(Narudzbenica n){
        return b.update("DELETE FROM naruceniproizvod WHERE narudzbenica_id=?", n.getId())>0;
    }


    public boolean obrisiteNaruceniProizvod(int id){
        return b.update("DELETE FROM naruceniproizvod WHERE id=?", id)==1;

    }

    public Stream<Map<String, Object>> izvrsiteQuery (String query, Object...listaArgumenata){
       return b.queryForList(query, listaArgumenata).stream();
    }
    public NaruceniProizvod napraviteNaruceniproizvodSaPodacimaIzBaze(Map<String, Object> m, Narudzbenica n){
        NaruceniProizvod np = NaruceniProizvod.izBaze(m);
        np.setNarudzbenica(n);
        np.setProizvod(this.vratiteProizvodPoId((Integer)m.get("proizvod_id")));
        np.setZalba(this.vratiteZalbuZaNaruceniProizvod(np));
        return np;
    }

    public List<NaruceniProizvod> vratiteNaruceneProizvodeZaNarudzbenicu(Narudzbenica n){
        List<NaruceniProizvod> naruceniProizvodi = new ArrayList<>();
        izvrsiteQuery("SELECT DISTINCT * FROM naruceniproizvod WHERE narudzbenica_id=?", n.getId()).forEach(m->{

            naruceniProizvodi.add(this.napraviteNaruceniproizvodSaPodacimaIzBaze(m,n));
        });
        return naruceniProizvodi;
    }
    public List<NaruceniProizvod> vratiteNaruceneProizvodeZaNarudzbenicuBezPovratneReference(Narudzbenica n){
        List<NaruceniProizvod> naruceniProizvodi = new ArrayList<>();
        izvrsiteQuery("SELECT DISTINCT * FROM naruceniproizvod WHERE narudzbenica_id=?", n.getId()).forEach(m->{

            naruceniProizvodi.add(this.napraviteNaruceniproizvodSaPodacimaIzBaze(m,null));
        });
        return naruceniProizvodi;
    }

    public NaruceniProizvod vratiteNaruceniProizvodPoId(int id){
        List<Map<String, Object>> l = b.queryForList("SELECT * FROM naruceniproizvod WHERE id=?", id);
        if(l.isEmpty())return null;
        Map<String, Object> mapa = l.get(0);
        Narudzbenica n = this.vratiteNarudzbenicuPoIdBezNarucenihProizvoda((Integer)mapa.get("narudzbenica_id"));
        return this.napraviteNaruceniproizvodSaPodacimaIzBaze(mapa, n);
    }

    public List<Narudzbenica> vratiteNarudzbeniceKupcu(String email){
        List<Narudzbenica> narudzbenice = new ArrayList<>();
        this.izvrsiteQuery("SELECT * FROM narudzbenica WHERE korisnik_email=? ORDER BY datum DESC",email).forEach(m->{
            narudzbenice.add(this.napraviteNarudzbenicuOdPodatakaBezPovratneVeze(m));
        });
        return narudzbenice;
    }
    public List<NaruceniProizvod> vratiteNaruceneProizvodeProdavca(String email){
        List<NaruceniProizvod> naruceniProizvodi = new ArrayList<>();
        this.izvrsiteQuery("SELECT DISTINCT naruceniproizvod.id as id,proizvod_id, narudzbenica_id, cenaputakolicina, cena, kolicina, otkazan, prodavacpotvrdio, kupacpotvrdio FROM naruceniproizvod, proizvod, korisnik WHERE naruceniproizvod.proizvod_id=proizvod.id AND proizvod.korisnik_email = ? ORDER BY naruceniproizvod.id DESC ", email).forEach(m->{
            Narudzbenica n = this.vratiteNarudzbenicuPoIdBezNarucenihProizvoda((Integer)m.get("narudzbenica_id"));

            naruceniProizvodi.add(this.napraviteNaruceniproizvodSaPodacimaIzBaze(m,n));
            n.setNaruceniProizvodi(null);
        });
        return naruceniProizvodi;

    }

    public List<Narudzbenica> vratiteNarudzbeniceKupcuLimit(String email, int pocetak, int pomeraj){
        List<Narudzbenica> narudzbenice = new ArrayList<>();
        this.izvrsiteQuery("SELECT * FROM narudzbenica WHERE korisnik_email=? ORDER BY datum DESC LIMIT ? OFFSET ?",email, pomeraj, pocetak).forEach(m->{
            narudzbenice.add(this.napraviteNarudzbenicuOdPodatakaBezPovratneVeze(m));
        });
        return narudzbenice;
    }
    public List<NaruceniProizvod> vratiteNaruceneProizvodeProdavcaLimit(String email, int pocetak, int pomeraj){
        List<NaruceniProizvod> naruceniProizvodi = new ArrayList<>();
        this.izvrsiteQuery("SELECT DISTINCT naruceniproizvod.id as id,proizvod_id, narudzbenica_id, cenaputakolicina, cena, kolicina, otkazan, prodavacpotvrdio, kupacpotvrdio FROM naruceniproizvod, proizvod, korisnik WHERE naruceniproizvod.proizvod_id=proizvod.id AND proizvod.korisnik_email = ? ORDER BY naruceniproizvod.id DESC LIMIT ? OFFSET ?", email,pomeraj, pocetak).forEach(m->{
            Narudzbenica n = this.vratiteNarudzbenicuPoIdBezNarucenihProizvoda((Integer)m.get("narudzbenica_id"));

            naruceniProizvodi.add(this.napraviteNaruceniproizvodSaPodacimaIzBaze(m,n));
            n.setNaruceniProizvodi(null);
        });
        return naruceniProizvodi;

    }

    public boolean prodavacPotvrdjujeNaruceniProizvod(NaruceniProizvod np){
        return b.update("UPDATE naruceniproizvod SET prodavacpotvrdio =?, prodavacpotvrdio_datum=? WHERE id=? ", true,Timestamp.valueOf(LocalDateTime.now()), np.getId())==1;
    }
    public boolean kupacPotvrdjujeNaruceniProizvod(NaruceniProizvod np){
        return b.update("UPDATE naruceniproizvod SET kupacpotvrdio =? WHERE id=? ", true, np.getId())==1;
    }
    public boolean otkazanNaruceniProizvod(NaruceniProizvod np){
        return b.update("UPDATE naruceniproizvod SET otkazan =? WHERE id=? ", true, np.getId())==1;
    }


    public void snimiteNaRacunSajta(double stanje){
        b.update("UPDATE racun SET racunsajta=?", stanje);
    }
    public double vratiteStanjeRacunaSajta(){
       List<Map<String, Object>> l = b.queryForList("SELECT racunsajta FROM racun");
       Map<String, Object> m = l.get(0);
       double vrednost = (Double)m.get("racunsajta");
       return vrednost;
    }
    public void snimiteNaRacunZaPrenos(double stanje){
        b.update("UPDATE racun SET racunzaprenos=?", stanje);
    }
    public double vratiteStanjeRacunaZaPrenos(){
        List<Map<String, Object>> l = b.queryForList("SELECT racunzaprenos FROM racun");
        Map<String, Object> m = l.get(0);
        double vrednost = (Double)m.get("racunzaprenos");
        return vrednost;
    }
    public void snimiteStanjeNaRacunKorisnika(String email, double stanje){
        b.update("UPDATE korisnik SET racun=? WHERE email=?", stanje, email);
    }
    public double vratiteStanjeRacunaKorisnika(String email){
        List<Map<String, Object>> l = b.queryForList("SELECT racun FROM korisnik WHERE email=?", email);
        Map<String, Object> m = l.get(0);
        double vrednost = (Double)m.get("racun");
        return vrednost;
    }
    public Racun vratiteStanjeRacuna(){
        Racun r = new Racun();
        this.izvrsiteQuery("SELECT * FROM racun").forEach(m->{
            r.setRacunSajta((Double)m.get("racunsajta"));
            r.setRacunZaPrenos((Double)m.get("racunzaprenos"));
        });
        return r;

    }
    public Zalba vratiteZalbuZaNaruceniProizvod(NaruceniProizvod np){

        List<Map<String, Object>> l = b.queryForList("SELECT zalba.id as id, zalba.datum_kupca, zalba.datum_prodavca, zalba.tekst_prodavca, zalba.tekst_kupca FROM zalba join naruceniproizvod on zalba.id=naruceniproizvod.zalba_id WHERE naruceniproizvod.id=?", np.getId());
        if(l.isEmpty())return null;
        Zalba z = Zalba.izBaze(l.get(0));
        return z;
    }
    public boolean korisnikSeZalio(NaruceniProizvod np, String tekstKupca){
        KeyHolder kh = new GeneratedKeyHolder();
        b.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("INSERT INTO zalba(tekst_kupca, datum_kupca) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, tekstKupca);
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

                return ps;
            }
        },kh);
        Integer idZalbe= (Integer) kh.getKeys().get("id");
        return b.update("UPDATE naruceniproizvod SET zalba_id=? WHERE id=?", idZalbe, np.getId())==1;
    }
    public boolean prodavacOdgovaraNaZalbu(Zalba z){
        return b.update("UPDATE zalba SET tekst_prodavca=?,datum_prodavca=? WHERE id=?  ", z.getTekstProdavca(), Timestamp.valueOf(LocalDateTime.now()),z.getId())==1;
    }
    public boolean obrisiteZalbu(Zalba z){
        return b.update("DELETE FROM zalba WHERE id=? ", z.getId())==1;
    }


    //novi api za komentare i ocene

    public boolean snimiteKomentarNovaMetoda(String tekst, String korisnikEmail, int proizvodId){
        List<Map<String, Object>> l = b.queryForList("SELECT id FROM komentar_i_ocena WHERE korisnik_email=? AND proizvod_id=?", korisnikEmail, proizvodId);
        if(l.isEmpty()){
            return b.update("INSERT INTO komentar_i_ocena(tekst, datum, korisnik_email, proizvod_id) VALUES(?,?,?,?)", tekst,Timestamp.valueOf(LocalDateTime.now()), korisnikEmail, proizvodId )==1;
        }
        else{
            int id = (Integer) l.get(0).get("id");
            return b.update("UPDATE komentar_i_ocena SET tekst=? , datum=? WHERE korisnik_email=? AND proizvod_id=? ",tekst,Timestamp.valueOf(LocalDateTime.now()), korisnikEmail, proizvodId)==1;
        }
    }
    public boolean snimiteOcenuNovaMetoda(int ocena, String korisnikEmail, int proizvodId){
        List<Map<String, Object>> l = b.queryForList("SELECT id FROM komentar_i_ocena WHERE korisnik_email=? AND proizvod_id=?", korisnikEmail, proizvodId);
        boolean rezultat=false;
        if(l.isEmpty()){
            rezultat= b.update("INSERT INTO komentar_i_ocena(ocena, korisnik_email, proizvod_id) VALUES(?,?,?)", ocena,Timestamp.valueOf(LocalDateTime.now()), korisnikEmail, proizvodId )==1;
        }
        else{
            int id = (Integer) l.get(0).get("id");
            rezultat= b.update("UPDATE komentar_i_ocena SET ocena=? WHERE korisnik_email=? AND proizvod_id=? ",ocena, korisnikEmail, proizvodId)==1;
        }
        if(rezultat){
            this.izracunajteProsecnuOcenuNovaMetoda(proizvodId);//to je za to i ostavljeno za prosecnu ocenu !!!
        }
        return rezultat;

    }
    public KomentarIOcena vratiteKomentarIOcenuZaKorisnikaIProizvodNovi(String korisnikEmail, int proizvodId ){
        List<Map<String, Object>> l = b.queryForList("SELECT * FROM komentar_i_ocena WHERE korisnik_email=? AND proizvod_id=?", korisnikEmail, proizvodId);
        if(l.isEmpty()) {
            return null;
        }
        Map<String, Object> mapa = l.get(0);
        KomentarIOcena k = KomentarIOcena.izBaze(mapa);
        k.setKorisnik(this.pretragaKorisnikaPoEmailu((String)mapa.get("korisnik_email")));
        return k;
    }

    List<KomentarIOcena> vratiteSveKomentareIOceneZaProizvodNovi(int proizvodId){
        List<KomentarIOcena> lista = new ArrayList<>();
        this.izvrsiteQuery("SELECT * FROM komentar_i_ocena WHERE proizvod_id=?",proizvodId).forEach(m->{
            KomentarIOcena k = KomentarIOcena.izBaze(m);
            k.setKorisnik(this.pretragaKorisnikaPoEmailu((String)m.get("korisnik_email")));
            lista.add(k);
        });
        return lista;
    }
    public boolean izracunajteProsecnuOcenuNovaMetoda(int proizvodId){
        //to je ipak pravilnije i brze da ga baza izracuna
        return b.update("UPDATE proizvod SET prosecna_ocena=kveri.vrednost  FROM (SELECT AVG(komentar_i_ocena.ocena) as vrednost FROM komentar_i_ocena  WHERE proizvod_id=?) as  kveri  WHERE proizvod.id=?", proizvodId, proizvodId)==1;
    }

    public Proizvod napraviteProizvodZaPrikazKorisnikuSaKomentarimaIOcenamaNovi(Map<String, Object> mapa){
        Proizvod p = this.napraviteProizvodSaPodacimaIzBaze(mapa);
        //tu idu komentari
        p.setKomentariIOcene(this.vratiteSveKomentareIOceneZaProizvodNovi(p.getId()));
        return p;
    }
    public Proizvod vratiteProizvodZaPrikazKorisnikuSaKomentarimaIOcenamaNovi(int proizvodId){
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM proizvod WHERE id=?", proizvodId);
        return this.napraviteProizvodZaPrikazKorisnikuSaKomentarimaIOcenamaNovi(rez.get(0));
    }
    public List<Proizvod> vratiteStoProizvodaNovi() {
        List<Proizvod> proizvodi = new ArrayList<Proizvod>();
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM proizvod ORDER BY id DESC LIMIT 100 OFFSET 0 ");
        if(rez.isEmpty())return proizvodi;
        rez.forEach(m->{
            Proizvod p = this.napraviteProizvodZaPrikazKorisnikuSaKomentarimaIOcenamaNovi(m);
            proizvodi.add(p);
        });

        return proizvodi;
    }
    public void proveriteDaLiSuTokeniZaPromenuSifreValidni(int brojMinuta){
        long period = 60*1000*brojMinuta;
        long milisekunde = System.nanoTime();
        List<Map<String,Object>> rez = b.queryForList("SELECT email, vreme FROM promenasifre");
        rez.forEach(m->{
            String email = (String)m.get("email");
            Long vreme = (Long)m.get("vreme");
            if(vreme-milisekunde>=period){
                b.update("DELETE FROM promenasifre WHERE email=?", email);
            }

        });
    }
    public void proveriteDaLiJeProdavacOdgovorioNaZalbu(int brojDana){
        long brojMiliSekundi = brojDana*24*60*60*1000;
        this.izvrsiteQuery("SELECT id, datum_kupca FROM zalba WHERE tekst_prodavca IS NULL  ").forEach(m->{
           // korisnik otkazuje plus se skidaju pare od racun
        int id = (Integer)m.get("id");
        long datumKupca = ((Timestamp)m.get("datum_kupca")).getTime();
        if(datumKupca+brojMiliSekundi>System.nanoTime()){
            //radi ga
            List<Map<String, Object>> l = b.queryForList("SELECT id FROM naruceniproizvod WHERE zalba_id=?", id);
            int npId = (Integer)l.get(0).get("id");
            NaruceniProizvod np = this.vratiteNaruceniProizvodPoId(npId);
            this.otkazanNaruceniProizvod(np);
            if(this.placanje.ponistitePlacanje(np.getNarudzbenica().getChargeId(),np.getCenaPutaKolicina())){
                this.upravljanjeRacunom.skiniteSaRacunaZaPrenos(np.getCenaPutaKolicina());
            }
        }
        });
    }
    public void proveriteDaLiJeKupacOdgovorioNaPoslatProizvod(int brojDana){
        long brojMiliSekundi = brojDana*24*60*60*1000;
        this.izvrsiteQuery("SELECT id, prodavacpotvrdio_datum from naruceniproizvod WHERE zalba_id is null AND prodavacpotvrdio=? AND kupacpotvrdio=?", true, false).forEach(m->{
            //ja sam ga bio napravio da potvrdi svaki na koji naidje
            int id = (Integer)m.get("id");
            long prodavacPotvrdioDatum = ((Timestamp)m.get("prodavacpotvrdio_datum")).getTime();
            if(prodavacPotvrdioDatum+brojMiliSekundi>System.nanoTime()) {
                NaruceniProizvod np = this.vratiteNaruceniProizvodPoId(id);
                this.kupacPotvrdjujeNaruceniProizvod(np);
                this.upravljanjeRacunom.prebaciteNaRacunKorisnika(np.getNarudzbenica().getKupac().getEmail(), np.getCenaPutaKolicina());
            }
        });
    }
    public List<Korisnik> vratiteSagovornikeZaKorisnika(String email){
        List<Korisnik> lista = new ArrayList<>();
        this.izvrsiteQuery("SELECT DISTINCT narudzbenica.korisnik_email as kupac, proizvod.korisnik_email as prodavac FROM naruceniproizvod, narudzbenica, proizvod WHERE naruceniproizvod.narudzbenica_id=narudzbenica.id AND naruceniproizvod.proizvod_id=proizvod.id AND narudzbenica.korisnik_email=?", email).forEach(m->{
            lista.add(this.pretragaKorisnikaPoEmailu((String)m.get("prodavac")));
        });

        return lista;
    }

    public void snimitePoruku(String od, String za, String tekst){//netestirano
        b.update("INSERT INTO poruka(od, za, tekst, datum) VALUES(?,?,?,?)", od, za, tekst, Timestamp.valueOf(LocalDateTime.now()));

    }
    public List<Poruka> vratitePoruke(String email1, String email2, int limit, int pomeraj){
        List<Poruka> poruke = new ArrayList<>();
        this.izvrsiteQuery("SELECT * FROM poruka WHERE (od=?AND za= ?) OR (od=? AND za=?) ORDER BY id DESC LIMIT ? OFFSET ?", email1, email2, email2, email1, limit, pomeraj).forEach(m->{
            Poruka p = Poruka.izBaze(m);
            poruke.add(p);
        });
        return poruke;
    }
    public List<NaruceniProizvod> izvestajZaAdmina(int limit, int offset) {
        List<NaruceniProizvod> l = new ArrayList<>();

        this.izvrsiteQuery("SELECT naruceniproizvod.id as id  FROM narudzbenica, naruceniproizvod WHERE naruceniproizvod.narudzbenica_id=narudzbenica.id ORDER BY datum DESC LIMIT ? OFFSET ?", limit, offset).forEach(m->{
            NaruceniProizvod np = this.vratiteNaruceniProizvodPoId((Integer)m.get("id"));
            l.add(np);
        });

        return l;
    }


    public JdbcTemplate getB() {
        return b;
    }

    public void setB(JdbcTemplate b) {
        this.b = b;
    }

    public Sifra getRadSaSifrom() {
        return radSaSifrom;
    }

    public void setRadSaSifrom(Sifra radSaSifrom) {
        this.radSaSifrom = radSaSifrom;
    }

    public Tokeni getRadSaTokenima() {
        return radSaTokenima;
    }

    public void setRadSaTokenima(Tokeni radSaTokenima) {
        this.radSaTokenima = radSaTokenima;
    }

    public GenerisanjeTokena getRadSaGenerisanjemTokena() {
        return radSaGenerisanjemTokena;
    }

    public void setRadSaGenerisanjemTokena(GenerisanjeTokena radSaGenerisanjemTokena) {
        this.radSaGenerisanjemTokena = radSaGenerisanjemTokena;
    }

    public void setSlike(Slike slike) {
        this.slike = slike;
    }

    public ProizvodSmeDaSeMenja getProizvodSmeDaSeMenja() {
        return proizvodSmeDaSeMenja;
    }

    public void setProizvodSmeDaSeMenja(ProizvodSmeDaSeMenja proizvodSmeDaSeMenja) {
        this.proizvodSmeDaSeMenja = proizvodSmeDaSeMenja;
    }



    public String getNeuspesnoLogovanjePoruka() {
        return neuspesnoLogovanjePoruka;
    }

    public void setNeuspesnoLogovanjePoruka(String neuspesnoLogovanjePoruka) {
        this.neuspesnoLogovanjePoruka = neuspesnoLogovanjePoruka;
    }

}
