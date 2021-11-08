package srb.akikrasic.domen;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by aki on 8/22/17.
 */
public class Narudzbenica implements Domen<Integer> {

    private int id;
    private LocalDateTime datum;
    private Korisnik kupac;
    private String dostava;
    List<NaruceniProizvod> naruceniProizvodi;
    String chargeId;
    double zbir;

    public Narudzbenica(){
        naruceniProizvodi= new ArrayList<>();
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public Korisnik getKupac() {
        return kupac;
    }

    public void setKupac(Korisnik kupac) {
        this.kupac = kupac;
    }

    public String getDostava() {
        return dostava;
    }

    public void setDostava(String dostava) {
        this.dostava= dostava;
    }


    public List<NaruceniProizvod> getNaruceniProizvodi() {
        return naruceniProizvodi;
    }

    public void setNaruceniProizvodi(List<NaruceniProizvod> naruceniProizvodi) {
        this.naruceniProizvodi = naruceniProizvodi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public double getZbir() {
        return zbir;
    }

    public void setZbir(double zbir) {
        this.zbir = zbir;
    }
    public static Narudzbenica izBaze(Map<String, Object> mapa){
        Narudzbenica n = new Narudzbenica();
        n.setId((Integer)mapa.get("id"));
        n.setDatum(((Timestamp)mapa.get("datum")).toLocalDateTime());
        n.setZbir((Double)mapa.get("zbir"));
        n.setChargeId((String)mapa.get("charge_id"));
        n.setDostava((String)mapa.get("dostava"));
        return n;
    }
    public static Narudzbenica izNarudzbeniceBezNarucenihProizvoda(NarudzbenicaBezNarucenihProizvoda nbp){
        Narudzbenica n = new Narudzbenica();
        n.setId(nbp.getId());
        n.setDatum(nbp.getDatum());
        n.setZbir(nbp.getZbir());
        n.setChargeId(nbp.getChargeId());
        n.setDostava(nbp.getDostava());
        n.setKupac(nbp.getKupac());
        return n;
    }
    @Override
    public Integer getIdentitet() {
        return getId();
    }
}
