package srb.akikrasic.zahtevi.novi.svikorisnici.dalijeemailslobodan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import srb.akikrasic.baza.novipaket.repozitorijum.NepotvrdjeniKorisniciRepozitorijum;

import org.springframework.web.bind.annotation.*;
/**
 * Created by aki on 12/20/17.
 */
@RestController
public class DaLiJeEmailSlobodan {

    @Autowired
    private NepotvrdjeniKorisniciRepozitorijum nepotvrdjeniKorisniciRepozitorijum;

    @RequestMapping(value = "/daLiJeEmailSlobodan",method= RequestMethod.GET)
    public String proveraEmaila(@RequestParam String email){
        if(nepotvrdjeniKorisniciRepozitorijum.daLiJeEmailSlobodanUObeTabele(email)){
            return  "true";
        }
        return "false";
    }

}
