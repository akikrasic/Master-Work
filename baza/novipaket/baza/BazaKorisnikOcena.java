package srb.akikrasic.baza.novipaket.baza;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 2/23/18.
 */
@Component
public class BazaKorisnikOcena extends BazaOsnovna {
    private String queryZaProveru="SELECT count(naruceniproizvod.id) as broj FROM naruceniproizvod, proizvod, narudzbenica WHERE proizvod.id=naruceniproizvod.proizvod_id AND narudzbenica.id=naruceniproizvod.narudzbenica_id AND narudzbenica.korisnik_email=? AND proizvod.korisnik_email=?";

    class Rezultat{
        public long broj;
    }
    private Rezultat rez = new Rezultat();

    public boolean daLiKupacMozeDaOcenjujeProdavca(String kupac, String prodavac){

        this.izvrsiteQuery(queryZaProveru, kupac, prodavac).forEach(m->{
             rez.broj = (Long)m.get("broj");

        });
        return rez.broj>0;
    }
    public int dodajteOcenuKorisnika(int ocena, LocalDateTime datum,String kupacEmail, String prodavacEmail){
        KeyHolder kh = new GeneratedKeyHolder();
        b.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement st = con.prepareStatement("INSERT INTO korisnikocena(ocena, datum, kupac_email, prodavac_email) VALUES (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                st.setInt(1, ocena);
                st.setTimestamp(2, Timestamp.valueOf(datum));
                st.setString(3,kupacEmail);
                st.setString(4, prodavacEmail);
                return st ;
            }
        }, kh);
        int id = (Integer) kh.getKeys().get("id");
        return id;
    }

    public boolean obrisiteOcenuKorisnika(int id){
        return b.update("DELETE FROM korisnikocena WHERE id=?", id)==1;
    }

    public boolean izmeniteOcenuKorisnika( int id,LocalDateTime datum, int ocena){
        return b.update("UPDATE korisnikocena SET datum=?, ocena=? WHERE id=?", Timestamp.valueOf(datum), ocena, id)==1;
    }

}
