package srb.akikrasic.zahtevi.novi.admin.odjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.rezultati.novi.admin.odjava.RezultatOdjaveAdmin;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;
import srb.akikrasic.token.Tokeni;

/**
 * Created by aki on 8/17/17.
 */
@RestController
public class AdminOdjava {

    @Autowired
    private Tokeni radSaTokenima;



    @RequestMapping(value = "/adminOdjava", method= RequestMethod.GET)
    public RezultatAdmin izvrsiteZahtev(@RequestHeader("Authorization")String autorizacija, @RequestParam String token){
        return vratiteRezultat(radSaTokenima.adminOdjava(token));
    }

    private RezultatAdmin vratiteRezultat(boolean uspesno){
        RezultatOdjaveAdmin r = new RezultatOdjaveAdmin();
        r.setOdjavaUspesna(uspesno);
        return r;
    }




}
