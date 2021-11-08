package srb.akikrasic.domen;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by aki on 10/30/17.
 */
public class KomentarIOcena implements Domen<Integer> {
    private int id;
    private Korisnik korisnik;
    private Proizvod proizvod;
    private String tekst;
    private LocalDateTime datum;
    private int ocena;

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

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public static KomentarIOcena izBaze(Map<String, Object> mapa){
        KomentarIOcena k = new KomentarIOcena();
        k.setId((Integer)mapa.get("id"));
        if(mapa.get("tekst")!=null) {
            k.setTekst((String) mapa.get("tekst"));
            k.setDatum(((Timestamp) mapa.get("datum")).toLocalDateTime());
        }
        if(mapa.get("ocena")!=null) {
            k.setOcena((Integer) mapa.get("ocena"));
        }
        return k;
    }
    @Override
    public Integer getIdentitet() {
        return getId();
    }
}
