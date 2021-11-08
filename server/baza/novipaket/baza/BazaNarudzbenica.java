package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovnaSaHesMapom;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.domen.Narudzbenica;
import srb.akikrasic.domen.NarudzbenicaBezNarucenihProizvoda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/26/17.
 */
@Component
public class BazaNarudzbenica extends BazaOsnovna {


    public void snimiteNarudzbenicu(Narudzbenica n){
        //prvo se snima sama narudzbenica, a onda se snimaju naruceni proizvodi jedan po jedan
        KeyHolder drzacKljuca = new GeneratedKeyHolder();
        b.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement p = con.prepareStatement(
                        "INSERT INTO narudzbenica(datum, korisnik_email, zbir, charge_id, dostava) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
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
    }





    public boolean obrisiteNarudzbenicu(Narudzbenica n){
        return b.update("DELETE FROM narudzbenica WHERE id=?", n.getId())>0;

    }

    public List<Integer> vratiteNarudzbeniceKupcu(String email){
        List<Integer> lista = new ArrayList<>();
        this.izvrsiteQuery("SELECT id FROM narudzbenica WHERE korisnik_email=? ORDER BY datum DESC",email).forEach(m->{
            lista.add((Integer)m.get("id"));
        });
        return lista;
    }
    public List<Integer> vratiteNarudzbeniceKupcuLimit(String email, int pocetak, int pomeraj){
        List<Integer> narudzbeniceId = new ArrayList<>();
        this.izvrsiteQuery("SELECT id FROM narudzbenica WHERE korisnik_email=? ORDER BY datum DESC LIMIT ? OFFSET ?",email, pomeraj, pocetak).forEach(m->{
            narudzbeniceId.add((Integer)m.get("id"));
        });
        return narudzbeniceId;
    }



}
