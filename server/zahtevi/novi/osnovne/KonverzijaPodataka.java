package srb.akikrasic.zahtevi.novi.osnovne;

import org.springframework.stereotype.Component;

/**
 * Created by aki on 12/17/17.
 */
@Component
public class KonverzijaPodataka {

    public Integer konverzijaInteger(String idString) {
        Integer i = null;

        try {
            i = Integer.parseInt(idString);
        } catch (Exception e) {
            return null;
        }

        return i;

    }
    public Long konverzijaLong(String idString) {
        Long l = null;

        try {
            l = Long.parseLong(idString);
        } catch (Exception e) {
            return null;
        }

        return l;

    }

    public Double konverzijaDouble(String cenaString) {
        Double d = null;
        try {
            d = Double.parseDouble(cenaString);
        } catch (Exception e) {
            return null;
        }
        return d;
    }



    public Boolean konverzijaBoolean(String aktivan) {
        Boolean b = null;
        try {
            b = Boolean.parseBoolean(aktivan);
        } catch (Exception e) {
            return null;
        }
        return b;
    }


}
