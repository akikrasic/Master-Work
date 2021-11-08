package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovnaSaHesMapom;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaReferenca;
import srb.akikrasic.domen.Kategorija;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by aki on 11/25/17.
 */
@Component
public class BazaKategorija extends BazaOsnovna {
    @Autowired
    private BazaReferenca b;

    public int dodajteKategoriju(String naziv) {
        KeyHolder drzacKljuca = new GeneratedKeyHolder();
        b.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("INSERT INTO kategorija(naziv) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,naziv);
                return ps;
            }
        }, drzacKljuca);

        int id = (Integer)drzacKljuca.getKeys().get("id");
        return id;

    }


    public boolean izmenaKategorije(int id, String noviNaziv) {
        return b.update("UPDATE kategorija SET naziv =? WHERE id=?", noviNaziv, id)==1;

    }

    public boolean obrisiteKategoriju(int id) {
        return (b.update("DELETE FROM kategorija WHERE id=?", id)) == 1;
    }



}
