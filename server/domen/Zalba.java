package srb.akikrasic.domen;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by aki on 10/17/17.
 */
public class Zalba implements Domen<Integer>{
    private int id;
    private String tekstKupca;
    private String tekstProdavca;
    private LocalDateTime datumKupca;
    private LocalDateTime datumProdavca;


    public String getTekstKupca() {
        return tekstKupca;
    }

    public void setTekstKupca(String tekstKupca) {
        this.tekstKupca = tekstKupca;
    }

    public LocalDateTime getDatumProdavca() {
        return datumProdavca;
    }

    public void setDatumProdavca(LocalDateTime datumProdavca) {
        this.datumProdavca = datumProdavca;
    }

    public LocalDateTime getDatumKupca() {
        return datumKupca;
    }

    public void setDatumKupca(LocalDateTime datumKupca) {
        this.datumKupca = datumKupca;
    }

    public String getTekstProdavca() {
        return tekstProdavca;
    }

    public void setTekstProdavca(String tekstProdavca) {
        this.tekstProdavca = tekstProdavca;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Zalba izBaze(Map<String, Object> mapa){
       Zalba z = new Zalba();
       z.setId((Integer)mapa.get("id"));
       z.setDatumKupca(((Timestamp)mapa.get("datum_kupca")).toLocalDateTime());
       z.setTekstKupca((String)mapa.get("tekst_kupca"));
       if(mapa.get("tekst_prodavca")!=null && mapa.get("datum_prodavca")!=null) {
           z.setTekstProdavca((String) mapa.get("tekst_prodavca"));
           z.setDatumProdavca(((Timestamp) mapa.get("datum_prodavca")).toLocalDateTime());
       }

        return z;
    }
    @Override
    public Integer getIdentitet() {
        return getId();
    }
}
