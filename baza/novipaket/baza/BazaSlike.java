package srb.akikrasic.baza.novipaket.baza;

import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;

import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/26/17.
 */
@Component
public class BazaSlike extends BazaOsnovna {

    public long ucitajteVrednostSlikeNaPocetku() {
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM slika");
        return (Long) rez.get(0).get("vrednost");
    }

    public void sacuvajteVrednostSlikeNaKraju(long vrednost) {
        b.update("UPDATE slika SET vrednost=?", vrednost);
    }

}
