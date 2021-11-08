package srb.akikrasic.domen.uporedjivaci;

/**
 * Created by aki on 12/8/17.
 */

import org.springframework.stereotype.Component;
import srb.akikrasic.domen.Kategorija;

import java.util.Comparator;

@Component
public class KategorijaUporedjivac implements Comparator<Kategorija> {


    @Override
    public int compare(Kategorija o1, Kategorija o2) {
        return o1.getNaziv().compareTo(o2.getNaziv());
    }
}
