package srb.akikrasic.stripe;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aki on 10/7/17.
 */
@Component
public class RadSaPlacanjem {

    private String javni="pk_test_MRjETwcI7j0DMewnpTlmHQte";
    private String privatni = "sk_test_j369HNR1qHiIDuBMFGTnlZ6Q";

    public String platiteIVratiteIdChargea(String token, double zbir){
        Stripe.apiKey=privatni;
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("amount", (int)zbir*100);
        param.put("currency", "rsd");
        param.put("description", "Плаћање");
        param.put("source", token);
        Charge charge=null;
        try {
             charge = Charge.create(param);
        } catch (AuthenticationException e) {
           // e.printStackTrace();
            return null;
        } catch (InvalidRequestException e) {
           // e.printStackTrace();
            return null;
        } catch (APIConnectionException e) {
           // e.printStackTrace();
            return null;
        } catch (CardException e) {
          //  e.printStackTrace();
            return null;
        } catch (APIException e) {
           // e.printStackTrace();
            return null;
        }
        return charge.getId();
    }
    public boolean ponistitePlacanje(String id, double kolicinaZaPovracaj){
        Stripe.apiKey = "sk_test_BQokikJOvBiI2HlWgH4olfQ2";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("charge", id);
        params.put("amount", kolicinaZaPovracaj*100);
        Refund refund =null;
        try {
            refund=Refund.create(params);
        } catch (AuthenticationException e) {
           // e.printStackTrace();
            return false;
        } catch (InvalidRequestException e) {
           // e.printStackTrace();
            return false;
        } catch (APIConnectionException e) {
           // e.printStackTrace();
            return false;
        } catch (CardException e) {
           // e.printStackTrace();
            return false;
        } catch (APIException e) {
           // e.printStackTrace();
            return false;
        }
        return true;
    }

}
