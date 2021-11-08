package srb.akikrasic.domen;

import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by aki on 12/5/17.
 */
public class NarudzbenicaBezNarucenihProizvoda extends Narudzbenica {
    public static NarudzbenicaBezNarucenihProizvoda izBaze(Map<String, Object> mapa){
        NarudzbenicaBezNarucenihProizvoda n = new NarudzbenicaBezNarucenihProizvoda();
        n.setId((Integer)mapa.get("id"));
        n.setDatum(((Timestamp)mapa.get("datum")).toLocalDateTime());
        n.setZbir((Double)mapa.get("zbir"));
        n.setChargeId((String)mapa.get("charge_id"));
        n.setDostava((String)mapa.get("dostava"));
       // n.setNaruceniProizvodi(null);
        return n;
    }
    public static NarudzbenicaBezNarucenihProizvoda izNarudzbenice(Narudzbenica nar){
        NarudzbenicaBezNarucenihProizvoda n = new NarudzbenicaBezNarucenihProizvoda();
        n.setId(nar.getId());
        n.setDatum(nar.getDatum());
        n.setZbir(nar.getZbir());
        n.setChargeId(nar.getChargeId());
        n.setDostava(nar.getDostava());
        n.setKupac(nar.getKupac());
        //n.setNaruceniProizvodi(null);
        return n;
    }

}
