package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovnaSaHesMapom;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.domen.KomentarIOcena;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/26/17.
 */
@Component
public class BazaKomentarOcena extends BazaOsnovna {



    public boolean snimiteKomentarNovaMetodaAkoPostoji(String tekst, int komentarIOcenaID){
        return  b.update("UPDATE komentar_i_ocena SET tekst=?, datum=? WHERE id=?",tekst,Timestamp.valueOf(LocalDateTime.now()), komentarIOcenaID)==1;
    }
    public int snimiteKomentarNovaMetodaPrviPut(String tekst, String korisnikEmail, int proizvodId){
        KeyHolder drzac = new GeneratedKeyHolder();
        b.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("INSERT INTO komentar_i_ocena(tekst, datum, korisnik_email, proizvod_id) VALUES(?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, tekst);
                ps.setTimestamp(2,Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(3, korisnikEmail);
                ps.setInt(4, proizvodId);
                return ps;
            }
        }, drzac);
        return (Integer)drzac.getKeys().get("id");
    }


    public boolean snimiteOcenuNovaMetodaAkoPostoji(int ocena, int id){
        return b.update("UPDATE komentar_i_ocena SET ocena=? WHERE id=? ",ocena, id)==1;
    }

    public int snimiteOcenuNovaMetodaPrviPut(int ocena, String korisnikEmail, int proizvodId){
        KeyHolder drzacKljuca = new GeneratedKeyHolder();
        b.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("INSERT INTO komentar_i_ocena(ocena, korisnik_email, proizvod_id) VALUES(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, ocena);
                ps.setString(2, korisnikEmail);
                ps.setInt(3, proizvodId);
                return ps;
            }
        }, drzacKljuca);
        return (Integer)drzacKljuca.getKeys().get("id");
    }





    public void izracunajteProsecnuOcenuNovaMetoda(int proizvodId){
        //to je ipak pravilnije i brze da ga baza izracuna
        boolean rez =  b.update("UPDATE proizvod SET prosecna_ocena=kveri.vrednost  FROM (SELECT AVG(komentar_i_ocena.ocena) as vrednost FROM komentar_i_ocena  WHERE proizvod_id=?) as  kveri  WHERE proizvod.id=?", proizvodId, proizvodId)==1;
        //dana 12.01.2018. ajde neka si sedi ovo s bocni efekat ali ne trebe teka
    }
    //logicki ipak pripada tu iako fizicki ne pripada jer se poziva posle racunanja prosecne ocene
    public double vratiteProsecnuOcenuZaProizvod(int proizvodId){
        //додато дана 12.01.2018.
        izracunajteProsecnuOcenuNovaMetoda(proizvodId);
        List<Map<String, Object>> l = b.queryForList("SELECT prosecna_ocena FROM proizvod WHERE id=?", proizvodId);
        String ocena =  l.get(0).get("prosecna_ocena").toString();//desavali su mi se problemi gde mi je za konverziju u double izbacivalo gresku da je tip float a kad prebacim u float obrnuto, a ovako nema nikakvih problema
        return Double.parseDouble(ocena);
    }

    public boolean daLiKorisnikSmeDaKomentariseProizvod(String email, int proizvodId){
        List<Map<String, Object>> l = b.queryForList( "SELECT DISTINCT  proizvod.id FROM proizvod, naruceniproizvod, narudzbenica WHERE narudzbenica.id=naruceniproizvod.narudzbenica_id AND proizvod.id=naruceniproizvod.proizvod_id AND narudzbenica.korisnik_email=? AND proizvod.id=?", email, proizvodId);
        return l.size()==1;
    }

}
