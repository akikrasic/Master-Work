package srb.akikrasic.domen;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by aki on 11/10/17.
 */
public class Poruka {
    private LocalDateTime datum;
    private String tekst;
    private String od;
    private String za;

    public String getOd() {
        return od;
    }

    public void setOd(String od) {
        this.od = od;
    }

    public String getZa() {
        return za;
    }

    public void setZa(String za) {
        this.za = za;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
    public static Poruka izBaze(Map<String, Object> mapa){
        Poruka p = new Poruka();
        p.setDatum(((Timestamp)mapa.get("datum")).toLocalDateTime());
        p.setTekst((String)mapa.get("tekst"));
        p.setOd((String)mapa.get("od"));
        p.setZa((String)mapa.get("za"));
        return p;
    }

}
