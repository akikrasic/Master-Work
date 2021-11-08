package srb.akikrasic.stripe;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.rezultati.stari.registrovanikorisnik.RezultatRegistrovaniKorisnik;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aki on 9/25/17.
 */
@RestController
public class TestStripe {

    private String javni="pk_test_MRjETwcI7j0DMewnpTlmHQte";
    private String privatni = "sk_test_j369HNR1qHiIDuBMFGTnlZ6Q";

    @RequestMapping(value="/posaljiteToken", method=RequestMethod.GET)
    public RezultatRegistrovaniKorisnik proba(@RequestParam String token) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        System.out.println("pozvano");
        System.out.println(token);
        Stripe.apiKey=privatni;
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("amount", 10000);
        param.put("currency", "rsd");
        param.put("description", " Опис проба");
        param.put("source", token);
        Charge charge = Charge.create(param);
        System.out.println(charge.getId());
        System.out.println(charge.getPaid());
        System.out.println(charge);
        System.out.println(charge.getAmount());
        RezultatRegistrovaniKorisnik r = new RezultatRegistrovaniKorisnik();
        r.setDaLiJeURedu(true);
        return r;
    }
}
