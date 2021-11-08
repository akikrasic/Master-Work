package srb.akikrasic.domen;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * Created by aki on 9/17/17.
 */
public class Komentar implements Domen<Integer> {
    private int id;
    private Korisnik korisnik;
    private Proizvod proizvod;
    private String tekst;
    private LocalDateTime datum;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Proizvod getProizvod() {
        return proizvod;
    }

    public void setProizvod(Proizvod proizvod) {
        this.proizvod = proizvod;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public static Komentar izBaze(Map<String, Object> mapa){
        Komentar k = new Komentar();
        k.setId((Integer)mapa.get("komentar_id"));
        k.setTekst((String)mapa.get("tekst"));
        k.setDatum(((Timestamp)mapa.get("datum")).toLocalDateTime());
        return k;
    }

    @Override
    public Integer getIdentitet() {
        return getId();
    }
}
