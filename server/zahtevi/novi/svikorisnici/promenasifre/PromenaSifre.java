package srb.akikrasic.zahtevi.novi.svikorisnici.promenasifre;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.KorisniciRepozitorijum;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.promenasifre.RezultatPromeneSifre;

import java.util.LinkedHashMap;

@RestController
public class PromenaSifre {

    @Autowired
    private KorisniciRepozitorijum korisniciRepozitorijum;

    @RequestMapping(value="/promenaSifre", method=RequestMethod.PUT)
    public RezultatPromeneSifre promeniteSifru(@RequestBody LinkedHashMap<String, Object> parametri ){
        String email = (String)parametri.get("email");
        String kodiraniToken = (String)parametri.get("token");
        String token = new String(Base64.decode(kodiraniToken));
        byte[] novaSifra = ((String)parametri.get("novaSifra")).getBytes();
        return vratiteRezultat(korisniciRepozitorijum.promenaSifre(email, token, novaSifra));

    }

    private RezultatPromeneSifre vratiteRezultat(boolean uspesnoPromenjenaSifra){
        RezultatPromeneSifre r = new RezultatPromeneSifre();
        r.setUspesnoPromenjenaSifra(uspesnoPromenjenaSifra);
        return r;
    }


}