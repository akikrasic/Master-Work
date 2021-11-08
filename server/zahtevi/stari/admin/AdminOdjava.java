package srb.akikrasic.zahtevi.stari.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.zahtevi.rezultati.stari.admin.*;
import srb.akikrasic.token.Tokeni;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.zahtevi.rezultati.novi.admin.odjava.RezultatOdjaveAdmin;

/**
 * Created by aki on 8/17/17.
 */
//@RestController
public class AdminOdjava {

    @Autowired
    private Tokeni radSaTokenima;

    @Autowired
    private Baza b;
    @Autowired
    private AdminRezultat rez;

    @RequestMapping(value = "/adminOdjava", method= RequestMethod.GET)
    public RezultatOdjaveAdmin adminOdjava(@RequestHeader("Authorization")String autorizacija, @RequestParam String token){
        RezultatOdjaveAdmin r = new RezultatOdjaveAdmin();
        r.setOdjavaUspesna(radSaTokenima.adminOdjava(token));
        return r;

    }



}
