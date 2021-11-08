package srb.akikrasic.baza.novipaket.baza;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.domen.Proizvod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/25/17.
 */
@Component
public class BazaProizvod extends BazaOsnovna {


    public int dodajteNoviProizvod(String email, String naziv, String opis, double cena, int kategorijaId) {
        KeyHolder drzacKljuca = new GeneratedKeyHolder();
        // ne treba mi to uopste Korisnik korisnikEmail = this.pretragaKorisnikaPoEmailu(email);

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
    public boolean obrisiteProizvod(int id) {
        return b.update("DELETE FROM proizvod WHERE id=?", id) == 1;
    }


    private List<Integer> vratiteListuIdjevaZaQueryIParametre(String query, Object... parametri){
        List<Integer> l = new ArrayList<>();

        List<Map<String, Object>> rez = b.queryForList(query, parametri);
        if (rez.isEmpty()) return l;
        rez.forEach(m -> {
            l.add((Integer)m.get("id"));
        });
        return l;
    }

    public List<Integer> vratiteSveProizvodeNekogKorisnikaPoEmailu(String email) {


        return this.vratiteListuIdjevaZaQueryIParametre("SELECT id FROM proizvod WHERE korisnik_email=? and aktivan=true ORDER BY id ", email);

    }
    public List<Integer> vratiteSveProizvodeNekogKorisnikaPoEmailuDaIhOnMenja(String email) {


        //ovde se svi vracaju i aktivni i neaktivni
        return this.vratiteListuIdjevaZaQueryIParametre("SELECT id FROM proizvod WHERE korisnik_email=? ORDER BY id ", email);

    }

    public String like(String s){
        return new StringBuilder("%").append(s).append("%").toString();
    }

    public List<Integer> vratiteSveProizvode(){

       return this.vratiteListuIdjevaZaQueryIParametre("SELECT id FROM proizvod ORDER BY naziv");

    }

    public List<Integer> vratiteSveProizvodeKorisnika(String email){

        return this.vratiteListuIdjevaZaQueryIParametre("SELECT * FROM proizvod WHERE korisnik_email=? ORDER BY naziv", email);

    }

    public List<Integer> pretragaProizvoda(String imeNazivKorisnika,
                                                       String email,
                                                       String nazivProizvoda,
                                                       double cenaOd,//samo ne sme da bude null sto se pre toga proverava
                                                       double cenaDo,//isto vazi
                                                       List<String> kljucneReci,
                                                       String kategorijaNaziv,
                                                       String opis
    ) {
        List<Integer> lista = new ArrayList<>();
        //zbog citljivosti je  realizovano po redovima
        StringBuilder query = new StringBuilder(
                "SELECT DISTINCT  proizvod.id AS id, proizvod.naziv ").append(
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

        if(promenljiviDeo.toString().equals(""))return lista;




        query.append(promenljiviDeo).append(" '1'='1' ORDER BY proizvod.naziv  ");//izbaceno GROUP BY proizvod.id

        //resen problem izbacen limit, sam cu da si limitiram tamo kad imam sve podatke i dovidjenja
        //kad korisnik klikne na pretrazite, onda se nadju novi podaci i onda si njih opet sam limitiram, a ne da se utepujem bez veze
        List<Map<String, Object>> rez = b.queryForList(query.toString(), parametri.toArray());

        rez.forEach(m->{
            lista.add((Integer)m.get("id"));//ajde tu sam posebno, da ga ne komplikujemo..
        });
        return lista;
    }



    //jedan treba da se obrise bez problema
    public List<Integer> vratiteStoProizvoda() {
        return this.vratiteListuIdjevaZaQueryIParametre("SELECT * FROM proizvod ORDER BY id DESC LIMIT 100 OFFSET 0 ");

    }
    public List<Integer> vratiteStoProizvodaNovi() {
        return this.vratiteListuIdjevaZaQueryIParametre("SELECT * FROM proizvod ORDER BY id DESC LIMIT 100 OFFSET 0 ");

    }

}
