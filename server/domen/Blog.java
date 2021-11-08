package srb.akikrasic.domen;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by aki on 2/20/18.
 */
public class Blog implements Domen<Integer>{
    private int id;
    private LocalDateTime datum;
    private String naslov;
    private String tekst;
    private Korisnik korisnik;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public static Blog izBaze(Map<String, Object> m){
        Blog b = new Blog();
        b.setId((Integer)m.get("id"));
        b.setDatum(((Timestamp)m.get("datum")).toLocalDateTime());
        b.setNaslov((String)m.get("naslov"));
        b.setTekst((String)m.get("tekst"));
        return b;
    }

    @Override
    public Integer getIdentitet() {
        return this.getId();
    }
}
