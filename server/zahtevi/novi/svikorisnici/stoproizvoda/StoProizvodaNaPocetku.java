package srb.akikrasic.zahtevi.novi.svikorisnici.stoproizvoda;

/**
 * Created by aki on 9/17/17.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import srb.akikrasic.baza.novipaket.repozitorijum.ProizvodiRepozitorijum;
import srb.akikrasic.domen.Proizvod;
import srb.akikrasic.zahtevi.rezultati.novi.svikorisnici.stoproizvoda.RezultatStoProizvoda;

import java.util.List;

@RestController
public class StoProizvodaNaPocetku {

   @Autowired
   private ProizvodiRepozitorijum proizvodiRepozitorijum;

    @RequestMapping (value="/vratiteStoPoslednjihProizvoda", method = RequestMethod.GET)
    public RezultatStoProizvoda vratiteStoProizvoda(){
        //r.setProizvodi(b.vratiteStoProizvoda());
        return vratiteRezultat(proizvodiRepozitorijum.vratiteStoProizvodaNovi());

    }

    private RezultatStoProizvoda vratiteRezultat(List<Proizvod> listaProizvoda){
        RezultatStoProizvoda r = new RezultatStoProizvoda();
        r.setProizvodi(listaProizvoda);
        r.setUkBrojProizvoda(r.getProizvodi().size());
        return r;
    }
}
