package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovnaSaHesMapom;
import srb.akikrasic.baza.novipaket.repozitorijum.NarudzbeniceRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.ZalbeRepozitorijum;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.domen.Narudzbenica;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/26/17.
 */
@Component
public class BazaNaruceniProizvod extends BazaOsnovna {

    public void snimiteNaruceniProizvod(NaruceniProizvod np){
        KeyHolder drzacKljuca = new GeneratedKeyHolder();
        b.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement st = con.prepareStatement("INSERT INTO naruceniproizvod(proizvod_id, narudzbenica_id, cenaputakolicina, cena, kolicina, otkazan, kupacpotvrdio, prodavacpotvrdio, komentar) VALUES(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                st.setInt(1, np.getProizvod().getId());
                st.setInt(2, np.getNarudzbenica().getId());
                st.setDouble(3,np.getCenaPutaKolicina());
                st.setDouble(4,np.getCena());
                st.setDouble(5,np.getKolicina());
                st.setBoolean(6,false);
                st.setBoolean(7,false);
                st.setBoolean(8,false);
                st.setString(9, np.getKomentar());
                return st;
            }
        }, drzacKljuca);

        np.setId((Integer)drzacKljuca.getKeys().get("id"));

    }

    public boolean obrisiteNaruceneProizvodeZaNarudzbenicu(Narudzbenica n){
        return b.update("DELETE FROM naruceniproizvod WHERE narudzbenica_id=?", n.getId())>0;
    }


    public boolean obrisiteNaruceniProizvod(int id){
        return b.update("DELETE FROM naruceniproizvod WHERE id=?", id)==1;

    }



    public List<Integer> vratiteNaruceneProizvodeProdavca(String email){
        List<Integer> naruceniProizvodi = new ArrayList<>();
        this.izvrsiteQuery("SELECT DISTINCT naruceniproizvod.id as id FROM naruceniproizvod, proizvod, korisnik WHERE naruceniproizvod.proizvod_id=proizvod.id AND proizvod.korisnik_email = ? ORDER BY naruceniproizvod.id DESC ", email).forEach(m->{
            naruceniProizvodi.add((Integer)m.get("id"));
        });
        return naruceniProizvodi;

    }
    public List<Integer> vratiteNaruceneProizvodeProdavcaLimit(String email, int pocetak, int pomeraj){
        List<Integer> naruceniProizvodi = new ArrayList<>();
        this.izvrsiteQuery("SELECT DISTINCT naruceniproizvod.id as id FROM naruceniproizvod, proizvod, korisnik WHERE naruceniproizvod.proizvod_id=proizvod.id AND proizvod.korisnik_email = ? ORDER BY naruceniproizvod.id DESC LIMIT ? OFFSET ?", email,pomeraj, pocetak).forEach(m->{

            naruceniProizvodi.add((Integer)m.get("id"));
        });
        return naruceniProizvodi;

    }
    public boolean prodavacPotvrdjujeNaruceniProizvod(NaruceniProizvod np){
        return b.update("UPDATE naruceniproizvod SET prodavacpotvrdio =?, prodavacpotvrdio_datum=? WHERE id=? ", true, Timestamp.valueOf(LocalDateTime.now()), np.getId())==1;
    }
    public boolean kupacPotvrdjujeNaruceniProizvod(NaruceniProizvod np){
        return b.update("UPDATE naruceniproizvod SET kupacpotvrdio =? WHERE id=? ", true, np.getId())==1;
    }
    public boolean otkazanNaruceniProizvod(NaruceniProizvod np){
        return b.update("UPDATE naruceniproizvod SET otkazan =? WHERE id=? ", true, np.getId())==1;
    }
     public List<Integer> izvestajZaAdmina(int limit, int offset) {
        List<Integer> l = new ArrayList<>();

        this.izvrsiteQuery("SELECT naruceniproizvod.id as id  FROM narudzbenica, naruceniproizvod WHERE naruceniproizvod.narudzbenica_id=narudzbenica.id ORDER BY datum DESC LIMIT ? OFFSET ?", limit, offset).forEach(m->{
            l.add((Integer)m.get("id"));
        });
        return l;
    }

    public List<Integer> vratiteNaruceneProizvodeZaNarudzbenicu(Narudzbenica n){
        List<Integer> listaId= new ArrayList<>();

        izvrsiteQuery("SELECT id FROM naruceniproizvod WHERE narudzbenica_id=?", n.getId()).forEach(m->{
            listaId.add((Integer)m.get("id"));
        });

        return listaId;
    }


}
