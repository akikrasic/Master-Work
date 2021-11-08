package srb.akikrasic.baza.novipaket.baza.osnovneklase;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by aki on 11/26/17.
 */
public class BazaOsnovna {
    @Autowired
    protected BazaReferenca b;

    public Stream<Map<String, Object>> izvrsiteQuery (String query, Object...listaArgumenata){
        return b.queryForList(query, listaArgumenata).stream();
    }


}
