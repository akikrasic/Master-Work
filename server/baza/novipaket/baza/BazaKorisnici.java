package srb.akikrasic.baza.novipaket.baza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovnaSaHesMapom;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaReferenca;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.token.GenerisanjeTokena;
import srb.akikrasic.token.Sifra;

import java.util.*;

/**
 * Created by aki on 11/24/17.
 */
@Component
public class BazaKorisnici extends BazaOsnovna {
    @Autowired
    private BazaReferenca b;

    @Autowired
    private GenerisanjeTokena radSaGenerisanjemTokena;

    @Autowired
    private Sifra radSaSifrom;



    public String posaljiteZahtevZaPromenuSifre(String email) {
        List<Map<String, Object>> rez = b.queryForList("SELECT email, imeNaziv, sifra FROM korisnik WHERE email=?", email);

        if (rez.isEmpty()) return null;
        Map<String, Object> m = rez.get(0);

        String imeNaziv = (String) m.get("imeNaziv");
        byte[] sifra = (byte[]) m.get("sifra");
        long vreme = System.nanoTime();
        String token = radSaGenerisanjemTokena.generisiteTokenZaPromenuSifre(imeNaziv, email, sifra, vreme);
        rez = b.queryForList("SELECT email FROM promenasifre WHERE email=?", email);
        //29.11.2017.
        System.out.println(email);
        System.out.println(vreme);
        System.out.println(token);
        if (rez.isEmpty()) {
            b.update("INSERT INTO promenasifre(email, vreme, token) VALUES(?,?,?)", email, vreme, token);
        } else {
            b.update("UPDATE promenasifre SET token=?, vreme=? WHERE email=? ", token, vreme, email);
        }
        return token;
    }
    public boolean promenaSifre(String email, String token, byte[] novaSifra) {

        //1 provera da li se nalazi u tabelu za promenu sifre
        //2 ako se nalazi да ли се токени поклапају
        //3 ako se poklapaju brisi token menjaj sifru
        List<Map<String, Object>> rezultat = b.queryForList("SELECT  token FROM promenasifre WHERE email=?", email);
        if (rezultat.isEmpty()) return false;
        Map m = rezultat.get(0);
        String tokenIZBaze = (String) m.get("token");

        if (!token.equals(tokenIZBaze)) return false;
        //sada moze sifra da se menja
        b.update("DELETE FROM promenasifre WHERE email=?", email);
        byte[] hesiranaSifra = radSaSifrom.hesirajteSifru(novaSifra);
        b.update("UPDATE korisnik SET sifra =? WHERE email=?", hesiranaSifra, email);

        return true;
    }

    public boolean izmenitePodatkeZaKorisnika(String imeNaziv, String email, byte[] sifra, String adresa, String mesto, String tel1, String tel2, String tel3) {
        List<Map<String, Object>> r = b.queryForList("SELECT email, imeNaziv, sifra, adresa,mesto, tel1, tel2, tel3, racun, prosecnaocenakupaca FROM korisnik WHERE email=? ", email);
        if (r.isEmpty()) return false;
        Korisnik k = Korisnik.korisnikIzBaze(r.get(0));
        StringBuilder deoZaPromenu = new StringBuilder("");
        ArrayList<Object> dodajuSe = new ArrayList<>();
        if (!k.getImeNaziv().equals(imeNaziv)) {
            deoZaPromenu.append(" imeNaziv=?,");
            dodajuSe.add(imeNaziv);

        }
        k.setSifra((byte[]) r.get(0).get("sifra"));
        if (!(new String(sifra)).equals("")) {//sve je  obezbedjeno da ne bude "" osim sifre
            if (!radSaSifrom.proveriteSifru(sifra, k.getSifra())) {
                //ako sifre nisu iste
                deoZaPromenu.append(" sifra=?,");
                dodajuSe.add(radSaSifrom.hesirajteSifru(sifra));
            }
        }
        //System.out.println(k.getAdresa()+" "+adresa);
        if (!k.getAdresa().equals(adresa)) {
            deoZaPromenu.append(" adresa=?,");
            dodajuSe.add(adresa);
        }
        if (!k.getMesto().equals(mesto)) {
            deoZaPromenu.append(" mesto=?,");
            dodajuSe.add(mesto);
        }
        if (!k.getTel1().equals(tel1)) {
            deoZaPromenu.append(" tel1=?,");
            dodajuSe.add(tel1);
        }
        if (!k.getTel2().equals(tel2)) {
            deoZaPromenu.append(" tel2=?,");
            dodajuSe.add(tel2);
        }
        if (!k.getTel3().equals(tel3)) {
            deoZaPromenu.append(" tel3=?,");
            dodajuSe.add(tel3);
        }
        if (deoZaPromenu.toString().equals("")) return false;
        dodajuSe.add(email);
        deoZaPromenu.deleteCharAt(deoZaPromenu.length() - 1);

        // System.out.println(deoZaPromenu);
        String query = new StringBuilder("UPDATE korisnik SET").append(deoZaPromenu).append(" WHERE email=?").toString();
        Object[] niz = dodajuSe.toArray();
        //  System.out.println(query);
        int brojRedova = b.update(query, niz);
        return brojRedova == 1;
    }


    public boolean potvrditeKorisnika(Korisnik k){
        return b.update("INSERT INTO korisnik(imeNaziv, email, sifra, adresa, mesto,tel1,tel2,tel3, token, racun, prosecnaocenakupaca) VALUES(?,?,?,?,?,?,?,?,?,?,?)", k.getImeNaziv(), k.getEmail(), k.getSifra(), k.getAdresa(), k.getMesto(), k.getTel1(), k.getTel2(), k.getTel3(), null, 0, 0) == 1;

    }


    /*
    public boolean potvrditeKorisnika(Korisnik k) {
        if (b.update("DELETE FROM nepotvrdjenikorisnik WHERE email=?", k.getEmail()) != 1) return false;
        return b.update("INSERT INTO korisnik(imeNaziv, email, sifra, adresa, mesto,tel1,tel2,tel3, token, racun) VALUES(?,?,?,?,?,?,?,?,?,?)", k.getImeNaziv(), k.getEmail(), k.getSifra(), k.getAdresa(), k.getMesto(), k.getTel1(), k.getTel2(), k.getTel3(), null, 0) == 1;

    }

     */
    public boolean obrisiteKorisnika(String email){
        return b.update("DELETE FROM korisnik WHERE email=?", email)==1;
    }

    public double izracunajteProsecnuOcenuKupaca(String prodavacEmail){
         b.update("UPDATE korisnik SET prosecnaocenakupaca=kveri.vrednost  FROM (SELECT AVG(korisnikocena.ocena) as vrednost FROM korisnikocena  WHERE prodavac_email=?) as  kveri  WHERE email=?", prodavacEmail, prodavacEmail);
         List<Map<String, Object>> r = b.queryForList("SELECT prosecnaocenakupaca FROM korisnik WHERE email=?", prodavacEmail);
         double prosek = Double.parseDouble(r.get(0).get("prosecnaocenakupaca").toString());
         return prosek;
    }

}
