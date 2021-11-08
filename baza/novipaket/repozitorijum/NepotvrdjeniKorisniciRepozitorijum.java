package srb.akikrasic.baza.novipaket.repozitorijum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.BazaNepotvrdjeniKorisnici;
import srb.akikrasic.baza.novipaket.repozitorijum.osnovne.RepozitorijumOsnovna;
import srb.akikrasic.domen.Korisnik;
import srb.akikrasic.domen.NeregistrovaniKorisnik;

import java.util.Map;

/**
 * Created by aki on 11/24/17.
 */
@Component
public class NepotvrdjeniKorisniciRepozitorijum extends RepozitorijumOsnovna<String, NeregistrovaniKorisnik> {

    @Autowired
    public void setBaza(BazaNepotvrdjeniKorisnici b){
        super.setBazaOsnovna(b);
        this.b = b;
        this.query="SELECT * FROM nepotvrdjenikorisnik";
    }
    private BazaNepotvrdjeniKorisnici b;

    @Autowired
    private KorisniciRepozitorijum korisniciR;

    public void setKorisniciR(KorisniciRepozitorijum korisniciR) {
        this.korisniciR = korisniciR;
    }

    private NeregistrovaniKorisnik napraviteNepotvrdjenogKorisnikaOdPodataka(String imeNaziv, String email, String adresa, String mesto, String tel1, String tel2, String tel3, String token) {
        NeregistrovaniKorisnik nk = new NeregistrovaniKorisnik();
        nk.setImeNaziv(imeNaziv);
        nk.setEmail(email);
        nk.setAdresa(adresa);
        nk.setMesto(mesto);
        nk.setTel1(tel1);
        nk.setTel2(tel2);
        nk.setTel3(tel3);
        nk.setToken(token);
        return nk;
    }


    public void snimiteNovogNepotvrdjenogKorisnika(String imeNaziv, String email, byte[] sifra, String adresa, String mesto, String tel1, String tel2, String tel3, String token) {
        NeregistrovaniKorisnik nk = this.napraviteNepotvrdjenogKorisnikaOdPodataka(imeNaziv, email, adresa, mesto, tel1, tel2, tel3, token);
        this.dodajteUMapu(nk);
        //posto nema generacije vrednosti, elegantno se sve uradi
        b.snimiteNovogNepotvrdjenogKorisnika(imeNaziv, email, sifra, adresa, mesto, tel1, tel2, tel3, token);
    }


    public boolean daLiJeEmailSlobodanKodNepotvrdjenogKorisnika(String email){
        return this.mapa.get(email)==null;//ako je null slobodan je
    }/*
    public boolean daLiJeEmailSlobodanUObeTabele(String email) {
        List<Map<String, Object>> rezultat = b.queryForList("SELECT email FROM nepotvrdjenikorisnik WHERE email=?", email);
        if (!rezultat.isEmpty()) return false;
        rezultat = b.queryForList("SELECT email FROM korisnik WHERE email=?", email);
        return rezultat.isEmpty();
    }
*/
    public Korisnik daLiJeMogucaPotvrdaKorisnika(String email, String token){
        NeregistrovaniKorisnik nk = this.mapa.get(email);
        if(nk==null){
            return null;
        }
        if(!token.equals(nk.getToken())) {
            return null;
        }

        return Korisnik.korisnikIzneregistrovanogKorisnika(nk);
    }
    /*
    public Korisnik daLiJeMogucaPotvrdaKorisnika(String email, String token) {
        List<Map<String, Object>> rezultat = b.queryForList("SELECT id, email, imenaziv, sifra, adresa, mesto, tel1, tel2, tel3, token FROM nepotvrdjenikorisnik WHERE email=?", email);
        //System.out.println(rezultat);
        if (rezultat.isEmpty()) return null;
        String tokenIzBaze = (String) rezultat.get(0).get("token");

        if (!token.equals(tokenIzBaze)) return null;
        NeregistrovaniKorisnik nk = NeregistrovaniKorisnik.napraviteNeregistrovanogKorisnikaIzMape(rezultat.get(0));
        return Korisnik.korisnikIzneregistrovanogKorisnika(nk);
    }
*/

    public boolean obrisiteNepotvrdjenogKorisnika(String email){
        this.mapa.remove(email);
        return b.obrisiteNepotvrdjenogKorisnika(email);
    }
    public boolean potvrditeKorisnika(Korisnik k){
        if(!this.obrisiteNepotvrdjenogKorisnika(k.getEmail()))return false;
        return this.korisniciR.potvrditeKorisnika(k);

    }

    public boolean daLiJeEmailSlobodanUObeTabele(String email){
        if(!this.daLiJeEmailSlobodanKodNepotvrdjenogKorisnika(email))return false;
        return korisniciR.daLiJeEmailSlobodanKodPotvrdjenogKorisnika(email);
    }
    /*
    public boolean potvrditeKorisnika(Korisnik k) {
        if (b.update("DELETE FROM nepotvrdjenikorisnik WHERE email=?", k.getEmail()) != 1) return false;
        return b.update("INSERT INTO korisnik(imeNaziv, email, sifra, adresa, mesto,tel1,tel2,tel3, token, racun) VALUES(?,?,?,?,?,?,?,?,?,?)", k.getImeNaziv(), k.getEmail(), k.getSifra(), k.getAdresa(), k.getMesto(), k.getTel1(), k.getTel2(), k.getTel3(), null, 0) == 1;

    }
    */
    public Map<String, NeregistrovaniKorisnik> getNepotvrdjeniKorisniciMapa(){
        return this.mapa;
    }

    @Override
    protected NeregistrovaniKorisnik napraviteReferencu(Map<String, Object> m) {
        return NeregistrovaniKorisnik.napraviteNeregistrovanogKorisnikaIzMape(m);
    }
}