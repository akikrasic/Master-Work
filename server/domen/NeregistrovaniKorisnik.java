package srb.akikrasic.domen;

import java.util.Map;

/**
 * Created by aki on 8/6/17.
 */
public class NeregistrovaniKorisnik implements Domen<String>{
    private int id;
    private String imeNaziv;
    private String email;
    private byte[] sifra;
    private String adresa;
    private String mesto;
    private String tel1;
    private String tel2;
    private String tel3;
    private String token;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static NeregistrovaniKorisnik napraviteNeregistrovanogKorisnikaIzMape(Map<String, Object> mapa){
        NeregistrovaniKorisnik k = new NeregistrovaniKorisnik();
        k.setEmail((String)mapa.get("email"));
        k.setImeNaziv((String)mapa.get("imeNaziv"));
        k.setSifra((byte[])mapa.get("sifra"));
        k.setAdresa((String)mapa.get("adresa"));
        k.setMesto((String)mapa.get("mesto"));
        k.setTel1((String)mapa.get("tel1"));
        k.setTel2((String)mapa.get("tel2"));
        k.setTel3((String)mapa.get("tel3"));
        k.setId((Integer)mapa.get("id"));
        k.setToken((String)mapa.get("token"));
        return k;
    }
    @Override
    public String getIdentitet() {
        return getEmail();
    }
}
