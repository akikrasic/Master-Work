package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.Poruka;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aki on 11/26/17.
 */
@Component
public class BazaPoruka extends BazaOsnovna {

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    public List<Korisnik> vratiteSagovornikeZaKorisnika(String email){
        List<Korisnik> lista = new ArrayList<>();
        this.izvrsiteQuery("SELECT DISTINCT narudzbenica.korisnik_email as kupac, proizvod.korisnik_email as prodavac FROM naruceniproizvod, narudzbenica, proizvod WHERE naruceniproizvod.narudzbenica_id=narudzbenica.id AND naruceniproizvod.proizvod_id=proizvod.id AND narudzbenica.korisnik_email=?", email).forEach(m->{
            lista.add(korisniciRepozitorijum.pretragaKorisnikaPoEmailu((String)m.get("prodavac")));
        });

        return lista;
    }

    public void snimitePoruku(String od, String za, String tekst){//netestirano
        b.update("INSERT INTO poruka(od, za, tekst, datum) VALUES(?,?,?,?)", od, za, tekst, Timestamp.valueOf(LocalDateTime.now()));

    }
    public List<Poruka> vratitePoruke(String email1, String email2, int limit, int pomeraj){
        List<Poruka> poruke = new ArrayList<>();
        this.izvrsiteQuery("SELECT * FROM poruka WHERE (od=?AND za= ?) OR (od=? AND za=?) ORDER BY id DESC LIMIT ? OFFSET ?", email1, email2, email2, email1, limit, pomeraj).forEach(m->{
            Poruka p = Poruka.izBaze(m);
            poruke.add(p);
        });
        return poruke;
    }

}
