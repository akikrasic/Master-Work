package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovnaSaHesMapom;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.domen.Zalba;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/26/17.
 */
@Component
public class BazaZalba extends BazaOsnovna {

    public int korisnikSeZalio(NaruceniProizvod np, String tekstKupca){
        KeyHolder kh = new GeneratedKeyHolder();
        b.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("INSERT INTO zalba(tekst_kupca, datum_kupca) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, tekstKupca);
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                return ps;
            }
        },kh);
        Integer idZalbe= (Integer) kh.getKeys().get("id");
        b.update("UPDATE naruceniproizvod SET zalba_id=? WHERE id=?", idZalbe, np.getId());
        return idZalbe;

    }
    public boolean prodavacOdgovaraNaZalbu(Zalba z){
        return b.update("UPDATE zalba SET tekst_prodavca=?,datum_prodavca=? WHERE id=?  ", z.getTekstProdavca(), Timestamp.valueOf(LocalDateTime.now()),z.getId())==1;
    }
    public boolean obrisiteZalbu(Zalba z){
        return b.update("DELETE FROM zalba WHERE id=? ", z.getId())==1;
    }


}
