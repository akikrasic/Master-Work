package srb.akikrasic.baza.novipaket.baza;

import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovnaSaHesMapom;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.NeregistrovaniKorisnik;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/24/17.
 */
@Component
public class BazaNepotvrdjeniKorisnici extends BazaOsnovna {



    public void snimiteNovogNepotvrdjenogKorisnika(String imeNaziv, String email, byte[] sifra, String adresa, String mesto, String tel1, String tel2, String tel3, String token) {
        b.update("INSERT INTO nepotvrdjenikorisnik(imeNaziv, email, sifra, adresa, mesto,tel1,tel2,tel3, token) VALUES(?,?,?,?,?,?,?,?,?)", imeNaziv, email, sifra, adresa, mesto, tel1, tel2, tel3, token);
    }

    public boolean daLiJeEmailSlobodanUObeTabele(String email) {
        List<Map<String, Object>> rezultat = b.queryForList("SELECT email FROM nepotvrdjenikorisnik WHERE email=?", email);
        if (!rezultat.isEmpty()) return false;
        rezultat = b.queryForList("SELECT email FROM korisnik WHERE email=?", email);
        return rezultat.isEmpty();
    }

    public Korisnik daLiJeMogucaPotvrdaKorisnika(String email, String token) {
        List<Map<String, Object>> rezultat = b.queryForList("SELECT id, email, imenaziv, sifra, adresa, mesto, tel1, tel2, tel3, token FROM nepotvrdjenikorisnik WHERE email=?", email);
        //System.out.println(rezultat);
        if (rezultat.isEmpty()) return null;
        String tokenIzBaze = (String) rezultat.get(0).get("token");

        if (!token.equals(tokenIzBaze)) return null;
        NeregistrovaniKorisnik nk = NeregistrovaniKorisnik.napraviteNeregistrovanogKorisnikaIzMape(rezultat.get(0));
        return Korisnik.korisnikIzneregistrovanogKorisnika(nk);
    }

    public boolean daLiJeMogucaPotvrdaKorisnika1(String email, String token) {
        List<Map<String, Object>> rezultat = b.queryForList("SELECT id, email, imenaziv, sifra, adresa, mesto, tel1, tel2, tel3, token FROM nepotvrdjenikorisnik WHERE email=?", email);
        //System.out.println(rezultat);
        if (rezultat.isEmpty()) return false;
        String tokenIzBaze = (String) rezultat.get(0).get("token");

        if (!token.equals(tokenIzBaze)) return false;
        NeregistrovaniKorisnik nk = NeregistrovaniKorisnik.napraviteNeregistrovanogKorisnikaIzMape(rezultat.get(0));
        Korisnik k = Korisnik.korisnikIzneregistrovanogKorisnika(nk);
        this.potvrditeKorisnika(k);
        return true;
    }
    public boolean potvrditeKorisnika(Korisnik k) {
        if (b.update("DELETE FROM nepotvrdjenikorisnik WHERE email=?", k.getEmail()) != 1) return false;
        return b.update("INSERT INTO korisnik(imeNaziv, email, sifra, adresa, mesto,tel1,tel2,tel3, token, racun) VALUES(?,?,?,?,?,?,?,?,?,?)", k.getImeNaziv(), k.getEmail(), k.getSifra(), k.getAdresa(), k.getMesto(), k.getTel1(), k.getTel2(), k.getTel3(), null, 0) == 1;

    }

    public boolean obrisiteNepotvrdjenogKorisnika(String email){
        return (b.update("DELETE FROM nepotvrdjenikorisnik WHERE email=?", email) == 1);

    }
    public HashMap<String, NeregistrovaniKorisnik> sviNepotvrdjeniKorisniciNaPocetku(){
        HashMap<String, NeregistrovaniKorisnik> mapa = new LinkedHashMap<>();
        this.izvrsiteQuery("SELECT * FROM nepotvrdjenikorisnik").forEach(m->{
            NeregistrovaniKorisnik nk = NeregistrovaniKorisnik.napraviteNeregistrovanogKorisnikaIzMape(m);
            mapa.put(nk.getEmail(), nk);
        });

        return mapa;
    }


}
