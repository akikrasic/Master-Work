package srb.akikrasic.baza.novipaket.baza;

import org.springframework.stereotype.Component;
import srb.akikrasic.baza.novipaket.baza.osnovneklase.BazaOsnovna;

import java.util.List;
import java.util.Map;

/**
 * Created by aki on 11/26/17.
 */
@Component
public class BazaPrivremeniBroj extends BazaOsnovna {

    public long ucitajteVrednostBrojaPrivremenogProizvodaNaPocetku() {
        List<Map<String, Object>> rez = b.queryForList("SELECT * FROM privremeniproizvodbroj");
        return (Long) rez.get(0).get("vrednost");
    }

    public void sacuvajteVrednostBrojaPrivremenogPRoizvodaNaKraju(long vr) {
        b.update("UPDATE privremeniproizvodbroj SET vrednost=?", vr);
    }
}
