package srb.akikrasic.domen;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by aki on 2/23/18.
 */
public class KorisnikOcena implements Domen<Integer>   {
    private int id;
    private Korisnik kupac;
    private Korisnik prodavac;
    private LocalDateTime datum;
    private int ocena;

    @Override
    public Integer getIdentitet() {
        return id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Korisnik getKupac() {
        return kupac;
    }

    public void setKupac(Korisnik kupac) {
        this.kupac = kupac;
    }

    public Korisnik getProdavac() {
        return prodavac;
    }

    public void setProdavac(Korisnik prodavac) {
        this.prodavac = prodavac;
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
    public static KorisnikOcena izBaze(Map<String, Object> m){
        KorisnikOcena k = new KorisnikOcena();
        k.setId((Integer)m.get("id"));
        k.setDatum(((Timestamp)m.get("datum")).toLocalDateTime());
        k.setOcena((Integer)m.get("ocena"));
        return k;
    }
}
