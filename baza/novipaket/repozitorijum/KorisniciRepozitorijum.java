package srb.akikrasic.baza.novipaket.repozitorijum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.BazaKorisnici;
import srb.akikrasic.baza.novipaket.repozitorijum.osnovne.RepozitorijumOsnovna;
import srb.akikrasic.domen.Korisnik;

import java.util.Map;

/**
 * Created by aki on 11/24/17.
 */
@Component
public class KorisniciRepozitorijum extends RepozitorijumOsnovna<String, Korisnik> {


    @Autowired
    public void setBaza(BazaKorisnici b){
        super.setBazaOsnovna(b);
        this.b = b;

        this.query="SELECT * FROM korisnik";
    }

    private BazaKorisnici b;

    private void dodajteKorisnikaUMapu(Korisnik k){
        this.mapa.put(k.getEmail(), k);
    }
    private void obrisiteKorisnikaIzMapu(String email){this.mapa.remove(email);}


    public Korisnik pretragaKorisnikaPoEmailu(String email){
        System.out.println("Korisnici mapa: "+mapa);
        return mapa.get(email);
    }

    public boolean potvrditeKorisnika(Korisnik k){
        dodajteKorisnikaUMapu(k);
        return b.potvrditeKorisnika(k);
    }
    public boolean daLiJeEmailSlobodanKodPotvrdjenogKorisnika(String email){
        return this.mapa.get(email)==null;
    }


    public String posaljiteZahtevZaPromenuSifre(String email) {
       return b.posaljiteZahtevZaPromenuSifre(email);
    }
    public boolean promenaSifre(String email, String token, byte[] novaSifra) {

        return b.promenaSifre(email, token,  novaSifra);
    }

    public boolean izmenitePodatkeZaKorisnika(String imeNaziv, String email, byte[] sifra, String adresa, String mesto, String tel1, String tel2, String tel3) {
        boolean rez = b.izmenitePodatkeZaKorisnika( imeNaziv,  email,  sifra,  adresa,  mesto,  tel1,  tel2,  tel3);
        if(!rez )return false;

        Korisnik k = mapa.get(email);


        if (!k.getImeNaziv().equals(imeNaziv)) {
            k.setImeNaziv(imeNaziv);

        }

        if (!k.getAdresa().equals(adresa)) {
            k.setAdresa(adresa);
        }
        if (!k.getMesto().equals(mesto)) {
            k.setMesto(mesto);
        }
        if (!k.getTel1().equals(tel1)) {
           k.setTel1(tel1);
        }
        if (!k.getTel2().equals(tel2)) {
            k.setTel2(tel2);
        }
        if (!k.getTel3().equals(tel3)) {
            k.setTel3(tel3);
        }
        return true;
        //jeste ovo sporije, ali posto je ubrzana pretraga i ponovno pravljenje reference svaki put, ovo usporavanje je zanemarljivo
    }



    public boolean obrisiteKorisnika(String email){
        this.obrisiteKorisnikaIzMapu(email);
        return b.obrisiteKorisnika(email);
    }
    public double izracunajteProsecnuOcenuKupaca(String prodavacEmail){
        double prosek = b.izracunajteProsecnuOcenuKupaca(prodavacEmail);
        mapa.get(prodavacEmail).setProsecnaOcenaKupaca(prosek);
        return prosek;
    }

    @Override
    protected Korisnik napraviteReferencu(Map<String, Object> m) {

        return Korisnik.korisnikIzBaze(m);
    }







}
