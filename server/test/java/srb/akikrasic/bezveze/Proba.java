package srb.akikrasic.bezveze;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import srb.akikrasic.domen.Korisnik;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aki on 10/6/17.
 */
public class Proba {
    @Test
    public void proba() throws Exception {
        ObjectMapper m = new ObjectMapper();
        String json="{\"ime\":\"Mika\", \"godina\":50}";
        HashMap<String, Object> mapa = m.readValue(json,new TypeReference<Map<String, String>>(){});
        System.out.println(mapa);
        int x = Integer.parseInt((String)mapa.get("godina"));
        System.out.println(x);

        Korisnik k = new Korisnik();
        k.setImeNaziv("Mika");
        k.setEmail("mika@pera.com");

        System.out.println(m.writeValueAsString(k));
    }
}
