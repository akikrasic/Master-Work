package srb.akikrasic.baza.novipaket.baza.nesredjeni;

import srb.akikrasic.domen.Komentar;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/26/17.
 */
//stari api prevazidjeno nema sta da se dira
public class BazaKomentar {

    public List<Komentar> vratiteKomentareZaProizvod(int proizvodId) {
        return null;
    }


    public boolean snimiteKomentar(String tekst, LocalDateTime datum, String email, int id) {
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
/*
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
    */ //dodato ovo ostavi samo jednu }
return false;
    }
}
