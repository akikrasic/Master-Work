package srb.akikrasic.zahtevi.novi.admin.izvestaj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import srb.akikrasic.baza.novipaket.repozitorijum.NaruceniProizvodiRepozitorijum;
import srb.akikrasic.domen.NaruceniProizvod;
import srb.akikrasic.zahtevi.rezultati.novi.admin.izvestaj.AdminIzvestajRezultat;
import srb.akikrasic.zahtevi.rezultati.stari.admin.RezultatAdmin;
import srb.akikrasic.zahtevi.novi.osnovne.AdminZahtev;

import java.util.List;

/**
 * Created by aki on 11/24/17.
 */
@RestController
public class AdminIzvestaj extends AdminZahtev {

    @Autowired
    private NaruceniProizvodiRepozitorijum naruceniProizvodiRepozitorijum;



    @RequestMapping(value="/adminIzvestaj", method= RequestMethod.GET)
    public RezultatAdmin izvrsiteZahtev(
            @RequestHeader("Authorization") String autorizacija,
            @RequestParam String limitS,
            @RequestParam String offsetS
    ){
        if(limitS==null || offsetS==null){
            return dosloJeDoNeovlascenogPristupa();

        }
        Integer limit = this.konverzija.konverzijaInteger(limitS);
        Integer offset = this.konverzija.konverzijaInteger(offsetS);
        if(limit==null||offset==null){
            return dosloJeDoNeovlascenogPristupa();

        }

        return vratiteRezultat(naruceniProizvodiRepozitorijum.izvestajZaAdmina(limit, offset));

    }

    private RezultatAdmin vratiteRezultat(List<NaruceniProizvod> izvestaj){
        AdminIzvestajRezultat ai = new AdminIzvestajRezultat();
        ai.setDaLiJeURedu(true);
        ai.setIzvestaj(izvestaj);
        return ai;
    }
}
