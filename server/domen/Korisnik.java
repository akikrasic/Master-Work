package srb.akikrasic.domen;

import java.util.List;
import java.util.Map;

/**
 * Created by aki on 8/6/17.
 */
public class Korisnik implements Domen<String> {

    private String imeNaziv;
    private String email;
    private byte[] sifra;
    private String adresa;
    private String mesto;
    private String tel1;
    private String tel2;
    private String tel3;

    private double racun;

    private double prosecnaOcenaKupaca;


    private List<KorisnikOcena> oceneKorisnika;

    public String getImeNaziv() {
        return imeNaziv;
    }

    public void setImeNaziv(String imeNaziv) {
        this.imeNaziv = imeNaziv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getSifra() {
        return sifra;
    }

    public void setSifra(byte[] sifra) {
        this.sifra = sifra;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getTel3() {
        return tel3;
    }

    public void setTel3(String tel3) {
        this.tel3 = tel3;
    }

    public double getRacun() {
        return racun;
    }

    public void setRacun(double racun) {
        this.racun = racun;
    }

    public List<KorisnikOcena> getOceneKorisnika() {
        return oceneKorisnika;
    }

    public double getProsecnaOcenaKupaca() {
        return prosecnaOcenaKupaca;
    }

    public void setProsecnaOcenaKupaca(double prosecnaOcenaKupaca) {
        this.prosecnaOcenaKupaca = prosecnaOcenaKupaca;
    }

    public void setOceneKorisnika(List<KorisnikOcena> oceneKorisnika) {
        this.oceneKorisnika = oceneKorisnika;
    }

    public static Korisnik korisnikIzneregistrovanogKorisnika(NeregistrovaniKorisnik nk){
        Korisnik k = new Korisnik();
        k.setEmail(nk.getEmail());
        k.setImeNaziv(nk.getImeNaziv());
        k.setSifra(nk.getSifra());
        k.setAdresa(nk.getAdresa());
        k.setMesto(nk.getMesto());
        k.setTel1(nk.getTel1());
        k.setTel2(nk.getTel2());
        k.setTel3(nk.getTel3());
        return k;
    }
    public static Korisnik korisnikIzBaze(Map<String, Object> mapa){
        Korisnik k = new Korisnik();
        k.setEmail((String)mapa.get("email"));
        k.setImeNaziv((String)mapa.get("imeNaziv"));
       // k.setSifra((byte[])mapa.get("sifra"));
        k.setAdresa((String)mapa.get("adresa"));
        k.setMesto((String)mapa.get("mesto"));
        k.setTel1((String)mapa.get("tel1"));
        k.setTel2((String)mapa.get("tel2"));
        k.setTel3((String)mapa.get("tel3"));

        //dodato 23.02.2018.
        String s = mapa.get("prosecnaocenakupaca").toString();
        k.setProsecnaOcenaKupaca(Double.parseDouble(s));
        //nazalost tako mora
        //kraj dodavanja 23.05.2018.


        //k.setRacun((Double)mapa.get("racun"));
        //k.setToken((String)mapa.get("token"));
        return k;
    }
    public boolean equals(Object o){
        return email.equals(((Korisnik)o).getEmail());
    }

    @Override
    public String getIdentitet() {
        return getEmail();
    }
}
