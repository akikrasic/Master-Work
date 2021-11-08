package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.domen.Blog;
import srb.akikrasic.domen.Korisnik;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aki on 2/20/18.
 */
@Component
public class BazaBlog extends BazaOsnovna {

    public int snimiteBlog(String naslov, String tekst, Korisnik k, LocalDateTime datum){
        KeyHolder drzacKljuca= new GeneratedKeyHolder();
        b.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement p = con.prepareStatement("INSERT INTO blog(datum,naslov, tekst, korisnik_email) VALUES(?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);

                Timestamp t = Timestamp.valueOf(datum);
                p.setTimestamp(1, t);
                p.setString(2, naslov);
                p.setString(3, tekst);
                p.setString(4, k.getEmail());

                return p;
            }
        }, drzacKljuca);
        return (Integer)drzacKljuca.getKeys().get("id");
    }
    public boolean izmeniteBlog(int id, String naslov, String tekst, LocalDateTime datum){
        return b.update("UPDATE blog SET naslov=? , datum=? , tekst=? WHERE id=?", naslov, Timestamp.valueOf(datum), tekst, id)==1;
    }
    public boolean obrisiteBlog(int id){
        return b.update("DELETE FROM blog WHERE id=?", id)==1;
    }

    public List<Integer> vratiteBlogoveKorisnikaSaPomerajem(String email, int pocetak, int pomeraj){
        List<Integer> lista = new ArrayList<>();
        this.izvrsiteQuery("SELECT id FROM blog WHERE korisnik_email=? ORDER BY datum desc LIMIT ? OFFSET ?",email,pomeraj,  pocetak).forEach(m->{
            lista.add((Integer)m.get("id"));
        });
        return lista;
    }
}
