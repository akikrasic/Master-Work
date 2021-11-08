package srb.akikrasic.zahtevi.stari.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.Baza;
import srb.akikrasic.zahtevi.rezultati.novi.admin.izvestaj.AdminIzvestajRezultat;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;

/**
 * Created by aki on 11/24/17.
 */
//@RestController

public class AdminIzvestaj {
    @Autowired
    private Baza b;
    private Integer konverzija(String s){
        Integer r =null;
        try{
            r = Integer.parseInt(s);
        }catch(Exception e){
            return null;
        }
        return r;
    }

    @RequestMapping(value="/adminIzvestaj", method= RequestMethod.GET)
    public RezultatAdmin vratiteIzvestaj(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String limitS,
            @RequestParam String offsetS
    ){
        AdminIzvestajRezultat ai = new AdminIzvestajRezultat();
        if(limitS==null || offsetS==null){
            ai.dosloJeDoNeovlascenogPristupa();
            return ai;
        }
        Integer limit = this.konverzija(limitS);
        Integer offset = this.konverzija(offsetS);
        if(limit==null||offset==null){
            ai.dosloJeDoNeovlascenogPristupa();
            return ai;
        }
        ai.setDaLiJeURedu(true);
        ai.setIzvestaj(b.izvestajZaAdmina(limit, offset));
        return ai;
    }
}
