package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaReferenca;
import srb.akikrasic.domen.KljucnaRec;
import srb.akikrasic.domen.Proizvod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by aki on 11/26/17.
 */
@Component
public class BazaKljucneReci {
    @Autowired
    private BazaReferenca b;
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
}
